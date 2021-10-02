package com.cannon.pgn;

import com.cannon.engine.board.Board;
import com.cannon.engine.board.BoardUtils;

public class FenUtilities {

    private FenUtilities() {
        throw new RuntimeException("Not instantiable");
    }

    public static Board createGameFromFEN(final String fenString) {
        return null;
    }

    public static String createFENFromGame(final Board board) {
        return calculateBoardText(board) + " " +
               calculateCurrentPlayerText(board) + " " +
               "0 1";
    }

    public static String calculateBoardText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = board.getTile(i).toString();
            builder.append(tileText);
        }
        builder.insert(10, "/");
        builder.insert(21, "/");
        builder.insert(32, "/");
        builder.insert(43, "/");
        builder.insert(54, "/");
        builder.insert(65, "/");
        builder.insert(76, "/");
        builder.insert(87, "/");
        builder.insert(98, "/");
        return builder.toString()
                .replaceAll("----------", "10")
                .replaceAll("---------", "9")
                .replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");

    }

    public static String calculateCurrentPlayerText(final Board board) {
        return board.currentPlayer().toString().substring(0, 1).toLowerCase();
    }
}
