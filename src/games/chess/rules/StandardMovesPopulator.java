package games.chess.rules;

import java.util.ArrayList;
import java.util.List;

import games.chess.board.Board;
import games.chess.board.Position;
import games.chess.game.Player;
import games.chess.move.Move;
import games.chess.move.PieceMove;
import games.chess.piece.Color;
import games.chess.piece.Piece;
import games.chess.piece.PieceType;

import static games.chess.board.Position.*;
import static games.chess.piece.PieceType.*;
import static java.lang.Math.abs;

/**
 * Moves should be validated before they are populated
 */
public class StandardMovesPopulator {

    private final Position start;
    private final Position end;
    private final Board board;
    private final Player player;
    private List<PieceMove> piecesMoved;

    public StandardMovesPopulator(Move move) {
        this.start = move.getStartPosition();
        this.end = move.getEndPosition();
        this.board = move.getBoard();
        this.player = move.getPlayer();
    }

    public List<PieceMove> populateMove(boolean projection) {
        piecesMoved = new ArrayList<>();

        if (moveIsCastle()) {
            populateCastleMove();
        }

        else if (moveIsPromotion()) {
            populatePromotionMove(projection);
        }

        else if (moveIsEnPassant()) {
            populateEnPassantMave();
        }

        else {
            populateNormalMove();
        }

        return piecesMoved;
    }

    private boolean moveIsCastle() {
        return board.getPieceAtPosition(start).type == KING
                && abs(start.column - end.column) > 1;
    }

    private boolean moveIsPromotion() {
        return board.getPieceAtPosition(start).type == PAWN
                && end.row == board.getPieceAtPosition(start).color.opposite().pieceRow;
    }

    private boolean moveIsEnPassant() {
        return board.getPieceAtPosition(start).type == PAWN
                && start.column != end.column
                && board.getPieceAtPosition(end) == null;
    }

    private void populatePromotionMove(boolean projection) {
        piecesMoved.add(new PieceMove(start, null, board.getPieceAtPosition(start)));
        addTakenPiece();
        addPromotedPiece(projection);
    }

    private void populateCastleMove() {
        addPieceAtStartPosition();
        addRookToCastle();
    }

    private void populateEnPassantMave() {
        addPieceAtStartPosition();
        addPawnTakenInEnPassant();
    }

    private void populateNormalMove() {
        addPieceAtStartPosition();
        addTakenPiece();
    }

    private void addPieceAtStartPosition() {
        piecesMoved.add(new PieceMove(start, end, board.getPieceAtPosition(start)));
    }

    private void addTakenPiece() {
        if (board.getPieceAtPosition(end) != null) {
            piecesMoved.add(new PieceMove(end, null, board.getPieceAtPosition(end)));
        }
    }

    private void addPromotedPiece(boolean projection) {
        PieceType newPieceType;
        newPieceType = player.getTypeForPromotion(board, projection);
        piecesMoved.add(new PieceMove(null, end, new Piece(player.getColor(), newPieceType)));
    }

    private void addRookToCastle() {
        int rookStartColumn, rookEndColumn;
        if (end.column == KING_SIDE_KNIGHT_COLUMN) {
            rookStartColumn = KING_SIDE_ROOK_COLUMN;
            rookEndColumn = KING_SIDE_BISHOP_COLUMN;
        } else {
            rookStartColumn = QUEEN_SIDE_ROOK_COLUMN;
            rookEndColumn = QUEEN_COLUMN;
        }

        piecesMoved.add(new PieceMove(
                new Position(player.getColor().pieceRow, rookStartColumn),
                new Position(player.getColor().pieceRow, rookEndColumn),
                board.getPieceAtPosition(new Position(player.getColor().pieceRow, rookStartColumn))));
    }

    private void addPawnTakenInEnPassant() {
        int difference = player.getColor() == Color.WHITE? -1 : 1;
        Position pawnBeingTakenPosition = new Position(end.row + difference, end.column);

        piecesMoved.add(
                new PieceMove(
                        pawnBeingTakenPosition,
                        null,
                        board.getPieceAtPosition(pawnBeingTakenPosition)));
    }
}