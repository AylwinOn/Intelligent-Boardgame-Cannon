package com.cannon.engine.AI.support;

import com.cannon.engine.board.Board;
import com.cannon.engine.board.Move;

public interface MoveStrategy {

    Move execute(Board board);
}
