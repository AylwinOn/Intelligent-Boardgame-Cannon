package com.cannon.engine.AI.support;

import com.cannon.engine.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);
}
