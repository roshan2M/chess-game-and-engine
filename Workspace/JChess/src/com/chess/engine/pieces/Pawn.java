package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Move.PawnMove;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {
	
	private final static int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16};

	public Pawn(final int piecePosition, final Alliance pieceAlliance) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, true);
	}
	
	public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}
	
	@Override
	public Pawn movePiece(Move move) {
		return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
			final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
			
			if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				continue;
			}
			
			if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				//TODO more work on pawn moves here (deal with promotions).
				legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
			}
			else if (currentCandidateOffset == 16 && this.isFirstMove() && ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) || (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * 8);
				
				if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
				}
			}
			else if (currentCandidateOffset == 7 && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.getPieceAlliance().isWhite()) || (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.getPieceAlliance().isBlack()))) {
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if (this.getPieceAlliance() != pieceOnCandidate.getPieceAlliance()) {
						//TODO Deal with attack and promote at the same time.
						legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
					}
				}
			}
			else if (currentCandidateOffset == 9 && !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.getPieceAlliance().isWhite()) || (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.getPieceAlliance().isBlack()))) {
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if (this.getPieceAlliance() != pieceOnCandidate.getPieceAlliance()) {
						//TODO Deal with attack and promote at the same time.
						legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
}
