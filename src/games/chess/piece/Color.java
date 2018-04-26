package games.chess.piece;

public enum Color {
    WHITE(0,1, 'w'),
    BLACK(7,6, 'b');

    public int pieceRow;
    public int pawnRow;
    public char c;
    Color (int pieceRow, int pawnRow, char c) {
        this.pieceRow = pieceRow;
        this.pawnRow = pawnRow;
        this.c = c;
    }

    public Color opposite() {
        if (this == WHITE) {
            return BLACK;
        }
        return WHITE;
    }

    public int directionOfTravel() {
        return pawnRow - pieceRow;
    }
}