package games.chess.move;

import games.chess.board.Position;

public class MovePositions {
    public Position start;
    public Position end;
    public MovePositions(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public boolean areTheSame() {
        return start.equals(end);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof MovePositions)) {
            return false;
        }
        MovePositions otherMovePositions = (MovePositions) object;
        return ( (this.start == null && otherMovePositions.start == null)
                || this.start.equals(otherMovePositions.start))
                && ((this.end == null && otherMovePositions.end == null)
                || this.end.equals(otherMovePositions.end));
    }

    @Override
    public int hashCode() {
        return (193 * (269 + start.hashCode()) + end.hashCode());
    }
}
