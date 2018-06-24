package com.chess.engine.player.ai;

import com.chess.engine.board.Move;
import com.chess.engine.board.Board;
import com.chess.engine.player.MoveTransition;

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
		final long startTime = System.currentTimeMillis();
		Move bestMove = null;
		int highestSeenValue = Integer.MIN_VALUE;
		int lowestSeenValue = Integer.MAX_VALUE;
		int currentValue = 0;
		System.out.println(board.getCurrentPlayer() + " THINKING with depth = " + depth);

		int numMoves = board.getCurrentPlayer().getLegalMoves().size();

		for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				if (board.getCurrentPlayer().getAlliance().isWhite()) {
					currentValue = minimumLayer(moveTransition.getTransitionBoard(), depth - 1);
					if (currentValue > highestSeenValue) {
						highestSeenValue = currentValue;
						bestMove = move;
					}
				}
				else {
					currentValue = maximumLayer(moveTransition.getTransitionBoard(), depth - 1);
					if (currentValue < lowestSeenValue) {
						lowestSeenValue = currentValue;
						bestMove = move;
					}
				}
			}
		}
		final long endTime = System.currentTimeMillis();
		final long calculationTimeSeconds = (endTime - startTime) / 1000;
		System.out.println("Took " + calculationTimeSeconds + " seconds to find the best move for " + (board.getCurrentPlayer().getAlliance().isWhite() ? "WHITE" : "BLACK") + ": " + bestMove.toString());
		return bestMove;
	}

	public static boolean isEndGameScenario(final Board board) {
		return board.getWhitePlayer().isInCheckMate() || board.getBlackPlayer().isInCheckMate();
	}

	public int minimumLayer(final Board board, final int depth) {
		if (depth <= 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		int lowestSeenValue = Integer.MAX_VALUE;
		for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				lowestSeenValue = Math.min(lowestSeenValue, maximumLayer(moveTransition.getTransitionBoard(), depth - 1));
			}
		}
		return lowestSeenValue;
	}

	public int maximumLayer(final Board board, final int depth) {
		if (depth <= 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		int highestSeenValue = Integer.MIN_VALUE;
		for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				highestSeenValue = Math.max(highestSeenValue, minimumLayer(moveTransition.getTransitionBoard(), depth - 1));
			}
		}
		return highestSeenValue;
	}

}
