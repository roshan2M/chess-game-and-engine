package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

	// Heuristic values for the board evaluator.
	private static final int CHECK_BONUS = 50;
	private static final int CHECKMATE_BONUS = 10000;

	@Override
	public int evaluate(final Board board, final int depth) {
		return scorePlayer(board, board.getWhitePlayer(), depth) -
				scorePlayer(board, board.getBlackPlayer(), depth);
	}

	private static int scorePlayer(final Board board, final Player player, final int depth) {
		return pieceValue(player) + mobility(player) + check(player) + checkmate(player, depth);
	}

	private static int pieceValue(final Player player) {
		int totalPiecesValue = 0;
		for (final Piece piece : player.getActivePieces()) {
			totalPiecesValue += piece.getPieceValue();
		}
		return totalPiecesValue;
	}

	private static int depthBonus(int depth) {
		return depth == 0 ? 1 : 100 * depth;
	}

	private static int mobility(final Player player) {
		return player.getLegalMoves().size();
	}

	private static int check(final Player player) {
		return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
	}

	private static int checkmate(final Player player, final int depth) {
		return player.getOpponent().isInCheckMate() ? CHECKMATE_BONUS * depthBonus(depth) : 0;
	}

}
