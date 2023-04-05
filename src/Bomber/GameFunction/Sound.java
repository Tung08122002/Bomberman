package Bomber.GameFunction;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Sound {
    public static void playSound(String filename) {
        try {
            String path = (System.getProperty("user.dir") + "\\src\\sound\\" + filename + ".wav");
            File file = new File(path);
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getItem() {
        playSound("beepSmall");
    }

    public static void ThemeSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        Scanner scanner = new Scanner(System.in);
        File file = new File(System.getProperty("user.dir") + "\\src\\sound\\mainMusic.wav");
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
        clip.open(inputStream);

        String response = "";
        while (!response.equals("PLAY")) {
            System.out.println("Wanna have music? On = play music, Off = stop music, Reset = play music again, Play = " +
                    "play the game");
            System.out.print("Enter your choice: ");
            response = scanner.next();
            response = response.toUpperCase();

            switch (response) {
                case "ON" -> clip.loop(Clip.LOOP_CONTINUOUSLY);
                case "OFF" -> clip.stop();
                case "RESET" -> clip.setMicrosecondPosition(0);
                case "PLAY" -> System.out.println("Enjoy the game.");
                default -> System.out.println("Not a valid response.");
            }
        }
    }
}
