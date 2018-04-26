package games.chess.piece;

public enum PieceType {
    PAWN('p'),
    ROOK('R'),
    KNIGHT('N'),
    BISHOP('B'),
    QUEEN('Q'),
    KING('K');
	
	public char c;
	PieceType(char c){
		this.c = c;
	}
}