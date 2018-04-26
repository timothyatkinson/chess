package games.chess.rules;

import java.util.List;

import games.chess.move.Move;
import games.chess.move.PieceMove;

public class StandardChessRules implements IChessRules {
    private final StandardMovesAuthorizor movesAuthorizor;
    private final StandardMovesPopulator movesPopulator;

    public StandardChessRules(Move move) {
        this.movesAuthorizor = new StandardMovesAuthorizor(move);
        this.movesPopulator = new StandardMovesPopulator(move);
    }

    @Override
    public boolean isMovePossible()  {
        return movesAuthorizor.isMovePossible();
    }

    @Override
    public List<PieceMove> populatePiecesMoved(boolean projection)  {
        return movesPopulator.populateMove(projection);
    }
}
