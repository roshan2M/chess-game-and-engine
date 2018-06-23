package com.chess.engine.player.ai;

import com.chess.engine.board.Move;
import com.chess.engine.board.Board;

public interface MoveStrategy {

	Move execute(Board board, int depth);

}
