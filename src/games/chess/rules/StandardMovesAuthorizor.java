package games.chess.rules;

import static games.chess.board.Position.*;
import static games.chess.piece.PieceType.*;
import static java.lang.Math.*;

import games.chess.board.Board;
import games.chess.board.Position;
import games.chess.game.Player;
import games.chess.move.Move;
import games.chess.move.MovePositions;
import games.chess.piece.Color;
import games.chess.piece.Piece;

public class StandardMovesAuthorizor {

    private final Position start;
    private final Position end;
    private final Board board;
    private final Player player;

    public StandardMovesAuthorizor(Move move) {
        this.start = move.getStartPosition();
        this.end = move.getEndPosition();
        this.board = move.getBoard();
        this.player = move.getPlayer();
    }

    public boolean isMovePossible() {
        switch (board.getPieceAtPosition(start).type) {
            case KNIGHT:
                return isKnightMovePossible();
            case ROOK:
                return isRookMovePossible();
            case BISHOP:
                return isBishopMovePossible();
            case QUEEN:
                return isQueenMovePossible();
            case KING:
                return isKingMovePossible();
            case PAWN:
                return isPawnMovePossible();
        }
        throw new RuntimeException("The type of piece you are trying to move shouldn't exist on the board");
    }

    private boolean isRookMovePossible() {
        return !endOccupiedByPieceOfSameColor()
                && moveIsLinear()
                && !somethingIsInTheWay();
    }

    private boolean isBishopMovePossible() {
        return !endOccupiedByPieceOfSameColor()
                && moveIsDiagonal()
                && !somethingIsInTheWay();
    }

    private boolean isQueenMovePossible() {
        return !endOccupiedByPieceOfSameColor()
                && (moveIsDiagonal() || moveIsLinear())
                && !somethingIsInTheWay();
    }

    private boolean isKnightMovePossible() {
        return !endOccupiedByPieceOfSameColor()
                && moveIsLShaped();

    }

    private boolean isKingMovePossible() {
        if (endOccupiedByPieceOfSameColor()) {
            return false;
        }

        // can castle
        Piece king = board.getPieceAtPosition(start);
        if (end.column == KING_SIDE_KNIGHT_COLUMN && !king.hasMoved()
                && board.getPieceAtPosition(new Position(king.color.pieceRow, KING_SIDE_ROOK_COLUMN)) != null
                && !board.getPieceAtPosition(new Position(king.color.pieceRow, KING_SIDE_ROOK_COLUMN)).hasMoved()
                && new Move(new MovePositions(start, new Position(end.row, KING_SIDE_BISHOP_COLUMN)), board, player).isLegal()) {
            return true;
        } else if (end.column == QUEEN_SIDE_BISHOP_COLUMN && !king.hasMoved()
                && board.getPieceAtPosition(new Position(king.color.pieceRow, QUEEN_SIDE_ROOK_COLUMN)) != null
                && !board.getPieceAtPosition(new Position(king.color.pieceRow, QUEEN_SIDE_ROOK_COLUMN)).hasMoved()
                && board.getPieceAtPosition(new Position(king.color.pieceRow, QUEEN_SIDE_KNIGHT_COLUMN)) == null
                && new Move(new MovePositions(start, new Position(end.row, QUEEN_COLUMN)), board, player).isLegal()) {
            return true;
        }

        // can make normal move
        return abs(rowDistance()) <= 1 && abs(columnDistance()) <= 1;
    }

    private boolean isPawnMovePossible() {
        // must be linear or diagonal move
        if (!moveIsDiagonal() && !moveIsLinear()) {
            return false;
        }

        // can only move forward
        if (!moveIsForward()) {
            return false;
        }

        // cannot move onto another piece of same color
        if (endOccupiedByPieceOfSameColor()) {
            return false;
        }

        // can move two squares as first move
        if (pawnIsOnStartRow(start) && moveIsLinear() && !endOccupiedByPieceOfDifferentColor()
                && abs(start.row - end.row) == 2 && !somethingIsInTheWay()) {
            return true;
        }

        // can move one step forward
        if (abs(rowDistance()) == 1 && columnDistance() == 0 && !endOccupiedByPieceOfDifferentColor()) {
            return true;
        }

        if (abs(rowDistance()) == 1 && abs(columnDistance()) == 1) {
            // can make diagonal move of one to take
            if (endOccupiedByPieceOfDifferentColor()) {
                return true;
            }

            // can en passant
            Piece pawn = board.getPieceAtPosition(start);
            if (end.row == pawn.color.opposite().pawnRow + pawn.color.opposite().directionOfTravel()
                    && board.getPieceAtPosition(new Position(end.row - pawn.color.directionOfTravel(), end.column)) != null) {

                Piece toBeTaken = board.getPieceAtPosition(new Position(end.row - pawn.color.directionOfTravel(), end.column));
                if (toBeTaken.type == PAWN && !board.moves.isEmpty()
                        && board.moves.get(board.moves.size() - 1).contains(toBeTaken)) {
                    return true;
                }
            }

        }

        return false;
    }

    private int rowDistance() {
        return start.row - end.row;
    }

    private int columnDistance() {
        return start.column - end.column;
    }

    private boolean moveIsDiagonal() {
        return abs(rowDistance()) == abs(columnDistance());
    }

    private boolean moveIsLinear() {
        return rowDistance() == 0 || columnDistance() == 0;
    }

    private boolean moveIsLShaped() {
        return (abs(rowDistance()) == 1 && abs(columnDistance()) == 2)
                || (abs(rowDistance()) == 2 && abs(columnDistance()) == 1);
    }

    private boolean pawnIsOnStartRow(Position position) {
        return position.row == board.getPieceAtPosition(position).color.pawnRow;
    }

    private boolean endOccupiedByPieceOfSameColor() {
        Color color = board.getPieceAtPosition(start).color;
        return board.getPieceAtPosition(end) != null && board.getPieceAtPosition(end).color == color;
    }

    private boolean endOccupiedByPieceOfDifferentColor() {
        Color color = board.getPieceAtPosition(start).color;
        return board.getPieceAtPosition(end) != null && board.getPieceAtPosition(end).color != color;
    }

    private boolean moveIsForward() {
        if (start.row - end.row < 0 && board.getPieceAtPosition(start).color == Color.WHITE) {
            return true;
        } else if (start.row - end.row > 0 && board.getPieceAtPosition(start).color == Color.BLACK) {
            return true;
        }
        return false;
    }

    private boolean somethingIsInTheWay() {
        Position position = new Position(start.row, start.column);

        for (int i = 1; i < max(abs(rowDistance()), abs(columnDistance())); i++) {

            if (columnDistance() != 0) {
                if (columnDistance() < 0) {
                    position.column = start.column + i;
                } else {
                    position.column = start.column - i;
                }
            }

            if (rowDistance() != 0) {
                if (rowDistance() < 0) {
                    position.row = start.row + i;
                } else {
                    position.row = start.row - i;
                }
            }

            if (board.getPieceAtPosition(position) != null) {
                return true;
            }
        }

        return false;
    }
}