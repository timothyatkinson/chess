package games.chess.game;

import games.chess.board.Board;
import games.chess.board.Position;
import games.chess.move.Move;
import games.chess.move.MovePositions;
import games.chess.piece.Color;
import games.chess.piece.PieceType;
import nets.Network;

public class NetworkPlayer extends Player{

	Network n;
	int steps;
	public NetworkPlayer(Color color, Network n, int steps) {
		super(color);
		this.n = n;
		this.steps = steps;
		n.resetValues();
	}

	@Override
	public Move makeNextMove(Board board) {
		double[] in = board.convertToNetworkInput(this.color);
		int i = 0;
		while(i < steps){
			double[] out = null;
			try {
				out = this.n.tick(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(out[4] > 0.5){
				Move m = getNNMove(out, board, this.color);
				if(m.isLegal()){
					return m;
				}
			}
			i++;
		}
		return new Move(new MovePositions(new Position(0,0), new Position(0,0)), board, this);
	}
	
	public Move getNNMove(double[] out, Board board, Color c){
		Position start = new Position(getNNPos(out[0], c), getNNPos(out[1], c));
		Position end = new Position(getNNPos(out[2], c), getNNPos(out[3], c));
		
		return new Move(new MovePositions(start, end), board, this);
	}

	public int getNNPos(double o, Color c){
		if(c == Color.WHITE){
			if(o <= 1.0/8.0){
				return 0;
			}
			else if(o <= 2.0/8.0){
				return 1;
			}
			else if(o <= 3.0/8.0){
				return 2;
			}
			else if(o <= 4.0/8.0){
				return 3;
			}
			else if(o <= 5.0/8.0){
				return 4;
			}
			else if(o <= 6.0/8.0){
				return 5;
			}
			else if(o <= 7.0/8.0){
				return 6;
			}
			else{
				return 7;
			}
		}
		else{
			if(o <= 1.0/8.0){
				return 7;
			}
			else if(o <= 2.0/8.0){
				return 6;
			}
			else if(o <= 3.0/8.0){
				return 5;
			}
			else if(o <= 4.0/8.0){
				return 4;
			}
			else if(o <= 5.0/8.0){
				return 3;
			}
			else if(o <= 6.0/8.0){
				return 2;
			}
			else if(o <= 7.0/8.0){
				return 1;
			}
			else{
				return 0;
			}
		}
	}

    @Override
    public PieceType getTypeForPromotion(Board board, boolean projection) {
        return PieceType.QUEEN;
    }

}
