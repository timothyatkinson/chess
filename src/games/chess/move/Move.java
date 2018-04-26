package games.chess.move;

import java.util.ArrayList;
import java.util.List;

import games.chess.board.Board;
import games.chess.board.Position;
import games.chess.game.Player;
import games.chess.piece.Piece;
import games.chess.rules.IChessRules;
import games.chess.rules.StandardChessRules;

public class Move {
    private final Player player;
    private final Board board;
    private final IChessRules chessRules;
    private MovePositions movePositions;
    private List<PieceMove> piecesMoved = new ArrayList<>();

    public Move(MovePositions movePositions, Board board, Player player) {
        this.movePositions = movePositions;
        this.board = board;
        this.player = player;
        chessRules = new StandardChessRules(this);
    }

    public Position getStartPosition() {
        return movePositions.start;
    }

    public Position getEndPosition() {
        return movePositions.end;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public void makeMove(boolean projection) {
        piecesMoved = chessRules.populatePiecesMoved(projection);
        movePieces();
        for (PieceMove pieceMove : piecesMoved) {
            pieceMove.piece.moves.add(this);
        }
    }

    public void undoMove() {
        if (piecesMoved.isEmpty()) {
            throw new RuntimeException("You have to make a move before you can undo that move.");
        }
        invertPieceMoves();
        movePieces();
        invertPieceMoves();
        for (PieceMove pieceMove : piecesMoved) {
            pieceMove.piece.moves.remove(this);
        }
    }

    public boolean isLegal() {
    	try{
	        if (!isPossible()) {
	            return false;
	        }
	
	        makeMove(true);
	        boolean isLegal = !board.isInCheck(player.getColor());
	        undoMove();
	
	        return isLegal;
    	}
    	catch(Exception e){
    		return false;
    	}
    }

    public boolean isPossible() {
        return board.getPieceAtPosition(movePositions.start) != null
                && !movePositions.areTheSame()
                && chessRules.isMovePossible();
    }

    public boolean contains(Piece piece) {
        for (PieceMove pieceMove : piecesMoved) {
            if (pieceMove.piece == piece) {
                return true;
            }
        }
        return false;
    }

    public boolean isCheck() {
        makeMove(true);
        boolean isCheck = board.isInCheck(player.getColor().opposite());
        undoMove();

        return isCheck;
    }

    public boolean isCheckMate() {
        makeMove(true);
        boolean isCheckMate = board.isCheckmated(player.getColor().opposite());
        undoMove();

        return isCheckMate;
    }

    public boolean isATake() {
        boolean isATake = false;
        for (PieceMove pieceMove : piecesMoved) {
            if (pieceMove.piece.color != player.getColor()) {
                isATake = true;
            }
        }
        return isATake;
    }

    private void invertPieceMoves() {
        for (PieceMove pieceMove : piecesMoved) {
            Position temp = pieceMove.start;
            pieceMove.start = pieceMove.end;
            pieceMove.end = temp;
        }
    }

    private void movePieces() {
        // take off pieces first
        for (PieceMove manipulation : piecesMoved) {
            if (manipulation.end == null) {
                board.clearSquare(manipulation.start);
            }
        }

        // if all pieces stay on board, order does not matter
        for (PieceMove manipulation : piecesMoved) {
            if (manipulation.start != null && manipulation.end != null) {
                board.clearSquare(manipulation.start);
                board.putPieceAtPosition(manipulation.piece, manipulation.end);
            }
        }

        // put new pieces on last
        for (PieceMove manipulation : piecesMoved) {
            if (manipulation.start == null) {
                board.putPieceAtPosition(manipulation.piece, manipulation.end);
            }
        }
    }

    @Override
    public String toString() {
        String summary = "";
        for (PieceMove pieceMove : piecesMoved) {
            summary = summary + pieceMove.toString() + " ";
        }
        return summary;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Move)) {
            return false;
        }
        Move otherMove = (Move) object;
        return this.movePositions.equals(otherMove.movePositions)
                && this.board == otherMove.board;
    }

    @Override
    public int hashCode() {
        return (193 * (269 + movePositions.hashCode())) + board.hashCode();
    }
}