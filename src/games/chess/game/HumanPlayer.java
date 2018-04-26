package games.chess.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import games.chess.board.Board;
import games.chess.board.Position;
import games.chess.move.Move;
import games.chess.move.MovePositions;
import games.chess.piece.Color;
import games.chess.piece.PieceType;

public class HumanPlayer extends Player {

    private static final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public HumanPlayer(Color color) {
        super(color);
    }

    @Override
    public PieceType getTypeForPromotion(Board board, boolean projection) {
    	if(projection){
            return PieceType.QUEEN;
    	}
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(color.toString() + ": What piece type would you like to getTypeForPromotion to?");
            return PieceType.valueOf(bufferedReader.readLine());
        } catch (Exception e) {
            System.out.println("That's not a type you can getTypeForPromotion to.  Please try again.");
            return getTypeForPromotion(board, projection);
        }
    }

    @Override
    public Move makeNextMove(Board board) {
        Position start = getPosition(color.name() + " make your move.  Enter the start position of your move:");
        Position end = getPosition("Now enter the end position:");
        return new Move(new MovePositions(start, end), board, this);
    }

    private Position getPosition(String message) {
        try {
            System.out.println(message);
            return new Position(bufferedReader.readLine());
        } catch (Exception e) {
            System.out.println("I didn't understand that.  Please try again.");
            return getPosition(message);
        }
    }
}
