package com.cannon.engine.player;

import com.cannon.engine.Alliance;
import com.cannon.engine.board.Board;
import com.cannon.engine.board.Move;
import com.cannon.engine.pieces.Piece;

import java.util.Collection;

public class LightPlayer extends Player {
    public LightPlayer(final Board board,
                       final Collection<Move> lightStandardLegalMoves,
                       final Collection<Move> darkStandardLegalMoves) {
        super(board, lightStandardLegalMoves, darkStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getLightPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.LIGHT;
    }

    @Override
    public Player getOpponent() {
        return this.board.darkPlayer();
    }
}
