package com.example.chessAI.ai;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.Move;

public interface ChessEngineAdapter {
    void initialize() throws ChessEngineException;
    void startNewGame() throws ChessEngineException;
    Move getBestMove(ChessBoard board, Color sideToMove, AiDifficulty difficulty, String gameId) throws ChessEngineException;
    void stop();
    void dispose();
}
