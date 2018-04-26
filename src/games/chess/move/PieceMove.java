package games.chess.move;

import games.chess.board.Position;
import games.chess.piece.Piece;

public class PieceMove {
    public Position start;
    public Position end;
    public Piece piece;
    public PieceMove(Position start, Position end, Piece piece) {
        this.start = start;
        this.end = end;
        this.piece = piece;
    }

    @Override
    public String toString() {
        if (start == null) {
            return piece.color.name() + " " + piece.type.name() + " placed at " + end.toString();
        }

        if (end == null) {
            return piece.color.name() + " " + piece.type.name() + " removed from " + start.toString();
        }

        return piece.color.name() + " " + piece.type.toString() + " from " + start.toString() + " to " + end.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof PieceMove)) {
            return false;
        }
        PieceMove otherPieceMove = (PieceMove) object;
        return ( (this.start == null && otherPieceMove.start == null)
                || this.start.equals(otherPieceMove.start))
                && ((this.end == null && otherPieceMove.end == null)
                || this.end.equals(otherPieceMove.end))
                && this.piece.equals(otherPieceMove.piece);
    }

    @Override
    public int hashCode() {
        return (193 * ( 17 * (269 + start.hashCode()) + end.hashCode()) + piece.hashCode());
    }
}
