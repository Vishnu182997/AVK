package com.example.chessAI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.chessAI.ai.AiDifficulty;
import com.example.chessAI.ai.ChessEngineAdapter;
import com.example.chessAI.ai.ChessEngineException;
import com.example.chessAI.gui.BoardPanel;
import com.example.chessAI.pieces.King;
import com.example.chessAI.pieces.Pawn;
import com.example.chessAI.pieces.Queen;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class ChessGameModeTest {

    @Test
    void humanVsHumanPermitsBothSidesInTurn() {
        BoardPanel panel = new BoardPanel(new MockEngine());
        assertTrue(panel.attemptMove(new Move(6, 4, 4, 4)));
        assertEquals(Color.BLACK, panel.getChessBoard().getSideToMove());
        assertTrue(panel.attemptMove(new Move(1, 4, 3, 4)));
        assertEquals(Color.WHITE, panel.getChessBoard().getSideToMove());
    }

    @Test
    void humanVsAiOnlyPermitsHumanControlledColorAndTriggersOneAiRequest() throws Exception {
        MockEngine engine = new MockEngine(new Move(1, 4, 3, 4));
        BoardPanel panel = new BoardPanel(engine);
        panel.setGameMode(GameMode.HUMAN_VS_AI);
        assertFalse(panel.attemptMove(new Move(1, 4, 3, 4)));
        assertTrue(panel.attemptMove(new Move(6, 4, 4, 4)));
        assertTrue(panel.isAiThinking());
        assertTrue(engine.awaitSearch());
        flushEdtAndDelay();
        assertEquals(1, engine.searchCount);
        assertEquals(Color.WHITE, panel.getChessBoard().getSideToMove());
        assertFalse(panel.isAiThinking());
    }

    @Test
    void invalidAiMoveProducesRecoverableErrorWithoutChangingPosition() throws Exception {
        MockEngine engine = new MockEngine(new Move(0, 0, 7, 7));
        BoardPanel panel = new BoardPanel(engine);
        panel.setGameMode(GameMode.HUMAN_VS_AI);
        assertTrue(panel.attemptMove(new Move(6, 4, 4, 4)));
        assertTrue(engine.awaitSearch());
        flushEdtAndDelay();
        assertNotNull(panel.getEngineError());
        assertEquals(Color.BLACK, panel.getChessBoard().getSideToMove());
    }

    @Test
    void resetAndModeChangeInvalidatePendingAiResponses() throws Exception {
        MockEngine engine = new MockEngine(new Move(1, 4, 3, 4));
        engine.blockSearch = true;
        BoardPanel panel = new BoardPanel(engine);
        panel.setGameMode(GameMode.HUMAN_VS_AI);
        String firstGameId = panel.getGameId();
        assertTrue(panel.attemptMove(new Move(6, 4, 4, 4)));
        assertTrue(engine.awaitSearch());
        panel.resetBoard();
        assertNotEquals(firstGameId, panel.getGameId());
        panel.setGameMode(GameMode.HUMAN_VS_HUMAN);
        engine.releaseSearch();
        flushEdtAndDelay();
        assertEquals(Color.WHITE, panel.getChessBoard().getSideToMove());
        assertEquals(1, engine.stopCount);
        assertEquals(1, engine.disposeCount);
    }

    @Test
    void removeNotifyDisposesEngine() {
        MockEngine engine = new MockEngine();
        BoardPanel panel = new BoardPanel(engine);
        panel.removeNotify();
        assertEquals(1, engine.disposeCount);
    }

    @Test
    void checkmatePreventsAiRequest() throws Exception {
        MockEngine engine = new MockEngine();
        BoardPanel panel = new BoardPanel(engine);
        panel.setGameMode(GameMode.HUMAN_VS_AI);
        panel.getChessBoard().clear();
        panel.getChessBoard().setCastlingRights(false, false, false, false);
        panel.getChessBoard().setPiece(5, 5, new King(Color.WHITE));
        panel.getChessBoard().setPiece(0, 7, new King(Color.BLACK));
        panel.getChessBoard().setPiece(2, 6, new Queen(Color.WHITE));
        panel.getChessBoard().setSideToMove(Color.WHITE);
        assertTrue(panel.attemptMove(new Move(2, 6, 1, 6)));
        flushEdtAndDelay();
        assertEquals(0, engine.searchCount);
        assertTrue(panel.isGameOver());
    }

    @Test
    void aiPromotionNotationIsApplied() throws Exception {
        MockEngine engine = new MockEngine(promotionMove(1, 0, 0, 0, PieceType.KNIGHT));
        BoardPanel panel = new BoardPanel(engine);
        panel.getChessBoard().clear();
        panel.getChessBoard().setCastlingRights(false, false, false, false);
        panel.getChessBoard().setPiece(7, 4, new King(Color.WHITE));
        panel.getChessBoard().setPiece(0, 7, new King(Color.BLACK));
        panel.getChessBoard().setPiece(6, 7, new Pawn(Color.WHITE));
        panel.getChessBoard().setPiece(1, 0, new Pawn(Color.BLACK));
        panel.getChessBoard().setSideToMove(Color.WHITE);
        panel.setGameMode(GameMode.HUMAN_VS_AI);
        assertTrue(panel.attemptMove(promotionMove(6, 7, 5, 7, PieceType.QUEEN)));
        assertTrue(engine.awaitSearch());
        flushEdtAndDelay();
        assertEquals(PieceType.KNIGHT, panel.getChessBoard().getPiece(0, 0).getType());
    }

    @Test
    void humanPromotionSelectionUsesRequestedPieceInHeadlessTests() {
        BoardPanel panel = new BoardPanel(new MockEngine());
        panel.getChessBoard().clear();
        panel.getChessBoard().setCastlingRights(false, false, false, false);
        panel.getChessBoard().setPiece(7, 4, new King(Color.WHITE));
        panel.getChessBoard().setPiece(0, 7, new King(Color.BLACK));
        panel.getChessBoard().setPiece(1, 0, new Pawn(Color.WHITE));
        panel.getChessBoard().setSideToMove(Color.WHITE);
        assertTrue(panel.attemptMove(promotionMove(1, 0, 0, 0, PieceType.ROOK)));
        assertEquals(PieceType.ROOK, panel.getChessBoard().getPiece(0, 0).getType());
    }

    @Test
    void difficultyConfigurationUsesExpectedPracticalLimits() {
        assertEquals(150, AiDifficulty.EASY.getMoveTimeMillis());
        assertEquals(500, AiDifficulty.MEDIUM.getMoveTimeMillis());
        assertEquals(1000, AiDifficulty.HARD.getMoveTimeMillis());
        assertEquals(2000, AiDifficulty.EXPERT.getMoveTimeMillis());
        assertTrue(AiDifficulty.EXPERT.getSearchDepth() > AiDifficulty.EASY.getSearchDepth());
    }

    @Test
    void engineFailureShowsRecoverableError() throws Exception {
        MockEngine engine = new MockEngine();
        engine.failSearch = true;
        BoardPanel panel = new BoardPanel(engine);
        JLabel label = new JLabel();
        panel.setStatusLabel(label);
        panel.setGameMode(GameMode.HUMAN_VS_AI);
        assertTrue(panel.attemptMove(new Move(6, 4, 4, 4)));
        assertTrue(engine.awaitSearch());
        flushEdtAndDelay();
        assertNotNull(panel.getEngineError());
        assertTrue(label.getText().contains("failed"));
    }

    @Test
    void humanVsAiUndoRemovesComputerAndHumanMove() throws Exception {
        MockEngine engine = new MockEngine(new Move(1, 4, 3, 4));
        BoardPanel panel = new BoardPanel(engine);
        panel.setGameMode(GameMode.HUMAN_VS_AI);
        assertTrue(panel.attemptMove(new Move(6, 4, 4, 4)));
        assertTrue(engine.awaitSearch());
        flushEdtAndDelay();
        panel.undoMove();
        assertEquals(Color.WHITE, panel.getChessBoard().getSideToMove());
        assertNotNull(panel.getChessBoard().getPiece(6, 4));
        assertNotNull(panel.getChessBoard().getPiece(1, 4));
    }

    private static Move promotionMove(int fromRow, int fromCol, int toRow, int toCol, PieceType pieceType) {
        Move move = new Move(fromRow, fromCol, toRow, toCol);
        move.setPromotion(true);
        move.setPromotionPiece(pieceType);
        return move;
    }

    private static void flushEdtAndDelay() throws Exception {
        Thread.sleep(BoardPanel.AI_VISUAL_DELAY_MILLIS + 100L);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override public void run() { }
        });
    }

    private static class MockEngine implements ChessEngineAdapter {
        private Move move;
        private int searchCount;
        private int stopCount;
        private int disposeCount;
        private boolean blockSearch;
        private boolean failSearch;
        private CountDownLatch searched = new CountDownLatch(1);
        private CountDownLatch release = new CountDownLatch(1);

        MockEngine(Move move) { this.move = move; }
        MockEngine() { this(new Move(1, 4, 3, 4)); }

        @Override public void initialize() { }
        @Override public void startNewGame() { }

        @Override
        public Move getBestMove(ChessBoard board, Color sideToMove, AiDifficulty difficulty, String gameId)
                throws ChessEngineException {
            searchCount++;
            searched.countDown();
            if (blockSearch) {
                try { release.await(2, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
            if (failSearch) { throw new ChessEngineException("boom"); }
            return move;
        }

        @Override public void stop() { stopCount++; release.countDown(); }
        @Override public void dispose() { disposeCount++; release.countDown(); }
        boolean awaitSearch() throws InterruptedException { return searched.await(2, TimeUnit.SECONDS); }
        void releaseSearch() { release.countDown(); }
    }
}
