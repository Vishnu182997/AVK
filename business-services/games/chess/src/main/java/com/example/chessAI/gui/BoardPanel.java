package com.example.chessAI.gui;

import com.example.chessAI.ChessBoard;
import com.example.chessAI.Color;
import com.example.chessAI.GameMode;
import com.example.chessAI.Move;
import com.example.chessAI.MoveGenerator;
import com.example.chessAI.MoveValidator;
import com.example.chessAI.Piece;
import com.example.chessAI.PieceType;
import com.example.chessAI.ai.AiDifficulty;
import com.example.chessAI.ai.ChessEngineAdapter;
import com.example.chessAI.ai.ChessEngineException;
import com.example.chessAI.ai.LocalChessEngineAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoardPanel extends JPanel {

    public static final int BOARD_SIZE = 8;
    public static final int AI_VISUAL_DELAY_MILLIS = 250;

    private final SquarePanel[][] squares;
    private final ChessBoard board;
    private final MoveGenerator moveGenerator;
    private final MoveValidator moveValidator;
    private final List<Move> selectedLegalMoves;
    private final List<Move> appliedMoves;
    private final List<String> appliedMoveAnnouncements;
    private ChessEngineAdapter chessEngine;

    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean gameOver;
    private boolean aiThinking;
    private boolean engineReady;
    private JLabel statusLabel;
    private GameMode gameMode = GameMode.HUMAN_VS_HUMAN;
    private AiDifficulty aiDifficulty = AiDifficulty.HARD;
    private Color humanColor = Color.WHITE;
    private String gameId = UUID.randomUUID().toString();
    private String engineError;
    private SwingWorker<Move, Void> aiWorker;
    private boolean engineDisposed;
    private Timer aiDelayTimer;
    private String activeAiRequestId;
    private Move lastMove;
    private String lastMoveAnnouncement;

    public BoardPanel() {
        this(new LocalChessEngineAdapter());
    }

    public BoardPanel(ChessEngineAdapter chessEngine) {
        squares = new SquarePanel[BOARD_SIZE][BOARD_SIZE];
        board = new ChessBoard();
        moveGenerator = new MoveGenerator();
        moveValidator = new MoveValidator();
        selectedLegalMoves = new ArrayList<Move>();
        appliedMoves = new ArrayList<Move>();
        appliedMoveAnnouncements = new ArrayList<String>();
        this.chessEngine = chessEngine;
        PieceImages.loadImages();
        initializeBoard();
        updateBoard();
    }

    private void initializeBoard() {
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        setPreferredSize(new Dimension(640, 640));
        removeAll();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean lightSquare = (row + col) % 2 == 0;
                SquarePanel square = new SquarePanel(row, col, lightSquare);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(square.getBoardRow(), square.getBoardColumn());
                    }
                });
                squares[row][col] = square;
                add(square);
            }
        }
    }

    public void setStatusLabel(JLabel statusLabel) { this.statusLabel = statusLabel; updateStatus(); }
    public ChessBoard getChessBoard() { return board; }
    public GameMode getGameMode() { return gameMode; }
    public AiDifficulty getAiDifficulty() { return aiDifficulty; }
    public Color getHumanColor() { return humanColor; }
    public boolean isAiThinking() { return aiThinking; }
    public boolean isEngineReady() { return engineReady; }
    public boolean isGameOver() { return gameOver; }
    public String getEngineError() { return engineError; }
    public String getGameId() { return gameId; }
    public Move getLastMove() { return lastMove; }
    public String getLastMoveAnnouncement() { return lastMoveAnnouncement; }

    public SquarePanel getSquare(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) { return null; }
        return squares[row][col];
    }

    public void setGameMode(GameMode gameMode) {
        if (gameMode == null || this.gameMode == gameMode) { return; }
        this.gameMode = gameMode;
        resetBoard();
        if (gameMode == GameMode.HUMAN_VS_AI) { ensureEngineReady(); scheduleAiTurnIfNeeded(); }
        else { disposeEngine(); }
        updateStatus();
    }

    public void setAiDifficulty(AiDifficulty aiDifficulty) {
        if (aiDifficulty == null) { return; }
        cancelPendingAi();
        this.aiDifficulty = aiDifficulty;
        invalidateGameId();
        updateStatus();
        scheduleAiTurnIfNeeded();
    }

    public void setHumanColor(Color humanColor) {
        if (humanColor == null || this.humanColor == humanColor) { return; }
        this.humanColor = humanColor;
        resetBoard();
        scheduleAiTurnIfNeeded();
    }

    public void clearHighlights() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].setHighlighted(false);
                squares[row][col].setSelected(false);
            }
        }
        repaint();
    }

    public void highlightSquare(int row, int col) {
        SquarePanel square = getSquare(row, col);
        if (square != null) { square.setHighlighted(true); }
        repaint();
    }

    public void resetBoard() {
        cancelPendingAi();
        board.initializeBoard();
        appliedMoves.clear();
        appliedMoveAnnouncements.clear();
        clearLastMove();
        gameOver = false;
        engineError = null;
        invalidateGameId();
        clearSelection();
        if (gameMode == GameMode.HUMAN_VS_AI) {
            ensureEngineReady();
            try { chessEngine.startNewGame(); } catch (ChessEngineException e) { engineError = e.getMessage(); }
        }
        updateBoard();
        updateLastMoveAfterUndo();
        updateGameState();
        scheduleAiTurnIfNeeded();
    }

    public void undoMove() {
        cancelPendingAi();
        int movesToUndo = gameMode == GameMode.HUMAN_VS_AI ? 2 : 1;
        for (int i = 0; i < movesToUndo; i++) {
            board.undoMove();
            if (!appliedMoves.isEmpty()) { appliedMoves.remove(appliedMoves.size() - 1); }
            if (!appliedMoveAnnouncements.isEmpty()) { appliedMoveAnnouncements.remove(appliedMoveAnnouncements.size() - 1); }
        }
        gameOver = false;
        engineError = null;
        invalidateGameId();
        clearSelection();
        updateBoard();
        updateLastMoveAfterUndo();
        updateGameState();
        scheduleAiTurnIfNeeded();
    }

    public void refreshBoard() { updateBoard(); }

    public void updateBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                squares[row][col].setPiece(piece, PieceImages.get(piece));
            }
        }
        revalidate();
        repaint();
    }

    public boolean attemptMove(Move move) {
        if (!canHumanInteract() || move == null || !moveValidator.isValidMove(board, move)) { return false; }
        Move legalMove = findMatchingLegalMove(move);
        if (legalMove == null) { return false; }
        if (legalMove.isPromotion()) {
            legalMove.setPromotionPiece(resolvePromotionPiece(move.getPromotionPiece()));
        }
        applyLegalMove(legalMove, false);
        scheduleAiTurnIfNeeded();
        return true;
    }

    private void applyLegalMove(Move legalMove, boolean computerMove) {
        Color mover = board.getSideToMove();
        board.makeMove(legalMove);
        appliedMoves.add(legalMove);
        String announcement = completedMoveAnnouncement(mover, legalMove, computerMove);
        appliedMoveAnnouncements.add(announcement);
        lastMove = legalMove;
        lastMoveAnnouncement = announcement;
        updateLastMoveSquares();
        clearSelection();
        updateBoard();
        updateGameState();
    }

    private void updateLastMoveAfterUndo() {
        lastMove = appliedMoves.isEmpty() ? null : appliedMoves.get(appliedMoves.size() - 1);
        lastMoveAnnouncement = appliedMoveAnnouncements.isEmpty()
                ? null
                : appliedMoveAnnouncements.get(appliedMoveAnnouncements.size() - 1);
        updateLastMoveSquares();
    }

    private void clearLastMove() {
        lastMove = null;
        lastMoveAnnouncement = null;
        updateLastMoveSquares();
    }

    private void updateLastMoveSquares() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].setLastMoveFrom(lastMove != null
                        && lastMove.getFromRow() == row
                        && lastMove.getFromCol() == col);
                squares[row][col].setLastMoveTo(lastMove != null
                        && lastMove.getToRow() == row
                        && lastMove.getToCol() == col);
            }
        }
    }

    private String completedMoveAnnouncement(Color mover, Move move, boolean computerMove) {
        String actor = computerMove ? "Computer" : (mover == Color.WHITE ? "White" : "Black");
        return actor + " moved from " + squareName(move.getFromRow(), move.getFromCol())
                + " to " + squareName(move.getToRow(), move.getToCol()) + ".";
    }

    private String squareName(int row, int col) {
        return String.valueOf((char) ('a' + col)) + (8 - row);
    }

    private void handleSquareClick(int row, int col) {
        if (!canHumanInteract()) { return; }

        Piece clickedPiece = board.getPiece(row, col);
        if (selectedRow >= 0) {
            Move selectedMove = findSelectedMove(row, col);
            if (selectedMove != null) {
                if (selectedMove.isPromotion()) {
                    selectedMove.setPromotionPiece(showPromotionDialog());
                }
                attemptMove(selectedMove);
                return;
            }
        }

        if (clickedPiece != null
                && clickedPiece.getColor() == board.getSideToMove()
                && isHumanControlled(clickedPiece.getColor())) {
            selectSquare(row, col);
        } else {
            clearSelection();
            updateBoard();
            updateStatus();
        }
    }

    private boolean canHumanInteract() {
        return !gameOver && !aiThinking && engineError == null && isHumanControlled(board.getSideToMove());
    }

    private boolean isHumanControlled(Color color) {
        return gameMode == GameMode.HUMAN_VS_HUMAN || color == humanColor;
    }

    private void selectSquare(int row, int col) {
        clearSelection();
        selectedRow = row;
        selectedCol = col;
        squares[row][col].setSelected(true);
        selectedLegalMoves.addAll(getLegalMovesFrom(row, col));
        for (Move move : selectedLegalMoves) { highlightSquare(move.getToRow(), move.getToCol()); }
        updateStatus();
    }

    private List<Move> getLegalMovesFrom(int row, int col) {
        List<Move> moves = new ArrayList<Move>();
        for (Move move : moveGenerator.generateLegalMoves(board, board.getSideToMove())) {
            if (move.getFromRow() == row && move.getFromCol() == col) { moves.add(move); }
        }
        return moves;
    }

    private Move findSelectedMove(int row, int col) {
        for (Move move : selectedLegalMoves) {
            if (move.getToRow() == row && move.getToCol() == col) { return move; }
        }
        return null;
    }

    private Move findMatchingLegalMove(Move requestedMove) {
        for (Move legalMove : moveGenerator.generateLegalMoves(board, board.getSideToMove())) {
            if (sameCoordinates(legalMove, requestedMove)) { return legalMove; }
        }
        return null;
    }

    private boolean sameCoordinates(Move left, Move right) {
        return left.getFromRow() == right.getFromRow()
                && left.getFromCol() == right.getFromCol()
                && left.getToRow() == right.getToRow()
                && left.getToCol() == right.getToCol();
    }

    private PieceType resolvePromotionPiece(PieceType requested) {
        if (requested == PieceType.QUEEN || requested == PieceType.ROOK
                || requested == PieceType.BISHOP || requested == PieceType.KNIGHT) {
            return requested;
        }
        return showPromotionDialog();
    }

    private PieceType showPromotionDialog() {
        if (GraphicsEnvironment.isHeadless()) { return PieceType.QUEEN; }
        PieceType[] choices = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};
        PieceType selected = (PieceType) JOptionPane.showInputDialog(
                this,
                "Choose promotion piece:",
                "Pawn Promotion",
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                PieceType.QUEEN);
        return selected == null ? PieceType.QUEEN : selected;
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        selectedLegalMoves.clear();
        clearHighlights();
    }

    private void updateGameState() {
        Color turn = board.getSideToMove();
        List<Move> legalMoves = moveGenerator.generateLegalMoves(board, turn);
        gameOver = legalMoves.isEmpty() || board.getHalfMoveClock() >= 100;
        updateStatus();
    }

    private void scheduleAiTurnIfNeeded() {
        if (gameMode != GameMode.HUMAN_VS_AI || gameOver || aiThinking || engineError != null) { return; }
        if (board.getSideToMove() == humanColor) { return; }
        if (moveGenerator.generateLegalMoves(board, board.getSideToMove()).isEmpty()) { return; }

        ensureEngineReady();
        if (engineError != null) { updateStatus(); return; }

        final String requestGameId = gameId;
        final String requestId = UUID.randomUUID().toString();
        activeAiRequestId = requestId;
        aiThinking = true;
        clearSelection();
        updateStatus();

        aiWorker = new SwingWorker<Move, Void>() {
            @Override
            protected Move doInBackground() throws Exception {
                return chessEngine.getBestMove(board, board.getSideToMove(), aiDifficulty, requestGameId);
            }

            @Override
            protected void done() {
                if (!isCurrentAiRequest(requestGameId, requestId)) { return; }
                try {
                    final Move bestMove = get();
                    aiDelayTimer = new Timer(AI_VISUAL_DELAY_MILLIS, e -> applyAiMove(bestMove, requestGameId, requestId));
                    aiDelayTimer.setRepeats(false);
                    aiDelayTimer.start();
                } catch (Exception e) {
                    if (isCurrentAiRequest(requestGameId, requestId)) {
                        aiThinking = false;
                        engineError = "Computer move failed. Start a new game to try again.";
                        updateStatus();
                    }
                }
            }
        };
        aiWorker.execute();
    }

    private void applyAiMove(Move bestMove, String requestGameId, String requestId) {
        if (!isCurrentAiRequest(requestGameId, requestId)) { return; }
        aiThinking = false;
        activeAiRequestId = null;
        if (bestMove == null) {
            updateGameState();
            return;
        }
        if (!moveValidator.isValidMove(board, bestMove)) {
            engineError = "Computer returned an illegal move. Start a new game to try again.";
            chessEngine.stop();
            updateStatus();
            return;
        }
        Move legalMove = findMatchingLegalMove(bestMove);
        if (legalMove == null) {
            engineError = "Computer returned an unavailable move. Start a new game to try again.";
            updateStatus();
            return;
        }
        if (legalMove.isPromotion()) { legalMove.setPromotionPiece(resolvePromotionPiece(bestMove.getPromotionPiece())); }
        applyLegalMove(legalMove, true);
    }

    private boolean isCurrentAiRequest(String requestGameId, String requestId) {
        return requestGameId.equals(gameId)
                && requestId.equals(activeAiRequestId)
                && gameMode == GameMode.HUMAN_VS_AI;
    }

    private void ensureEngineReady() {
        if (engineDisposed && chessEngine instanceof LocalChessEngineAdapter) {
            chessEngine = new LocalChessEngineAdapter();
            engineDisposed = false;
        }
        try {
            chessEngine.initialize();
            engineReady = true;
            engineDisposed = false;
            engineError = null;
        } catch (ChessEngineException e) {
            engineReady = false;
            engineError = "Computer engine failed to start: " + e.getMessage();
        }
    }

    private void cancelPendingAi() {
        if (aiDelayTimer != null) { aiDelayTimer.stop(); aiDelayTimer = null; }
        if (aiWorker != null && !aiWorker.isDone()) { aiWorker.cancel(true); }
        if (chessEngine != null) { chessEngine.stop(); }
        aiThinking = false;
        activeAiRequestId = null;
    }

    public void disposeEngine() {
        cancelPendingAi();
        if (chessEngine != null) { chessEngine.dispose(); }
        engineDisposed = true;
        engineReady = false;
    }

    @Override
    public void removeNotify() {
        disposeEngine();
        super.removeNotify();
    }

    private void invalidateGameId() { gameId = UUID.randomUUID().toString(); }

    private String withLastMoveAnnouncement(String status) {
        return lastMoveAnnouncement == null ? status : lastMoveAnnouncement + " " + status;
    }

    private void updateStatus() {
        if (statusLabel == null) { return; }
        statusLabel.getAccessibleContext().setAccessibleDescription(lastMoveAnnouncement);
        if (engineError != null) {
            statusLabel.setText(withLastMoveAnnouncement(engineError));
            return;
        }
        Color turn = board.getSideToMove();
        boolean inCheck = board.isKingInCheck(turn);
        boolean noMoves = moveGenerator.generateLegalMoves(board, turn).isEmpty();
        String side = turn == Color.WHITE ? "White" : "Black";
        if (gameOver && inCheck && noMoves) {
            statusLabel.setText(withLastMoveAnnouncement(side + " is checkmated. " + (turn.opposite() == Color.WHITE ? "White" : "Black") + " wins."));
        } else if (gameOver && noMoves) {
            statusLabel.setText(withLastMoveAnnouncement("Stalemate. Game over."));
        } else if (gameOver && board.getHalfMoveClock() >= 100) {
            statusLabel.setText(withLastMoveAnnouncement("Draw by fifty-move rule. Game over."));
        } else if (aiThinking) {
            statusLabel.setText(withLastMoveAnnouncement("Computer is thinking…"));
        } else if (gameMode == GameMode.HUMAN_VS_AI && !engineReady && board.getSideToMove() != humanColor) {
            statusLabel.setText(withLastMoveAnnouncement("Computer engine is loading…"));
        } else if (inCheck) {
            statusLabel.setText(withLastMoveAnnouncement(side + " to move - check!"));
        } else {
            statusLabel.setText(withLastMoveAnnouncement(side + " to move"));
        }
    }
}
