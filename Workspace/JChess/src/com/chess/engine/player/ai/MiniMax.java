package com.chess.engine.player.ai;

import com.chess.engine.board.Move;
import com.chess.engine.board.Board;

public class MiniMax implements MoveStrategy {

	private final BoardEvaluator boardEvaluator;

	public MiniMax(BoardEvaluator boardEvaluator) {
		this.boardEvaluator = boardEvaluator;
	}

	@Override
	public String toString() {
		return "MiniMax";
	}

	@Override
	public Move execute(Board board, int depth) {
		return null;
	}

}
