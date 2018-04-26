package games.chess.board;

public class Position {
    public static final int QUEEN_SIDE_ROOK_COLUMN = 0;
    public static final int QUEEN_SIDE_KNIGHT_COLUMN = 1;
    public static final int QUEEN_SIDE_BISHOP_COLUMN = 2;
    public static final int QUEEN_COLUMN = 3;
    public static final int KING_COLUMN = 4;
    public static final int KING_SIDE_BISHOP_COLUMN = 5;
    public static final int KING_SIDE_KNIGHT_COLUMN = 6;
    public static final int KING_SIDE_ROOK_COLUMN = 7;

    public int row;
    public int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(String position) {
        this.column = getRankInt(position.charAt(0));
        this.row = Integer.valueOf(position.substring(1)) - 1;
    }

    @Override
    public String toString() {
        return String.format("%s%d", getRankChar(column), row + 1);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Position)) {
            return false;
        }
        Position otherPosition = (Position) object;
        return this.row == otherPosition.row
                && this.column == otherPosition.column;
    }

    @Override
    public int hashCode() {
        return (193 * (269 + row) + column);
    }


    private int getRankInt(char rank) {
        switch (rank) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
        }
        throw new RuntimeException("That is an invalid position");
    }

    private char getRankChar(int rank) {
        switch (rank) {
            case 0:
                return 'a';
            case 1:
                return 'b';
            case 2:
                return 'c';
            case 3:
                return 'd';
            case 4:
                return 'e';
            case 5:
                return 'f';
            case 6:
                return 'g';
            case 7:
                return 'h';
        }
        throw new RuntimeException("That is an invalid position");
    }
}