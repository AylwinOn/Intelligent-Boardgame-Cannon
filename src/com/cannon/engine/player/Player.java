package com.cannon.engine.player;

import com.cannon.engine.Alliance;
import com.cannon.engine.board.Board;
import com.cannon.engine.board.Move;
import com.cannon.engine.pieces.Piece;
import com.cannon.engine.pieces.Soldier;
import com.cannon.engine.pieces.Town;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final Town playerTown;
    protected final Soldier playerSoldier;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    //private final boolean isUnderAttack;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerTown = establishTown();
        this.playerSoldier = establishSoldier();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttackOnTile(this.playerTown.getPiecePosition(), opponentMoves).isEmpty();
        //this.isUnderAttack = !Player.calculateAttackOnTile(this.playerSoldier.getPiecePosition(), opponentMoves).isEmpty();
    }

    public Town getPlayerTown() {
        return this.playerTown;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
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
        throw new RuntimeException("Not a valid board");
    }

    protected Soldier establishSoldier() {
        return null;
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    //TODO: Implement methods below
    public boolean isUnderAttack() {
        return false;
    }

    public boolean hasEmptyTiles() {
        return false;
    }

    public boolean isFormedCannon() {
        return false;
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }


    public MoveTransition makeMove(final Move move) {
        if(!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> townAttacks = Player.calculateAttackOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerTown().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());
        if(!townAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
