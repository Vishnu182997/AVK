package com.example.chessAI.ai;

public enum AiDifficulty {
    EASY("Easy", 1, 150, 2),
    MEDIUM("Medium", 2, 500, 6),
    HARD("Hard", 3, 1000, 12),
    EXPERT("Expert", 4, 2000, 20);

    private final String label;
    private final int searchDepth;
    private final int moveTimeMillis;
    private final int skillLevel;

    AiDifficulty(String label, int searchDepth, int moveTimeMillis, int skillLevel) {
        this.label = label;
        this.searchDepth = searchDepth;
        this.moveTimeMillis = moveTimeMillis;
        this.skillLevel = skillLevel;
    }

    public String getLabel() { return label; }
    public int getSearchDepth() { return searchDepth; }
    public int getMoveTimeMillis() { return moveTimeMillis; }
    public int getSkillLevel() { return skillLevel; }

    @Override
    public String toString() { return label; }
}
