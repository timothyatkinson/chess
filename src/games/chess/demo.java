package games.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import games.chess.game.Game;
import games.chess.game.HumanPlayer;
import games.chess.game.RobotPlayer;
import games.chess.piece.Color;

public class demo {

    private static final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to chess!");
        
        Game g = new Game();
        createPlayers(g);
        g.setBoard();

        int winner = g.enterGameLoop(true, false);

        System.out.println("Game over! " + winner);
    }
    
    private static void createPlayers(Game g) {
        int numHumans = getNumberOfHumanPlayers();
        if (numHumans == 1) {
            Color humanPlayerColor = getHumanPlayerColor();
            g.whitePlayer = humanPlayerColor == Color.WHITE ? new HumanPlayer(Color.WHITE) : new RobotPlayer(Color.WHITE);
            g.blackPlayer = humanPlayerColor == Color.BLACK ? new HumanPlayer(Color.BLACK) : new RobotPlayer(Color.BLACK);
        } else if (numHumans == 0) {
        	g.whitePlayer = new RobotPlayer(Color.WHITE);
        	g.blackPlayer = new RobotPlayer(Color.BLACK);
        } else if(numHumans == 2) {
        	g.whitePlayer = new HumanPlayer(Color.WHITE);
        	g.blackPlayer = new HumanPlayer(Color.BLACK);
        } else {
            System.out.println("Please enter a number between 0 and 2 inclusive");
            createPlayers(g);
        }
    }

    private static int getNumberOfHumanPlayers() {
        try {
            System.out.println("How many humans are playing?");
            return Integer.parseInt(bufferedReader.readLine());
        } catch (Exception e) {
            System.out.println("I did not understand that.  Please enter 0, 1, or 2.");
            return getNumberOfHumanPlayers();
        }
    }

    public static Color getHumanPlayerColor() {
        try {
            System.out.println("What color would you like to play as?");
            return Color.valueOf(bufferedReader.readLine().toUpperCase());
        } catch (Exception e) {
            System.out.println("I did not understand that.  Please enter \"black\" or \"white\"");
            return getHumanPlayerColor();
        }
    }
}
