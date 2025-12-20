import javax.sound.sampled.*;
import java.io.File;
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

            activeClips.put("bgm", clip);
            System.out.println("🎵 BGM playing...");
        } catch (Exception e) {
            System.err.println("❌ Error playing BGM: " + e.getMessage());
        }
    }

    public static void playSound(String fileName) {
        try {
            String key = fileName.replace(".wav", "");
            stopSound(key);

            Clip clip = loadAudio(fileName);

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
        }
    }

    public static void playSoundLoop(String fileName) {
        try {
            String key = fileName.replace(".wav", "");
            stopSound(key);

            Clip clip = loadAudio(fileName);
            setVolume(clip, -15.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            activeClips.put(key, clip);
            System.out.println("🔊 " + fileName + " looping...");
        } catch (Exception e) {
            System.err.println("❌ Error looping sound: " + fileName);
        }
    }

    public static void stopSound(String key) {
        Clip clip = activeClips.get(key);
        if (clip != null) {
            clip.stop();
            clip.close();
            activeClips.remove(key);
        }
    }

    public static boolean isSoundPlaying(String key) {
        Clip clip = activeClips.get(key);
        return clip != null && clip.isRunning();
    }

    private static Clip loadAudio(String fileName) throws Exception {
        File file = new File("sounds/" + fileName);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        return clip;
    }

    private static void setVolume(Clip clip, float decibels) {
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = gain.getMinimum();
            float max = gain.getMaximum();
            gain.setValue(Math.max(min, Math.min(max, decibels)));
        } catch (Exception ignored) {
        }
    }

    public static void cleanup() {
        for (Clip clip : activeClips.values()) {
            clip.stop();
            clip.close();
        }
        activeClips.clear();
        System.out.println("🔇 All sounds cleaned up");
    }
}
