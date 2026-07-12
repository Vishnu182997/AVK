package com.example.chessAI.ai;


import java.util.HashMap;
import java.util.Map;



/**
 * Stores evaluated positions.
 */
public class TranspositionTable {


    private Map<Long, TTEntry> table;



    public TranspositionTable() {

        table =
                new HashMap<Long, TTEntry>();
    }



    /**
     * Store position.
     */
    public void put(
            long hash,
            int depth,
            int score,
            MoveFlag flag) {


        TTEntry entry =
                new TTEntry(
                        hash,
                        depth,
                        score,
                        flag
                );


        table.put(hash, entry);
    }




    /**
     * Retrieve position.
     */
    public TTEntry get(long hash) {

        return table.get(hash);
    }




    /**
     * Check position exists.
     */
    public boolean contains(long hash) {

        return table.containsKey(hash);
    }



    /**
     * Clear table.
     */
    public void clear() {

        table.clear();
    }



    /**
     * Number of stored positions.
     */
    public int size() {

        return table.size();
    }
}