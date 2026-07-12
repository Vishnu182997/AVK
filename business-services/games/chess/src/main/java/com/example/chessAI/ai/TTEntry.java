package com.example.chessAI.ai;


public class TTEntry {


    public long hash;

    public int depth;

    public int score;

    public MoveFlag flag;



    public TTEntry(
            long hash,
            int depth,
            int score,
            MoveFlag flag) {


        this.hash = hash;
        this.depth = depth;
        this.score = score;
        this.flag = flag;
    }
}