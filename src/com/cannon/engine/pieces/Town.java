package com.cannon.engine.pieces;

import com.cannon.engine.Alliance;
import com.cannon.engine.board.Board;
import com.cannon.engine.board.BoardUtils;
import com.cannon.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.cannon.engine.board.Move.*;

public class Town extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public Town(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.TOWN, piecePosition, pieceAlliance, true);
    }

    public Town(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.TOWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if(this.isFirstMove()) {
                legalMoves.add(new TownMove(board, this, candidateDestinationCoordinate));
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Town movePiece(Move move) {
        if(move.getMovedPiece().getPieceAlliance().isDark()) {
            townPlacedDark = true;
        } else if(move.getMovedPiece().getPieceAlliance().isLight()) {
            townPlacedLight = true;
        }
        return new Town(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
    }

    @Override
    public String toString() {
        return PieceType.TOWN.toString();
    }
}
