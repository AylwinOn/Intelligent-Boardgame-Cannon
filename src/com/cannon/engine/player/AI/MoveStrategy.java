package com.cannon.engine.player.AI;

import com.cannon.engine.board.Board;
import com.cannon.engine.board.Move;

public interface MoveStrategy {

    Move execute(Board board);
}
