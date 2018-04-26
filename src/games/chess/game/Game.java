package games.chess.game;

import games.chess.board.Board;
import games.chess.board.BoardSetter;
import games.chess.move.Move;
import games.chess.piece.Color;

public class Game {
    public Player whitePlayer;
    public Player blackPlayer;
    public Board board;
    public int enterGameLoop(boolean debug, boolean loseOnBadMove) {
        Player currentPlayer = whitePlayer;
        int turns = 0;
        while (!board.isCheckmated(currentPlayer.color) && !board.isStalemated(currentPlayer.color) && board.moves.size() < 1000) {
            if(debug){
            	board.print();
            }
            turns++;
        	boolean moved = makeMove(currentPlayer, board, debug, loseOnBadMove);
            if(!moved && loseOnBadMove){
            	if(currentPlayer.color.equals(Color.WHITE)){
            		return -turns;
            	}
            	else{
            		return turns;
            	}
            }
            currentPlayer = board.getPlayerByColor(currentPlayer.getColor().opposite());
        }

        if (board.isCheckmated(currentPlayer.color)) {
        	if(debug){
        		System.out.println(currentPlayer.getColor().opposite().name() + " checkmates " + currentPlayer.getColor().name());
        	}
        	if(currentPlayer.color.equals(Color.WHITE)){
        		return -turns;
        	}
        	else{
        		return turns;
        	}
        }
        else if (board.isStalemated(currentPlayer.color)) {
        	if(debug){
        		System.out.println("The game ends in a stalemate");
        	}
        } else if (board.moves.size() >= 1000) {
        	if(debug){
        		System.out.println("This madness has gone on long enough");
        	}
        }
        return 0;
    }

    private static boolean makeMove(Player player, Board board, boolean debug, boolean loseOnBadMove) {
        Move move = player.makeNextMove(board);
        if (board.getPieceAtPosition(move.getStartPosition()) != null
                && board.getPieceAtPosition(move.getStartPosition()).color == move.getPlayer().color
                && board.makeMove(move)) {
            
        	if(debug){
        		System.out.println(player.getColor().name() + " has made move " + move.toString());
        	}
            return true;
        }
        if(debug){
        	System.out.println("That move is not legal.");
        }
        if(!loseOnBadMove){
        	return makeMove(player, board, debug, loseOnBadMove);
        }
        return false;
    }

    public void setBoard() {
        board = new Board(whitePlayer, blackPlayer);
        BoardSetter.setBoard(board);
    }
}