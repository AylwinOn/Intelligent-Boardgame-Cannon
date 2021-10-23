package com.cannon.engine.board;

import com.cannon.engine.player.MoveTransition;
import com.google.common.collect.ImmutableMap;

import java.util.*;

import static com.cannon.engine.board.Move.MoveFactory;

public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] THIRD_COLUMN = initColumn(2);
    public static final boolean[] FOURTH_COLUMN = initColumn(3);
    public static final boolean[] FIFTH_COLUMN = initColumn(4);
    public static final boolean[] SIXTH_COLUMN = initColumn(5);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);
    public static final boolean[] NINTH_COLUMN = initColumn(8);
    public static final boolean[] TENTH_COLUMN = initColumn(9);

    public static final boolean[] TENTH_RANK = initRow(0);
    public static final boolean[] FIRST_RANK = initRow(90);

    public static final String[] ALGEBREIC_NOTATION = initializeAlgebreicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    public static final int NUM_TILES = 100;
    public static final int NUM_TILES_PER_ROW = 10;


    public static String[] initializeAlgebreicNotation() {
        return new String[] {
                "A10", "B10", "C10", "D10", "E10", "F10", "G10", "H10", "I10", "J10",
                "A9", "B9", "C9", "D9", "E9", "F9", "G9", "H9", "I9", "J9",
                "A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8", "I8", "J8",
                "A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7", "I7", "J7",
                "A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6", "I6", "J6",
                "A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5", "I5", "J5",
                "A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4", "I4", "J4",
                "A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3", "I3", "J3",
                "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", "I2", "J2",
                "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1", "I1", "J1",
        };
    }

    public static boolean isThreatenedBoardImmediate(final Board board) {
        return board.lightPlayer().isInCheck() || board.darkPlayer().isInCheck();
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for(int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBREIC_NOTATION[i], i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    public static  int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBREIC_NOTATION[coordinate];
    }

    public static List<Move> lastNMoves(final Board board, int N) {
        final List<Move> moveHistory = new ArrayList<>();
        Move currentMove = board.getTransitionMove();
        int i = 0;
        while(currentMove != MoveFactory.getNullMove() && i < N) {
            moveHistory.add(currentMove);
            currentMove = currentMove.getBoard().getTransitionMove();
            i++;
        }
        return Collections.unmodifiableList(moveHistory);
    }

    public static boolean isEndGame(final Board board) {
        return board.currentPlayer().isInCheck();
    }
}
