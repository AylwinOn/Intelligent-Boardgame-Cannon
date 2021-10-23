package com.cannon.engine.AI;


import com.cannon.engine.AI.support.BoardEvaluator;
import com.cannon.engine.AI.support.MoveStrategy;
import com.cannon.engine.AI.support.StandardBoardEvaluator;
import com.cannon.engine.player.Alliance;
import com.cannon.engine.board.Board;
import com.cannon.engine.board.BoardUtils;
import com.cannon.engine.board.Move;
import com.cannon.engine.player.MoveTransition;
import com.cannon.engine.player.Player;
import com.cannon.pgn.ZobristHashing;
import com.google.common.collect.ComparisonChain;
import com.google.common.primitives.Ints;

import java.util.*;

import static com.cannon.engine.board.Move.*;
import static com.google.common.collect.Ordering.from;

public class AlphaTwo extends Observable implements MoveStrategy {

    private final BoardEvaluator evaluator;
    private final int searchDepth;
    private final MoveSorter moveSorter;
    private final int quiescenceFactor;
    private long timeResources;
    private long boardsEvaluated;
    private long executionTime;
    private int quiescenceCount;
    private static final int MAX_QUIESCENCE = 5000 * 5;
    private int cutOffsProduced;
    private int nodesExplored = 0;
    private int depthExplored = 0;
    private Map<Long,tableNode> transposition = new HashMap<>();
    public ZobristHashing zobrist;

    private class tableNode{
        protected int score;
        protected int depth;
        protected int flag;
        public tableNode(Move bestMove, int score, int depth, int flag){
            this.score = score;
            this.depth = depth;
            this.flag = flag;
        }
    }

    private enum MoveSorter {

        SORT {
            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return from(SMART_SORT).immutableSortedCopy(moves);
            }
        };

        public static Comparator<Move> SMART_SORT = new Comparator<Move>() {
            @Override
            public int compare(final Move move1, final Move move2) {
                return ComparisonChain.start()
                        .compareTrueFirst(BoardUtils.isThreatenedBoardImmediate(move1.getBoard()), BoardUtils.isThreatenedBoardImmediate(move2.getBoard()))
                        .compareTrueFirst(move1.isAttack(), move2.isAttack())
                        .compare(move2.getMovedPiece().getPieceValue(), move1.getMovedPiece().getPieceValue())
                        .result();
            }
        };

        abstract Collection<Move> sort(Collection<Move> moves);
    }

    public AlphaTwo(final int searchDepth,
                    final int quiescenceFactor,
                    final int timeResources) {
        this.evaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
        this.quiescenceFactor = quiescenceFactor;
        this.timeResources = timeResources;
        this.moveSorter = MoveSorter.SORT;
        this.boardsEvaluated = 0;
        this.quiescenceCount = 0;
        this.cutOffsProduced = 0;
        this.zobrist = new ZobristHashing();
    }

    @Override
    public String toString() {
        return "AlphaTwo";
    }


    @Override
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();
        final Player currentPlayer = board.currentPlayer();
        final Alliance alliance = currentPlayer.getAlliance();
        Move bestMove = MoveFactory.getNullMove();
        int currentValue;
        System.out.println(board.currentPlayer() + " THINKING with depth = " + this.searchDepth);
        System.out.println("\tOrdered moves! : " + this.moveSorter.sort(board.currentPlayer().getLegalMoves()));
        MoveOrderingBuilder builder = new MoveOrderingBuilder();
        builder.setOrder(board.currentPlayer().getAlliance().isLight() ? Ordering.DESC : Ordering.ASC);
        for(final Move move : board.currentPlayer().getLegalMoves()) {
            builder.addMoveOrderingRecord(move, 0);
        }

        int currentDepth = 1;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        while (currentDepth <= this.searchDepth) {
            final long subTimeStart = System.currentTimeMillis();
            final List<MoveScoreRecord> records = builder.build();
            builder = new MoveOrderingBuilder();
            builder.setOrder(board.currentPlayer().getAlliance().isLight() ? Ordering.DESC : Ordering.ASC);
            for (MoveScoreRecord record : records) {
                if(System.currentTimeMillis() - startTime >= this.timeResources) {
                    break;
                }
                final Move move = record.getMove();
                final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
                this.quiescenceCount = 0;
                if (moveTransition.getMoveStatus().isDone()) {
                    currentValue = alliance.isLight() ?
                            min(moveTransition.getToBoard(), currentDepth - 1, highestSeenValue, lowestSeenValue) :
                            max(moveTransition.getToBoard(), currentDepth - 1, highestSeenValue, lowestSeenValue);
                    builder.addMoveOrderingRecord(move, currentValue);
                    if (alliance.isLight() && currentValue > highestSeenValue) {
                        highestSeenValue = currentValue;
                        bestMove = move;
                    } else if (alliance.isDark() && currentValue < lowestSeenValue) {
                        lowestSeenValue = currentValue;
                        bestMove = move;
                    }
                }
            }
            final long subTime = System.currentTimeMillis()- subTimeStart;
            System.out.println("\t" +toString()+ " bestMove = " +bestMove+ " Depth = " +currentDepth+ " took " +(subTime) + " ms");
            setChanged();
            notifyObservers(bestMove);
            currentDepth++;
        }
        this.executionTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s SELECTS %s [#boards evaluated = %d, time taken = %d ms, eval rate = %.1f cutoffCount = %d prune percent = %.2f\n", board.currentPlayer(),
                    bestMove, this.boardsEvaluated, this.executionTime, (1000 * ((double)this.boardsEvaluated/this.executionTime)), this.cutOffsProduced, 100 * ((double)this.cutOffsProduced/this.boardsEvaluated));
        return bestMove;
    }

    public int max(final Board board,
                   int depth,
                   int highest,
                   int lowest) {
        int olda = lowest;
        incrementNodeCount();
        updateDepth(depth);
        long state = zobrist.getHash(board);

        if(transposition.containsKey(state)) {
            if(transposition.get(state).depth >= depth) {
                int value = transposition.get(state).score;
                if(transposition.get(state).flag == 0) {
                    return value;
                } else if(transposition.get(state).flag == -1) {
                    lowest = Math.max(lowest, value);
                } else if(transposition.get(state).flag == 1) {
                    highest = Math.min(highest, value);
                }
                if(lowest >= highest) {
                    return value;
                }
            }
        }
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            this.boardsEvaluated++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        Move bestMove = null;
        for (final Move move : this.moveSorter.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final Board toBoard = moveTransition.getToBoard();
                currentHighest = Math.max(currentHighest, min(moveTransition.getToBoard(),
                        calculateQuiescenceDepth(toBoard, depth), currentHighest, lowest));
                bestMove = move;
                if (lowest <= currentHighest) {
                    this.cutOffsProduced++;
                    break;
                }
            }
        }
        int flag = 0;
        if(currentHighest <= olda) {
            flag = 1;
        } else if(currentHighest >= highest) {
            flag = -1;
        } else if(flag > lowest && flag < highest) {
            flag = 0;
        }
        transposition.put(state, new tableNode(bestMove, currentHighest, depth, flag));
        return currentHighest;
    }

    public int min(final Board board,
                   final int depth,
                   int highest,
                   int lowest) {
        int olda = lowest;
        incrementNodeCount();
        updateDepth(depth);
        long state = zobrist.getHash(board);

        if(transposition.containsKey(state)) {
            if(transposition.get(state).depth >= depth) {
                int value = transposition.get(state).score;
                if(transposition.get(state).flag == 0) {
                    return value;
                } else if(transposition.get(state).flag == -1) {
                    lowest = Math.max(lowest, value);
                } else if(transposition.get(state).flag == 1) {
                    highest = Math.min(highest, value);
                }
                if(lowest >= highest) {
                    return value;
                }
            }
        }
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            this.boardsEvaluated++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        Move bestMove = null;
        for (final Move move : this.moveSorter.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final Board toBoard = moveTransition.getToBoard();
                currentLowest = Math.min(currentLowest, max(moveTransition.getToBoard(),
                        calculateQuiescenceDepth(toBoard, depth), highest, currentLowest));
                bestMove = move;
                if (currentLowest <= highest) {
                    this.cutOffsProduced++;
                    break;
                }
            }
        }
        int flag = 0;
        if(currentLowest <= olda) {
            flag = 1;
        } else if(currentLowest >= highest) {
            flag = -1;
        } else if(flag > lowest && flag < highest) {
            flag = 0;
        }
        transposition.put(state, new tableNode(bestMove, currentLowest, depth, flag));
        return currentLowest;
    }

    private int calculateQuiescenceDepth(final Board toBoard,
                                         final int depth) {
        if(depth == 1 && this.quiescenceCount < MAX_QUIESCENCE) {
            int activityMeasure = 0;
            if (toBoard.currentPlayer().isInCheck()) {
                activityMeasure += 1;
            }
            for(final Move move: BoardUtils.lastNMoves(toBoard, 2)) {
                if(move.isAttack()) {
                    activityMeasure += 1;
                }
            }
            if(activityMeasure >= 2) {
                this.quiescenceCount++;
                return 2;
            }
        }
        return depth - 1;
    }

    private static long calculateTimeTaken(final long start, final long end) {
        final long timeTaken = (end - start) / 1000000;
        return timeTaken;
    }

    protected void updateDepth(int depth) {
        depthExplored = Math.max(depth, depthExplored);
    }

    protected void incrementNodeCount() {
        nodesExplored++;
    }


    private static class MoveScoreRecord implements Comparable<MoveScoreRecord> {
        final Move move;
        final int score;

        MoveScoreRecord(final Move move, final int score) {
            this.move = move;
            this.score = score;
        }

        Move getMove() {
            return this.move;
        }

        int getScore() {
            return this.score;
        }

        @Override
        public int compareTo(MoveScoreRecord o) {
            return Integer.compare(this.score, o.score);
        }

        @Override
        public String toString() {
            return this.move + " : " +this.score;
        }
    }

    enum Ordering {
        ASC {
            @Override
            List<MoveScoreRecord> order(final List<MoveScoreRecord> moveScoreRecords) {
                Collections.sort(moveScoreRecords, new Comparator<MoveScoreRecord>() {
                    @Override
                    public int compare(final MoveScoreRecord o1,
                                       final MoveScoreRecord o2) {
                        return Ints.compare(o1.getScore(), o2.getScore());
                    }
                });
                return moveScoreRecords;
            }
        },
        DESC {
            @Override
            List<MoveScoreRecord> order(final List<MoveScoreRecord> moveScoreRecords) {
                Collections.sort(moveScoreRecords, new Comparator<MoveScoreRecord>() {
                    @Override
                    public int compare(final MoveScoreRecord o1,
                                       final MoveScoreRecord o2) {
                        return Ints.compare(o2.getScore(), o1.getScore());
                    }
                });
                return moveScoreRecords;
            }
        };

        abstract List<MoveScoreRecord> order(final List<MoveScoreRecord> moveScoreRecords);
    }


    private static class MoveOrderingBuilder {
        List<MoveScoreRecord> moveScoreRecords;
        Ordering ordering;

        MoveOrderingBuilder() {
            this.moveScoreRecords = new ArrayList<>();
        }

        void addMoveOrderingRecord(final Move move,
                                   final int score) {
            this.moveScoreRecords.add(new MoveScoreRecord(move, score));
        }

        void setOrder(final Ordering order) {
            this.ordering = order;
        }

        List<MoveScoreRecord> build() {
            return this.ordering.order(moveScoreRecords);
        }
    }

}
