package games.chess;

import java.io.IOException;

import games.chess.game.Game;
import games.chess.game.HumanPlayer;
import games.chess.game.NetworkPlayer;
import games.chess.game.RobotPlayer;
import games.chess.piece.Color;
import nets.Network;

public class EA {

	public static class Result{
		int maxturns;
		int score;
		int turns;
		public Result(int maxturns, int score, int turns){
			this.maxturns = maxturns;
			this.score = score;
			this.turns = turns;
		}
	}
	
	
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to chess!");
        
        run(200, 0.1, 0.01, 20, 4, 3, 3, 30, 20000);
    }
    
	public static void run(int nodes, double const_rate, double conn_rate, int ticks, int lambda, int ffcon, int reccon, int popsize, int gen){
		Network[] pop = new Network[popsize];
		for(int i = 0; i < popsize; i++){
			System.out.println("Generating first individual " + i);
			int whiteMoves = 0;
			int blackMoves = 1;
			while(whiteMoves <= 0 || blackMoves <= 1){
				pop[i] = Network.randNetwork(64, 5, nodes, ffcon, reccon, -1.0, 1.0);
				Game g = new Game();
				pop[i].resetValues();
		        g.whitePlayer = new NetworkPlayer(Color.WHITE, pop[i], ticks);
		        g.blackPlayer = new RobotPlayer(Color.BLACK);
		        g.setBoard();
		        whiteMoves = g.enterGameLoop(false, true);
			    if(whiteMoves < 0){
			    	whiteMoves = whiteMoves * -1;
			    }
			    whiteMoves--;
		        g = new Game();
				pop[i].resetValues();
				pop[i].resetValues();
		        g.whitePlayer = new RobotPlayer(Color.WHITE);
		        g.blackPlayer = new NetworkPlayer(Color.BLACK, pop[i], ticks);
		        g.setBoard();
		        blackMoves = g.enterGameLoop(false, true);
		        if(blackMoves < 0){
		        	blackMoves = blackMoves * -1;
		        }
		        blackMoves--;
			}
		}
		int i = 0;
		while(i < gen){
			Network[] newpop = new Network[popsize];
			int[] scores = new int[popsize];
			int[] turn = new int[popsize];
			int[] indturn = new int[popsize];
			for(int j = 0; j < popsize; j++){
				//Get parent score
				Network newM = pop[j];
				Result r = getScore(pop, popsize, pop[j], ticks);
				int bestScore = r.score;
				int turns = r.maxturns;
				int indturns = r.turns;
				for(int k = 0; k < lambda; k++){
					Network can = Network.mutateNetwork(pop[j], const_rate, conn_rate);
					r = getScore(pop, popsize, can, ticks);
					int canScore = r.score;
					int canTurns = r.turns;
					if(canScore > bestScore || (canScore == bestScore && canTurns >= indturns)){
						newM = can;
						bestScore = canScore;
						turns = r.maxturns;
						indturns = canTurns;
					}
				}
				newpop[j] = newM;
				scores[j] = bestScore;
				turn[j] = turns;
				indturn[j] = indturns;
			}
			pop = newpop;
			i++;
			System.out.println("Generation " + i);
			System.out.print("{");
			for(int j = 0; j < popsize; j++){
				if(j != 0){
					System.out.print(", ");
				}
				System.out.print(scores[j]);
			}
			System.out.print("}\n");
			System.out.print("{");
			for(int j = 0; j < popsize; j++){
				if(j != 0){
					System.out.print(", ");
				}
				System.out.print(indturn[j]);
			}
			System.out.print("}\n");
			System.out.print("{");
			for(int j = 0; j < popsize; j++){
				if(j != 0){
					System.out.print(", ");
				}
				System.out.print(turn[j]);
			}
			System.out.print("}\n");
		}
		
		Network best = pop[0];
		Result r = getScore(pop, popsize, best, ticks);
		double bestScore = r.score;
		int indturns = r.turns;
		for(int j = 0; j < popsize; j++){
			r = getScore(pop, popsize, best, ticks);
			double score = r.score;
			int canturns = r.turns;
			if(score > bestScore || (score == bestScore && canturns >= indturns)){
				bestScore = score;
				best = pop[j];
				indturns = canturns;
			}
		}
		
		while(true){
			Game g = new Game();
            Color humanPlayerColor = demo.getHumanPlayerColor();
            g.whitePlayer = humanPlayerColor == Color.WHITE ? new HumanPlayer(Color.WHITE) : new NetworkPlayer(Color.WHITE, best, ticks);
            g.blackPlayer = humanPlayerColor == Color.BLACK ? new HumanPlayer(Color.BLACK) : new NetworkPlayer(Color.BLACK, best, ticks);

            g.setBoard();
            g.enterGameLoop(true, true);
		}
		
	}
	
	public static Result getScore(Network[] pop, int popsize, Network candidate, int ticks){
		int score = 0;
		int maxturns = 0;
		int turns = 0;
		for(int i = 0; i < popsize; i++){
			candidate.resetValues();
			pop[i].resetValues();
	        Game g = new Game();
	        g.whitePlayer = new NetworkPlayer(Color.WHITE, candidate, ticks);
	        g.blackPlayer = new NetworkPlayer(Color.BLACK, pop[i], ticks);
	        g.setBoard();
	        int winnerWhite = g.enterGameLoop(false, true);
	        int mturns = winnerWhite;
	        if(winnerWhite < 0){
	        	score = score  - 1;
	        	mturns = mturns * -1;
	        }
	        else if(winnerWhite > 0){
	        	score = score + 1;
	        }
	        if(mturns > maxturns){
	        	maxturns = mturns;
	        }
	        turns += (mturns - 1);
	        g = new Game();
			candidate.resetValues();
			pop[i].resetValues();
	        g.whitePlayer = new NetworkPlayer(Color.WHITE, pop[i], ticks);
	        g.blackPlayer = new NetworkPlayer(Color.BLACK, candidate, ticks);
	        g.setBoard();
	        int winnerBlack = g.enterGameLoop(false, true);
	        mturns = winnerBlack;
	        if(winnerBlack < 0){
	        	score = score + 1;
	        	mturns = mturns * -1;
	        }
	        else if(winnerBlack > 0){
	        	score = score - 1;
	        }
	        if(mturns > maxturns){
	        	maxturns = mturns;
	        }
	        turns += (mturns - 1);candidate.resetValues();

		}
		for(int i = 0; i < 10; i++){
	        Game g = new Game();
			candidate.resetValues();
	        g.whitePlayer = new NetworkPlayer(Color.WHITE, candidate, ticks);
	        g.blackPlayer = new RobotPlayer(Color.BLACK);
	        g.setBoard();
	        int winnerWhite = g.enterGameLoop(false, true);
	        int mturns = winnerWhite;
	        if(winnerWhite < 0){
	        	score = score  - 1;
	        	mturns = mturns * -1;
	        }
	        else if(winnerWhite > 0){
	        	score = score + 1;
	        }
	        if(mturns > maxturns){
	        	maxturns = mturns;
	        }
	        turns += (mturns - 1);
	        g = new Game();
			candidate.resetValues();
	        g.whitePlayer = new RobotPlayer(Color.WHITE);
	        g.blackPlayer = new NetworkPlayer(Color.BLACK, candidate, ticks);
	        g.setBoard();
	        int winnerBlack = g.enterGameLoop(false, true);
	        mturns = winnerBlack;
	        if(winnerBlack < 0){
	        	score = score + 1;
	        	mturns = mturns * -1;
	        }
	        else if(winnerBlack > 0){
	        	score = score - 1;
	        }
	        if(mturns > maxturns){
	        	maxturns = mturns;
	        }
	        turns += (mturns - 1);
		}
		return new EA.Result(maxturns -1 , score, turns);
	}
}
