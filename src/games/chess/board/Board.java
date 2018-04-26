package games.chess.board;

import java.util.ArrayList;
import java.util.List;

import games.chess.game.Player;
import games.chess.move.Move;
import games.chess.move.MovePositions;
import games.chess.piece.Color;
import games.chess.piece.Piece;
import games.chess.piece.PieceType;

public class Board {
	public static final String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
    public static final int BOARD_SIZE = 8;
    public final List<Move> moves = new ArrayList<Move>();
    private final Player playerOne;
    private final Player playerTwo;
    private Piece[][] squares = new Piece[BOARD_SIZE][BOARD_SIZE];

    public double getPieceColour(PieceType p){
    	if(p.equals(PieceType.PAWN)){
    		return 1.0/6.0;
    	}
    	if(p.equals(PieceType.ROOK)){
    		return 2.0/6.0;
    		
    	}
    	if(p.equals(PieceType.KNIGHT)){
    		return 3.0/6.0;
    		
    	}
    	if(p.equals(PieceType.BISHOP)){
    		return 4.0/6.0;
    		
    	}
    	if(p.equals(PieceType.QUEEN)){
    		return 5.0/6.0;
    		
    	}
    	else if(p.equals(PieceType.KING)){
    		return 1.0;
    	}
		return 0;
    }
    
    public double[] convertToNetworkInput(Color player){
    	double[] ret = new double[64];
    	int n = 0;
    	if(player.equals(Color.WHITE)){
    		for(int i = 0; i < 8; i++){
    			for(int j = 0; j < 8; j++){
    				Piece p = squares[i][j];
    				if(p == null){
    					ret[n] = 0.0;
    				}
    				else{
    					double val = this.getPieceColour(p.type);
    					if(!p.color.equals(player)){
    						val = val * -1;
    					}
    					ret[n] = val;
    				}
    				n++;
    			}
    		}
    	}
    	else{
    		for(int i = 7; i >= 0; i--){
    			for(int j = 7; j >= 0; j--){
    				Piece p = squares[i][j];
    				if(p == null){
    					ret[n] = 0.0;
    				}
    				else{
    					double val = this.getPieceColour(p.type);
    					if(!p.color.equals(player)){
    						val = val * -1;
    					}
    					ret[n] = val;
    				}
    				n++;
    			}
    		}
    	}
    			
    			
    	return ret;
    }
    
    public void printConvertToNetworkInput(Color p){
    	double[] in = this.convertToNetworkInput(p);
    	System.out.print("{");
    	for(int i = 0; i < 64; i++){
    		if(i != 0){
    			System.out.print(", ");
    		}
    		System.out.print(in[i]);
    	}
    	System.out.print("}\n");
    } 
    
    public Board(Player playerOne, Player playerTwo) {
        if (playerOne.getColor() == playerTwo.getColor()) {
            throw new RuntimeException("Both players on this board have the same color!");
        }
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public boolean isCheckmated(Color color) {
        return isInCheck(color) && getAllLegalMovesByColor(color).isEmpty();
    }

    public boolean isStalemated(Color color) {
        return !isInCheck(color) && getAllLegalMovesByColor(color).isEmpty();
    }

    public void clearSquare(Position position) {
        squares[position.row][position.column] = null;
    }

    public void putPieceAtPosition(Piece piece, Position position) {
        squares[position.row][position.column] = piece;
    }

    public Piece getPieceAtPosition(Position position) {
        return squares[position.row][position.column];
    }

    public boolean makeMove(Move move) {
        if (move.isLegal()) {
            move.makeMove(false);
            moves.add(move);
            return true;
        }
        return false;
    }

    public boolean isInCheck(Color color) {
        Position kingPosition = getPositionOfKing(color);
        for (Move move : getAllPossibleMovesByColor(color.opposite())) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    public Player getPlayerByColor(Color color) {
        return playerOne.getColor() == color ? playerOne : playerTwo;
    }

    public List<Move> getAllLegalMovesByColor(Color color) {
        List<Move> moves = new ArrayList<>();
        for (Move move : getAllPossibleMovesByColor(color)) {
            if (move.isLegal()) {
                moves.add(move);
            }
        }
        return moves;
    }

    private List<Move> getAllPossibleMovesByColor(Color color) {
        List<Move> moves = new ArrayList<>();
        for (MovePositions movePositions : getAllPossibleMovePositions()) {
            if (getPieceAtPosition(movePositions.start) != null && getPieceAtPosition(movePositions.start).color == color) {
                Move move = new Move(movePositions, this, getPlayerByColor(color));
                if (move.isPossible()) {
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    private List<MovePositions> getAllPossibleMovePositions() {
        List<MovePositions> listOfMovePositions = new ArrayList<>();
        for (Position start : getAllPositionsOnBoard()) {
            for (Position end : getAllPositionsOnBoard()) {
                listOfMovePositions.add(new MovePositions(start, end));
            }
        }
        return listOfMovePositions;
    }

    private List<Position> getAllPositionsOnBoard() {
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                positions.add(new Position(i,j));
            }
        }
        return positions;
    }

    public Position getPositionOfKing(Color color) {
        for (Position position : getAllPositionsOnBoard()) {
            if (getPieceAtPosition(position) != null
                    && getPieceAtPosition(position).type == PieceType.KING
                    && getPieceAtPosition(position).color == color) {
                return position;
            }
        }
        throw new RuntimeException("A king of color " + color.name() + " is no longer on the board.");
    }
    
    public void print(){
		System.out.print("  ");
		for(int i = 0; i < 8; i++){
			System.out.print("|   " + letters[i] + "   ");
		}
		System.out.print("|\n");
		
		String rowBuffer = "  ";
		for(int i = 0; i < 8; i++){
			rowBuffer = rowBuffer + "|-------";
		}
		rowBuffer = rowBuffer + "|\n";
		System.out.print(rowBuffer);
		
		for(int i = 7; i >= 0; i--){
			System.out.print((i + 1) + " ");
			for(int j = 0; j < 8; j++){
				Piece p = squares[i][j];
				if(p == null){
					System.out.print("|       ");
				}
				else{
					System.out.print("|  " + p.color.c + ':' + p.type.c + "  ");
				}
			}
			System.out.print("|\n");
			System.out.print(rowBuffer);
		}
    }
}