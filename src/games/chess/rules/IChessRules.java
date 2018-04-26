package games.chess.rules;

import java.util.List;

import games.chess.move.PieceMove;

public interface IChessRules {
    public boolean isMovePossible();
    public List<PieceMove> populatePiecesMoved(boolean projection);
}
