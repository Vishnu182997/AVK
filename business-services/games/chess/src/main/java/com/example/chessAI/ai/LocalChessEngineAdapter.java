package com.example.chessAI.ai;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;

public class LocalChessEngineAdapter implements ChessEngineAdapter {
    private final ChessEngine engine;
    private volatile boolean disposed;
    private volatile boolean stopped;

    public LocalChessEngineAdapter() {
        this.engine = new ChessEngine();
    }

    @Override
    public void initialize() throws ChessEngineException {
        if (disposed) {
            throw new ChessEngineException("Chess engine has been disposed.");
        }
        stopped = false;
    }

    @Override
    public void startNewGame() throws ChessEngineException {
        initialize();
    }

    @Override
    public Move getBestMove(ChessBoard board, Color sideToMove, AiDifficulty difficulty, String gameId)
            throws ChessEngineException {
        initialize();
        if (board == null || sideToMove == null || difficulty == null || gameId == null) {
            throw new ChessEngineException("Missing engine search input.");
        }
        stopped = false;
        engine.setSearchDepth(difficulty.getSearchDepth());
        Move move = engine.getBestMove(board, sideToMove);
        if (stopped || disposed) {
            return null;
        }
        return move;
    }

    @Override
    public void stop() { stopped = true; }

    @Override
    public void dispose() {
        stopped = true;
        disposed = true;
    }
}
