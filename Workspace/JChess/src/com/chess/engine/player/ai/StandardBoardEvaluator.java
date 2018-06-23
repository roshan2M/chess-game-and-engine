package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

	@Override
	public int evaluate(final Board board, final int depth) {
		return scorePlayer(board, board.getWhitePlayer(), depth) -
				scorePlayer(board, board.getBlackPlayer(), depth);
	}

	private static int scorePlayer(final Board board, final Player player, final int depth) {
		int totalPiecesValue = 0;
		for (final Piece piece : player.getActivePieces()) {
			totalPiecesValue += piece.getPieceValue();
		}
		return totalPiecesValue;
	}

}
