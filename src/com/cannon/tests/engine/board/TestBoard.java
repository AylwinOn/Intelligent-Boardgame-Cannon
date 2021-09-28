package com.cannon.tests.engine.board;

import com.cannon.engine.board.Board;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestBoard {

    @Test
    public void initialBoard() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.currentPlayer().isInCheck());
        assertEquals(board.currentPlayer(), board.lightPlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.darkPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        //assertEquals(new StandardBoardEvaluator().evaluate(board, 0), 0);
    }

}