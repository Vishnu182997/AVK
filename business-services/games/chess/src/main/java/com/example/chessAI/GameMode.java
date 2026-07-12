package com.example.chessAI;

public enum GameMode {
    HUMAN_VS_HUMAN("Human vs Human"),
    HUMAN_VS_AI("Human vs Computer");

    private final String label;

    GameMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
