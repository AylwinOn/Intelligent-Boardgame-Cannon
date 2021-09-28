package com.cannon.engine.player.AI;

import com.cannon.engine.board.Board;
import com.cannon.engine.pieces.Piece;
import com.cannon.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final StandardBoardEvaluator INSTANCE = new StandardBoardEvaluator();

    public static StandardBoardEvaluator get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.lightPlayer(), depth) -
               scorePlayer(board, board.darkPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return pieceValue(player) + mobility(player) + check(player, depth);
    }

    private static int check(final Player player, int depth) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int mobility(final Player player) {
        return player.getLegalMoves().size();
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for(final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
