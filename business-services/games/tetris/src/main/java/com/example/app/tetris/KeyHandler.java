package com.example.app.tetris;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean leftPressed;
    public boolean rightPressed;
    public boolean downPressed;
    public boolean rotatePressed;

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;

            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;

            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;

            case KeyEvent.VK_UP:
                rotatePressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;

            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;

            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;

            case KeyEvent.VK_UP:
                rotatePressed = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}