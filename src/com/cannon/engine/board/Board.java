package com.cannon.engine.board;

import com.cannon.engine.Alliance;
import com.cannon.engine.pieces.Piece;
import com.cannon.engine.pieces.Soldier;
import com.cannon.engine.pieces.Town;
import com.cannon.engine.player.DarkPlayer;
import com.cannon.engine.player.LightPlayer;
import com.cannon.engine.player.Player;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;


public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> lightPieces;
    private final Collection<Piece> darkPieces;

    private final LightPlayer lightPlayer;
    private final DarkPlayer darkPlayer;
    private final Player currentPlayer;
    private final Soldier cannonSoldier;

    public Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.lightPieces = calculateActivePieces(this.gameBoard, Alliance.LIGHT);
        this.darkPieces = calculateActivePieces(this.gameBoard, Alliance.DARK);
        final Collection<Move> lightStandardLegalMoves = calculateLegalMoves(this.lightPieces);
        final Collection<Move> darkStandardLegalMoves = calculateLegalMoves(this.darkPieces);
        this.lightPlayer = new LightPlayer(this, lightStandardLegalMoves, darkStandardLegalMoves);
        this.darkPlayer = new DarkPlayer(this, lightStandardLegalMoves, darkStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.lightPlayer, this.darkPlayer);
        this.cannonSoldier = builder.cannonSoldier;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player lightPlayer() {
        return this.lightPlayer;
    }

    public Player darkPlayer() {
        return this.darkPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Collection<Piece> getDarkPieces() {
        return this.darkPieces;
    }

    public Collection<Piece> getLightPieces() {
        return this.lightPieces;
    }

    public Iterable<Piece> getAllPieces() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.lightPieces, this.darkPieces));
    }


    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile : gameBoard) {
            if(tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance) {
                    if(piece.isFirstMove()) {
                        activePieces.add(piece);
                    } else {
                        activePieces.add(piece);
                    }
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        builder.setPiece(new Town(0, Alliance.LIGHT, true));
        builder.setPiece(new Town(99, Alliance.DARK, true));

        builder.setPiece(new Soldier(Alliance.LIGHT, 11));
        builder.setPiece(new Soldier(Alliance.LIGHT, 13));
        builder.setPiece(new Soldier(Alliance.LIGHT, 15));
        builder.setPiece(new Soldier(Alliance.LIGHT, 17));
        builder.setPiece(new Soldier(Alliance.LIGHT, 19));
        builder.setPiece(new Soldier(Alliance.LIGHT, 21));
        builder.setPiece(new Soldier(Alliance.LIGHT, 23));
        builder.setPiece(new Soldier(Alliance.LIGHT, 25));
        builder.setPiece(new Soldier(Alliance.LIGHT, 27));
        builder.setPiece(new Soldier(Alliance.LIGHT, 29));
        builder.setPiece(new Soldier(Alliance.LIGHT, 31));
        builder.setPiece(new Soldier(Alliance.LIGHT, 33));
        builder.setPiece(new Soldier(Alliance.LIGHT, 35));
        builder.setPiece(new Soldier(Alliance.LIGHT, 37));
        builder.setPiece(new Soldier(Alliance.LIGHT, 39));

        builder.setPiece(new Soldier(Alliance.DARK, 60));
        builder.setPiece(new Soldier(Alliance.DARK, 62));
        builder.setPiece(new Soldier(Alliance.DARK, 64));
        builder.setPiece(new Soldier(Alliance.DARK, 66));
        builder.setPiece(new Soldier(Alliance.DARK, 68));
        builder.setPiece(new Soldier(Alliance.DARK, 70));
        builder.setPiece(new Soldier(Alliance.DARK, 72));
        builder.setPiece(new Soldier(Alliance.DARK, 74));
        builder.setPiece(new Soldier(Alliance.DARK, 76));
        builder.setPiece(new Soldier(Alliance.DARK, 78));
        builder.setPiece(new Soldier(Alliance.DARK, 80));
        builder.setPiece(new Soldier(Alliance.DARK, 82));
        builder.setPiece(new Soldier(Alliance.DARK, 84));
        builder.setPiece(new Soldier(Alliance.DARK, 86));
        builder.setPiece(new Soldier(Alliance.DARK, 88));

        builder.setMoveMaker(Alliance.DARK);

        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.lightPlayer.getLegalMoves(), this.darkPlayer.getLegalMoves()));
    }


    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Soldier cannonSoldier;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Builder setCannonAttackMove(final Soldier cannonSoldier) {
            this.cannonSoldier = cannonSoldier;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
