package com.cannon.engine.player;

import com.cannon.engine.board.Board;
import com.cannon.engine.board.Move;
import com.cannon.engine.pieces.Piece;
import com.cannon.engine.pieces.Soldier;
import com.cannon.engine.pieces.Town;
import com.cannon.engine.AI.support.MoveStrategy;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final Town playerTown;
    protected final Soldier playerSoldier;
    protected final Collection<Move> legalMoves;
    private MoveStrategy strategy;
    protected final boolean isInCheck;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerTown = establishTown();
        this.playerSoldier = establishSoldier();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttackOnTile(this.playerTown.getPiecePosition(), opponentMoves).isEmpty();
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public void setMoveStrategy(final MoveStrategy strategy) {
        this.strategy = strategy;
    }

    public MoveStrategy getMoveStrategy() {
        return this.strategy;
    }

    private static Collection<Move> calculateAttackOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : moves) {
            if(piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private Town establishTown() {
        for(final Piece piece : getActivePieces()) {
            if(piece.getPieceType().isTown()) {
                return (Town) piece;
            }
        }
        throw new RuntimeException("CHECHMATE! " +this.getAlliance()+ " king could not be established!");
    }

    protected Soldier establishSoldier() {
        return null;
    }

    private boolean hasEscapeMoves() {
        return this.legalMoves.stream()
                .anyMatch(move -> makeMove(move)
                        .getMoveStatus().isDone());
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }


    public MoveTransition makeMove(final Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionedBoard = move.execute();
        return transitionedBoard.currentPlayer().getOpponent().isInCheck() ?
                new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK) :
                new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
    }

    public MoveTransition unMakeMove(final Move move) {
        return new MoveTransition(this.board, move.undo(), move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
