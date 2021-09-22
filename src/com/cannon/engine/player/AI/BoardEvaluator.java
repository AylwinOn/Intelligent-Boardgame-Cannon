package com.cannon.engine.player.AI;

import com.cannon.engine.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);
}
