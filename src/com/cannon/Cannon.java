package com.cannon;

import com.cannon.engine.board.Board;
import com.cannon.gui.Table;

public class Cannon {

    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);
        Table table = new Table();
    }
}
