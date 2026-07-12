package com.example.chessAI.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class SoundManager {


    public static void play(String file) {

        try {


            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(
                    SoundManager.class
                    .getResource(
                    "/sounds/" + file
                    ));



            Clip clip =
                    AudioSystem.getClip();


            clip.open(audio);

            clip.start();


        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}