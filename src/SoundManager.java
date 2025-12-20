import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static Map<String, Clip> activeClips = new HashMap<>();


    public static void playBGM(String fileName) {
        try {
            stopSound("bgm");


            Clip clip = loadAudio(fileName);
            setVolume(clip, -25.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();


            // Track clip ini
            activeClips.put("bgm", clip);


            System.out.println("🎵 BGM playing...");
        } catch (Exception e) {
            System.err.println("❌ Error playing BGM: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playSound(String fileName) {
        try {
            String key = fileName.replace(".wav", "");

            stopSound(key);

            Clip clip = loadAudio(fileName);


            // Adjust volume
            if (fileName.equals("click.wav")) {
                setVolume(clip, -18.0f);
            } else if (fileName.equals("findingpath.wav")) {
                setVolume(clip, -15.0f);
            } else {
                setVolume(clip, -12.0f);
            }


            clip.start();

            activeClips.put(key, clip);


        } catch (Exception e) {
            System.err.println("❌ Error playing sound: " + fileName);
            e.printStackTrace();
        }
    }

    public static void playSoundLoop(String fileName) {
        try {
            String key = fileName.replace(".wav", "");


            stopSound(key);
            Clip clip = loadAudio(fileName);

            if (fileName.equals("findingpath.wav")) {
                setVolume(clip, -15.0f);
            } else {
                setVolume(clip, -12.0f);
            }

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            activeClips.put(key, clip);


            System.out.println("🔊 " + fileName + " looping...");


        } catch (Exception e) {
            System.err.println("❌ Error playing looped sound: " + fileName);
            e.printStackTrace();
        }
    }


    public static void stopSound(String key) {
        Clip clip = activeClips.get(key);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            activeClips.remove(key);
        }
    }

    public static boolean isSoundPlaying(String key) {
        Clip clip = activeClips.get(key);
        return clip != null && clip.isRunning();
    }


    private static Clip loadAudio(String fileName) throws Exception {
        InputStream audioSrc = SoundManager.class.getResourceAsStream("/" + fileName);
        if (audioSrc == null) {
            throw new RuntimeException("File not found: " + fileName);
        }
        InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        return clip;
    }

    private static void setVolume(Clip clip, float decibels) {
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);


            // Clamp value dalam range yang valid
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            float clampedValue = Math.max(min, Math.min(max, decibels));


            gainControl.setValue(clampedValue);
        } catch (Exception e) {
            System.err.println("⚠️ Cannot set volume: " + e.getMessage());
        }
    }

    public static void cleanup() {
        for (Clip clip : activeClips.values()) {
            if (clip != null) {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close();
            }
        }
        activeClips.clear();
        System.out.println("🔇 All sounds cleaned up");
    }
}
