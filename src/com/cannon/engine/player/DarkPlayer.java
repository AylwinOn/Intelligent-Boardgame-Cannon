package com.cannon.engine.player;

import com.cannon.engine.Alliance;
import com.cannon.engine.board.Board;
import com.cannon.engine.board.Move;
import com.cannon.engine.pieces.Piece;

import java.util.Collection;

public class DarkPlayer extends Player {
    public DarkPlayer(final Board board,
                      final Collection<Move> lightStandardLegalMoves,
                      final Collection<Move> darkStandardLegalMoves) {
        super(board, darkStandardLegalMoves, lightStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getDarkPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.DARK;
    }

    @Override
    public Player getOpponent() {
        return this.board.lightPlayer();
    }
}
