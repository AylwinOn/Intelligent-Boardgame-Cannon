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

public class Soldier extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {10, 9, 11, 1, -1, -20, -18, -22};
    private final static int[] SURROUNDING_MOVE_COORDINATES = {10, 9, 11, 1, -1};
    private final static int[] NEIGHBOUR_COORDINATES = {10, 9, 11, 1};

    public Soldier(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.SOLDIER, piecePosition, pieceAlliance, true);
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        boolean formedCannon = false;
        boolean underAttack = false;

        for(final int currentCandidateNeighbour : NEIGHBOUR_COORDINATES) {
            int candidateNeighbourCoordinateUP = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateNeighbour);
            int candidateNeighbourCoordinateUP2 = candidateNeighbourCoordinateUP + (this.pieceAlliance.getDirection() * currentCandidateNeighbour);
            int candidateNeighbourCoordinateUP3 = candidateNeighbourCoordinateUP2 + (this.pieceAlliance.getDirection() * currentCandidateNeighbour);
            int candidateNeighbourCoordinateDOWN = this.piecePosition - (this.pieceAlliance.getDirection() * currentCandidateNeighbour);
            int candidateNeighbourCoordinateDOWN2 = candidateNeighbourCoordinateDOWN - (this.pieceAlliance.getDirection() * currentCandidateNeighbour);
            int candidateNeighbourCoordinateDOWN3 = candidateNeighbourCoordinateDOWN2 - (this.pieceAlliance.getDirection() * currentCandidateNeighbour);

            if(candidateNeighbourCoordinateUP > 0 && candidateNeighbourCoordinateUP < 100 && candidateNeighbourCoordinateDOWN > 0 && candidateNeighbourCoordinateDOWN < 100
                    && board.getTile(candidateNeighbourCoordinateUP).isTileOccupied() && board.getTile(candidateNeighbourCoordinateDOWN).isTileOccupied()) {
                final Piece pieceOnCandidateUP = board.getTile(candidateNeighbourCoordinateUP).getPiece();
                final Piece pieceOnCandidateDOWN = board.getTile(candidateNeighbourCoordinateDOWN).getPiece();
                if(this.pieceAlliance == pieceOnCandidateUP.getPieceAlliance() && this.pieceAlliance == pieceOnCandidateDOWN.getPieceAlliance()
                        && this.pieceType == pieceOnCandidateUP.getPieceType() && this.pieceType == pieceOnCandidateDOWN.getPieceType()) {
                    formedCannon = true;

                    int intermediateCannonAttackUP = 2 * currentCandidateNeighbour;
                    int intermediateCannonAttackCoordinateUP = this.piecePosition + (this.pieceAlliance.getDirection() * intermediateCannonAttackUP);
                    if(intermediateCannonAttackCoordinateUP > 0 && intermediateCannonAttackCoordinateUP < 100 && !board.getTile(intermediateCannonAttackCoordinateUP).isTileOccupied()) {
                        int candidateCannonAttackUP = 3 * currentCandidateNeighbour;
                        int candidateCannonAttackCoordinateUP = this.piecePosition + (this.pieceAlliance.getDirection() * candidateCannonAttackUP);
                        if(candidateCannonAttackCoordinateUP > 0 && candidateCannonAttackCoordinateUP < 100 && board.getTile(candidateCannonAttackCoordinateUP).isTileOccupied()) {
                            final Piece pieceOnCandidateCannonUP = board.getTile(candidateCannonAttackCoordinateUP).getPiece();
                            if(this.pieceAlliance != pieceOnCandidateCannonUP.getPieceAlliance()) {
                                if(!((BoardUtils.NINTH_COLUMN[this.piecePosition] && BoardUtils.SECOND_COLUMN[candidateCannonAttackCoordinateUP]) ||
                                     (BoardUtils.SECOND_COLUMN[this.piecePosition] && BoardUtils.NINTH_COLUMN[candidateCannonAttackCoordinateUP]) ||
                                     (BoardUtils.EIGHT_COLUMN[this.piecePosition] && BoardUtils.FIRST_COLUMN[candidateCannonAttackCoordinateUP]) ||
                                     (BoardUtils.FIRST_COLUMN[this.piecePosition] && BoardUtils.EIGHT_COLUMN[candidateCannonAttackCoordinateUP]))){
                                    legalMoves.add(new CannonAttackMove(board, this, candidateCannonAttackCoordinateUP, pieceOnCandidateCannonUP));
                                }
                            }
                        } else if(candidateCannonAttackCoordinateUP > 0 && candidateCannonAttackCoordinateUP < 100 && !board.getTile(candidateCannonAttackCoordinateUP).isTileOccupied()) {
                            int candidateCannonAttackUP2 = 4 * currentCandidateNeighbour;
                            int candidateCannonAttackCoordinateUP2 = this.piecePosition + (this.pieceAlliance.getDirection() * candidateCannonAttackUP2);
                            if(candidateCannonAttackCoordinateUP2 > 0 && candidateCannonAttackCoordinateUP2 < 100 && board.getTile(candidateCannonAttackCoordinateUP2).isTileOccupied()) {
                                final Piece pieceOnCandidateCannonUP2 = board.getTile(candidateCannonAttackCoordinateUP2).getPiece();
                                if(this.pieceAlliance != pieceOnCandidateCannonUP2.getPieceAlliance()) {
                                    if(!((BoardUtils.NINTH_COLUMN[this.piecePosition] && BoardUtils.THIRD_COLUMN[candidateCannonAttackCoordinateUP2]) ||
                                         (BoardUtils.EIGHT_COLUMN[this.piecePosition] && BoardUtils.SECOND_COLUMN[candidateCannonAttackCoordinateUP2]) ||
                                         (BoardUtils.SEVENTH_COLUMN[this.piecePosition] && BoardUtils.FIRST_COLUMN[candidateCannonAttackCoordinateUP2]) ||
                                         (BoardUtils.THIRD_COLUMN[this.piecePosition] && BoardUtils.NINTH_COLUMN[candidateCannonAttackCoordinateUP2]) ||
                                         (BoardUtils.SECOND_COLUMN[this.piecePosition] && BoardUtils.EIGHT_COLUMN[candidateCannonAttackCoordinateUP2]) ||
                                         (BoardUtils.FIRST_COLUMN[this.piecePosition] && BoardUtils.SEVENTH_COLUMN[candidateCannonAttackCoordinateUP2]) )) {
                                        legalMoves.add(new CannonAttackMove(board, this, candidateCannonAttackCoordinateUP2, pieceOnCandidateCannonUP2));
                                    }
                                }
                            }
                        }
                    }
                    int intermediateCannonAttackDOWN = -2 * currentCandidateNeighbour;
                    int intermediateCannonAttackCoordinateDOWN = this.piecePosition + (this.pieceAlliance.getDirection() * intermediateCannonAttackDOWN);
                    if(intermediateCannonAttackCoordinateDOWN > 0 && intermediateCannonAttackCoordinateDOWN < 100 && !board.getTile(intermediateCannonAttackCoordinateDOWN).isTileOccupied()) {
                        int candidateCannonAttackDOWN = -3 * currentCandidateNeighbour;
                        int candidateCannonAttackCoordinateDOWN = this.piecePosition + (this.pieceAlliance.getDirection() * candidateCannonAttackDOWN);
                        if(candidateCannonAttackCoordinateDOWN > 0 && candidateCannonAttackCoordinateDOWN < 100 && board.getTile(candidateCannonAttackCoordinateDOWN).isTileOccupied()) {
                            final Piece pieceOnCandidateCannonDOWN = board.getTile(candidateCannonAttackCoordinateDOWN).getPiece();
                            if(this.pieceAlliance != pieceOnCandidateCannonDOWN.getPieceAlliance()) {
                                if(!((BoardUtils.NINTH_COLUMN[this.piecePosition] && BoardUtils.SECOND_COLUMN[candidateCannonAttackCoordinateDOWN]) ||
                                     (BoardUtils.SECOND_COLUMN[this.piecePosition] && BoardUtils.NINTH_COLUMN[candidateCannonAttackCoordinateDOWN]) ||
                                     (BoardUtils.EIGHT_COLUMN[this.piecePosition] && BoardUtils.FIRST_COLUMN[candidateCannonAttackCoordinateDOWN]) ||
                                     (BoardUtils.FIRST_COLUMN[this.piecePosition] && BoardUtils.EIGHT_COLUMN[candidateCannonAttackCoordinateDOWN]))) {
                                    legalMoves.add(new CannonAttackMove(board, this, candidateCannonAttackCoordinateDOWN, pieceOnCandidateCannonDOWN));
                                }
                            }
                        } else if(candidateCannonAttackCoordinateDOWN > 0 && candidateCannonAttackCoordinateDOWN < 100 && !board.getTile(candidateCannonAttackCoordinateDOWN).isTileOccupied()) {
                            int candidateCannonAttackDOWN2 = -4 * currentCandidateNeighbour;
                            int candidateCannonAttackCoordinateDOWN2 = this.piecePosition + (this.pieceAlliance.getDirection() * candidateCannonAttackDOWN2);
                            if(candidateCannonAttackCoordinateDOWN2 > 0 && candidateCannonAttackCoordinateDOWN2 < 100 && board.getTile(candidateCannonAttackCoordinateDOWN2).isTileOccupied()) {
                                final Piece pieceOnCandidateCannonDOWN2 = board.getTile(candidateCannonAttackCoordinateDOWN2).getPiece();
                                if(this.pieceAlliance != pieceOnCandidateCannonDOWN2.getPieceAlliance()) {
                                    if(!((BoardUtils.NINTH_COLUMN[this.piecePosition] && BoardUtils.THIRD_COLUMN[candidateCannonAttackCoordinateDOWN2]) ||
                                         (BoardUtils.EIGHT_COLUMN[this.piecePosition] && BoardUtils.SECOND_COLUMN[candidateCannonAttackCoordinateDOWN2]) ||
                                         (BoardUtils.SEVENTH_COLUMN[this.piecePosition] && BoardUtils.FIRST_COLUMN[candidateCannonAttackCoordinateDOWN2]) ||
                                         (BoardUtils.THIRD_COLUMN[this.piecePosition] && BoardUtils.NINTH_COLUMN[candidateCannonAttackCoordinateDOWN2]) ||
                                         (BoardUtils.SECOND_COLUMN[this.piecePosition] && BoardUtils.EIGHT_COLUMN[candidateCannonAttackCoordinateDOWN2]) ||
                                         (BoardUtils.FIRST_COLUMN[this.piecePosition] && BoardUtils.SEVENTH_COLUMN[candidateCannonAttackCoordinateDOWN2]) )) {
                                        legalMoves.add(new CannonAttackMove(board, this, candidateCannonAttackCoordinateDOWN2, pieceOnCandidateCannonDOWN2));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(candidateNeighbourCoordinateUP > 0 && candidateNeighbourCoordinateUP < 100 && candidateNeighbourCoordinateUP2 > 0 && candidateNeighbourCoordinateUP2 < 100
                    && board.getTile(candidateNeighbourCoordinateUP).isTileOccupied() && board.getTile(candidateNeighbourCoordinateUP2).isTileOccupied()) {
                final Piece pieceOnCandidateUP = board.getTile(candidateNeighbourCoordinateUP).getPiece();
                final Piece pieceOnCandidateUP2 = board.getTile(candidateNeighbourCoordinateUP2).getPiece();
                if(this.pieceAlliance == pieceOnCandidateUP.getPieceAlliance() && this.pieceAlliance == pieceOnCandidateUP2.getPieceAlliance()
                        && this.pieceType == pieceOnCandidateUP.getPieceType() && this.pieceType == pieceOnCandidateUP2.getPieceType()) {
                    if(candidateNeighbourCoordinateUP3 > 0 && candidateNeighbourCoordinateUP3 < 100 && !board.getTile(candidateNeighbourCoordinateUP3).isTileOccupied()) {
                        legalMoves.add(new CannonSlideMove(board, this, candidateNeighbourCoordinateUP3));
                    }
                }
            }
            if(candidateNeighbourCoordinateDOWN > 0 && candidateNeighbourCoordinateDOWN < 100 && candidateNeighbourCoordinateDOWN2 > 0 && candidateNeighbourCoordinateDOWN2 < 100
                    && board.getTile(candidateNeighbourCoordinateDOWN).isTileOccupied() && board.getTile(candidateNeighbourCoordinateDOWN2).isTileOccupied()) {
                final Piece pieceOnCandidateDOWN = board.getTile(candidateNeighbourCoordinateDOWN).getPiece();
                final Piece pieceOnCandidateDOWN2 = board.getTile(candidateNeighbourCoordinateDOWN2).getPiece();
                if(this.pieceAlliance == pieceOnCandidateDOWN.getPieceAlliance() && this.pieceAlliance == pieceOnCandidateDOWN2.getPieceAlliance()
                        && this.pieceType == pieceOnCandidateDOWN.getPieceType() && this.pieceType == pieceOnCandidateDOWN2.getPieceType()) {
                    if(candidateNeighbourCoordinateDOWN3 > 0 && candidateNeighbourCoordinateDOWN3 < 100 && !board.getTile(candidateNeighbourCoordinateDOWN3).isTileOccupied()) {
                        legalMoves.add(new CannonSlideMove(board, this, candidateNeighbourCoordinateDOWN3));
                    }
                }
            }
        }


        for(final int currentCandidateSurrounding : SURROUNDING_MOVE_COORDINATES) {
            int candidateUnderAttackCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateSurrounding);
            if(candidateUnderAttackCoordinate > 0 && candidateUnderAttackCoordinate < 100 && board.getTile(candidateUnderAttackCoordinate).isTileOccupied()) {
                final Piece pieceOnCandidate = board.getTile(candidateUnderAttackCoordinate).getPiece();
                if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance() && this.pieceType == pieceOnCandidate.getPieceType()) {
                    underAttack = true;
                }
            }
        }


        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
            int candidateIntermediateCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * (currentCandidateOffset / 2));
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            if(currentCandidateOffset == 10) {
                if(!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new SoldierMove(board, this, candidateDestinationCoordinate));
                } else if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(new SoldierAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            } else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.TENTH_COLUMN[this.piecePosition] && this.pieceAlliance.isDark()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isLight()))) {
                if (!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new SoldierMove(board, this, candidateDestinationCoordinate));
                } else if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(new SoldierAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            } else if(currentCandidateOffset == 11 &&
                        !((BoardUtils.TENTH_COLUMN[this.piecePosition] && this.pieceAlliance.isLight()) ||
                          (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isDark()))) {
                if (!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new SoldierMove(board, this, candidateDestinationCoordinate));
                } else if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(new SoldierAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            } else if (currentCandidateOffset == 1 &&
                    !((BoardUtils.TENTH_COLUMN[this.piecePosition] && this.pieceAlliance.isLight()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isDark()))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(new SoldierAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            } else if (currentCandidateOffset == -1 &&
                    !((BoardUtils.TENTH_COLUMN[this.piecePosition] && this.pieceAlliance.isDark()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isLight()))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(new SoldierAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            } else if (currentCandidateOffset == -20 && underAttack &&
                    !board.getTile(candidateIntermediateCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                legalMoves.add(new SoldierRetreatMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == -18 && underAttack &&
                    !board.getTile(candidateIntermediateCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied() &&
                    !((BoardUtils.TENTH_COLUMN[this.piecePosition] && this.pieceAlliance.isLight()) ||
                      (BoardUtils.NINTH_COLUMN[this.piecePosition] && this.pieceAlliance.isLight()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isDark()) ||
                      (BoardUtils.SECOND_COLUMN[this.piecePosition] && this.pieceAlliance.isDark()))) {
                legalMoves.add(new SoldierRetreatMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == -22 && underAttack &&
                    !board.getTile(candidateIntermediateCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied() &&
                    !((BoardUtils.TENTH_COLUMN[this.piecePosition] && this.pieceAlliance.isDark()) ||
                      (BoardUtils.NINTH_COLUMN[this.piecePosition] && this.pieceAlliance.isDark()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isLight()) ||
                      (BoardUtils.SECOND_COLUMN[this.piecePosition] && this.pieceAlliance.isLight()))) {
                legalMoves.add(new SoldierRetreatMove(board, this, candidateDestinationCoordinate));
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Soldier movePiece(final Move move) {
        return new Soldier(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.SOLDIER.toString();
    }

}
