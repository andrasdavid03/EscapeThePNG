package view;

import persistence.FileHandler;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final HashMap<String, Clip> sounds = new HashMap<>();

    private SoundManager() {
    }

    public static synchronized void startSound(String fileName) {
        try {
            if (sounds.containsKey(fileName) && sounds.get(fileName).isRunning()) {
                System.out.println("Sound already playing: " + fileName);
                return;
            }

            InputStream musicFile = FileHandler.getMusicFile(fileName);
            BufferedInputStream bufferedMusicFile = new BufferedInputStream(musicFile);
            AudioInputStream stream = AudioSystem.getAudioInputStream(bufferedMusicFile);
            Clip clip = AudioSystem.getClip();

            clip.open(stream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            sounds.put(fileName, clip);
            //System.out.println("Started sound: " + fileName);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file format.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error reading the audio file.");
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable.");
            throw new RuntimeException(e);
        }
    }

    public static synchronized void continueSound(String fileName) {
        if (!sounds.containsKey(fileName)) {
            //System.out.println("Sound not found, starting new: " + fileName);
            startSound(fileName);
        }

        Clip clip = sounds.get(fileName);
        if (!clip.isRunning()) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            //System.out.println("Continued sound: " + fileName);
        }
    }

    public static synchronized void resetContinueSound(String fileName) {
        if (!sounds.containsKey(fileName)) {
            //System.out.println("Sound not found, starting new: " + fileName);
            startSound(fileName);
        }

        Clip clip = sounds.get(fileName);
        clip.setFramePosition(0);
        if (!clip.isRunning()) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            //System.out.println("Continued sound: " + fileName);
        }
    }

    public static synchronized void pauseSound(String fileName) {
        if (!sounds.containsKey(fileName))
            return;

        Clip clip = sounds.get(fileName);
        if (clip.isRunning()) {
            clip.stop();
            //System.out.println("Paused sound: " + fileName);
        }
    }

    // THIS MAY BE USED IN THE FUTURE
    public static synchronized void stopSound(String fileName) {
        if (!sounds.containsKey(fileName))
            return;

        Clip clip = sounds.get(fileName);
        clip.stop();
        clip.close();
        sounds.remove(fileName);
        //System.out.println("Stopped and removed sound: " + fileName);
    }

    public static synchronized void setVolume(String fileName, float volume) {
        if (!sounds.containsKey(fileName))
            return;

        FloatControl volumeControl = (FloatControl) sounds.get(fileName).getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(volume);
        //System.out.println("Volume set for " + fileName + ": " + volume);
    }

    public static synchronized boolean isPlaying(String fileName) {
        return sounds.containsKey(fileName) && sounds.get(fileName).isRunning();
    }

    public static synchronized void stopAllSounds() {
        ArrayList<String> keysToRemove = new ArrayList<>();
        for (Map.Entry<String, Clip> sound : sounds.entrySet()) {
            Clip clip = sound.getValue();
            if (clip.isRunning())
                clip.stop();
            clip.close();

            keysToRemove.add(sound.getKey());
        }

        for (String key : keysToRemove)
            sounds.remove(key);
    }

    public static synchronized void pauseAllSounds() {
        for (Map.Entry<String, Clip> sound : sounds.entrySet()) {
            Clip clip = sound.getValue();
            if (clip.isRunning())
                clip.stop();
        }
    }

    public static void continueAllSounds() {
        for (Map.Entry<String, Clip> sound : sounds.entrySet()) {
            Clip clip = sound.getValue();
            if (!clip.isRunning()) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            }
        }
    }

    public static void playJumpscareSound(String jumpScareSound) {
        try {
            InputStream musicFile = FileHandler.getMusicFile(jumpScareSound);
            BufferedInputStream bufferedMusicFile = new BufferedInputStream(musicFile);
            AudioInputStream stream = AudioSystem.getAudioInputStream(bufferedMusicFile);
            Clip clip = AudioSystem.getClip();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.CLOSE) {
                    clip.close();
                    synchronized (clip) {
                        clip.notifyAll();
                    }
                }
            });

            clip.open(stream);
            SoundManager.setVolume(jumpScareSound, -20);
            clip.start();

            synchronized (clip) {
                while (clip.isRunning()) {
                    clip.wait();
                }
            }
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
