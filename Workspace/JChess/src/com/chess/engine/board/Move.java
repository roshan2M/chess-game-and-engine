package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

/**
 * @author Roshan Munjal
 */
public abstract class Move {
	
	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCoordinate;
	protected final boolean isFirstMove;
	
	public static final Move NULL_MOVE = new NullMove();
	
	private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
		this.isFirstMove = movedPiece.isFirstMove();
	}
	
	private Move(final Board board, final int destinationCoordinate) {
		this.board = board;
		this.movedPiece = null;
		this.destinationCoordinate = destinationCoordinate;
		this.isFirstMove = false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.movedPiece.hashCode();
		result = prime * result + this.movedPiece.getPiecePosition();
		result = result + (isFirstMove ? 1 : 0);
		return result;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Move)) {
			return false;
		}
		final Move otherMove = (Move) other;
		return getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPiece() == otherMove.getMovedPiece() && getCurrentCoordinate() == otherMove.getCurrentCoordinate();
	}
	
	public Board execute() {
		final Board.Builder builder = new Builder();
		for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
			if (!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			System.out.println(piece);
			builder.setPiece(piece);
		}
		System.out.println("BREAK HERE 3");
		//TODO Move the moved piece.
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
		return builder.build();
	}
	
	public int getCurrentCoordinate() {
		return this.movedPiece.getPiecePosition();
	}

	public int getDestinationCoordinate() {
		return this.destinationCoordinate;
	}
	
	public Piece getMovedPiece() {
		return this.movedPiece;
	}
	
	public boolean isAttack() {
		return false;
	}
	
	public boolean isCastlingMove() {
		return false;
	}
	
	public Piece getAttackedPiece() {
		return null;
	}
	
	public static final class MajorMove extends Move {
		public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof MajorMove && super.equals(other);
		}
		
		@Override
		public String toString() {
			return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
	
	public static class AttackMove extends Move {
		final Piece attackedPiece;
		
		public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}
		
		@Override
		public int hashCode() {
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean equals(final Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof AttackMove)) {
				return false;
			}
			final AttackMove otherAttackMove = (AttackMove) other;
			return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}
		
		@Override
		public boolean isAttack() {
			return true;
		}
		
		@Override
		public Piece getAttackedPiece() {
			return this.attackedPiece;
		}
		
		@Override
		public String toString() {
			return movedPiece.getPieceType().toString() + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
	
	public static final class PawnMove extends Move {
		public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
	
	public static class PawnAttackMove extends AttackMove {
		public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
	
	public static final class PawnEnPassantAttack extends PawnAttackMove {
		public PawnEnPassantAttack(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}
	}
	
	public static final class PawnJump extends Move {
		public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
		
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}
	
	public static abstract class CastleMove extends Move {
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;
		
		
		public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate);
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookDestination = castleRookDestination;
		}
		
		public Rook getCastleRook() {
			return this.castleRook;
		}
		
		@Override
		public boolean isCastlingMove() {
			return true;
		}
		
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece) && !this.movedPiece.equals(castleRook)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			//TODO Look into the first move on normal pieces.
			builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
			builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
	}
	
	public static final class KingSideCastleMove extends CastleMove {
		public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}
		
		@Override
		public String toString() {
			return "O-O";
		}
	}
	
	public static final class QueenSideCastleMove extends CastleMove {
		public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}
		
		@Override
		public String toString() {
			return "O-O-O";
		}
	}
	
	public static final class NullMove extends Move {
		public NullMove() {
			super(null, 65);
		}
		
		@Override
		public Board execute() {
			throw new RuntimeException("Cannot execute the null move.");
		}
		
		@Override
		public int getCurrentCoordinate() {
			return -1;
		}
	}
	
	public static class MoveFactory {
		private MoveFactory() {
			throw new RuntimeException("Not Instantiable!");
		}
		
		public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
			for (final Move move : board.getAllLegalMoves()) {
				if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
					return move;
				}
			}
			System.out.println("FOUND A NULL MOVE");
			return NULL_MOVE;
		}
	}
	
}
