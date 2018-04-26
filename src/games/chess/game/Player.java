package games.chess.game;

import games.chess.board.Board;
import games.chess.move.Move;
import games.chess.piece.Color;
import games.chess.piece.PieceType;

public abstract class Player {
    protected Color color;
    public Player(Color color) {
        this.color = color;
    }
    public Color getColor() {return color;}
    public abstract Move makeNextMove(Board board);
    public abstract PieceType getTypeForPromotion(Board board, boolean projection);
}
