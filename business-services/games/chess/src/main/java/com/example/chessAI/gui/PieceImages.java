package com.example.chessAI.gui;

import javax.swing.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class PieceImages {

    private static final String PATH = "/piece_images/";

    private static final Map<String, ImageIcon> images = new HashMap<>();

    private PieceImages() {
    }

    public static void loadImages() {

        images.put("WHITE_PAWN", load("white_pawn.png"));
        images.put("WHITE_ROOK", load("white_rook.png"));
        images.put("WHITE_KNIGHT", load("white_knight.png"));
        images.put("WHITE_BISHOP", load("white_bishop.png"));
        images.put("WHITE_QUEEN", load("white_queen.png"));
        images.put("WHITE_KING", load("white_king.png"));

        images.put("BLACK_PAWN", load("black_pawn.png"));
        images.put("BLACK_ROOK", load("black_rook.png"));
        images.put("BLACK_KNIGHT", load("black_knight.png"));
        images.put("BLACK_BISHOP", load("black_bishop.png"));
        images.put("BLACK_QUEEN", load("black_queen.png"));
        images.put("BLACK_KING", load("black_king.png"));
    }

    private static ImageIcon load(String fileName) {

        URL url = PieceImages.class.getResource(PATH + fileName);

        if (url == null) {
            System.err.println("Image not found: " + fileName);
            return null;
        }

        return new ImageIcon(url);
    }

    public static ImageIcon get(String key) {
        return images.get(key);
    }
}