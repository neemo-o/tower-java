package util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Sound {
    private static final Map<String, Clip> activeClips = new ConcurrentHashMap<>();
    private static final Map<String, Clip> loopClips = new ConcurrentHashMap<>();
    private static float globalVolume = 1.0f;
    private static boolean soundEnabled = true;
    

    public Clip play(String fileName, float volume) {
        if (!soundEnabled) return null;
        
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    getClass().getResourceAsStream("/assets/" + fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            setVolume(clip, volume * globalVolume);
            

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    activeClips.remove(fileName);
                    clip.close();
                }
            });
            
            activeClips.put(fileName, clip);
            clip.start();
            return clip;
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Erro ao reproduzir som: " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    public Clip playLoop(String fileName, float volume) {
        if (!soundEnabled) return null;
        
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    getClass().getResourceAsStream("/assets/" + fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            setVolume(clip, volume * globalVolume);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            loopClips.put(fileName, clip);
            return clip;
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Erro ao reproduzir som em loop: " + fileName);
            e.printStackTrace();
            return null;
        }
    }
    

    public void stop(String fileName) {
        Clip clip = activeClips.get(fileName);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            activeClips.remove(fileName);
        }
        
        Clip loopClip = loopClips.get(fileName);
        if (loopClip != null && loopClip.isRunning()) {
            loopClip.stop();
            loopClip.close();
            loopClips.remove(fileName);
        }
    }
    

    public void stop(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
    

    public void stopAllSounds() {
        // Para todos os sons normais
        for (Clip clip : activeClips.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
        activeClips.clear();
        

        for (Clip clip : loopClips.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
        loopClips.clear();
    }

    public void stopAllLoops() {
        for (Clip clip : loopClips.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
        loopClips.clear();
    }

    public void pause(String fileName) {
        Clip clip = activeClips.get(fileName);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
        
        Clip loopClip = loopClips.get(fileName);
        if (loopClip != null && loopClip.isRunning()) {
            loopClip.stop();
        }
    }
    

    public void resume(String fileName) {
        Clip clip = activeClips.get(fileName);
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
        
        Clip loopClip = loopClips.get(fileName);
        if (loopClip != null && !loopClip.isRunning()) {
            loopClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setGlobalVolume(float volume) {
        globalVolume = Math.max(0.0f, Math.min(1.0f, volume));
        

        for (Clip clip : activeClips.values()) {
            if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                setVolume(clip, globalVolume);
            }
        }
        
        for (Clip clip : loopClips.values()) {
            if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                setVolume(clip, globalVolume);
            }
        }
    }
    

    public void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
        if (!enabled) {
            stopAllSounds();
        }
    }
    

    public boolean isPlaying(String fileName) {
        Clip clip = activeClips.get(fileName);
        Clip loopClip = loopClips.get(fileName);
        return (clip != null && clip.isRunning()) || (loopClip != null && loopClip.isRunning());
    }

    public int getActiveSoundsCount() {
        return activeClips.size() + loopClips.size();
    }
    

    private void setVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float gain = min + (max - min) * volume;
            volumeControl.setValue(gain);
        }
    }
    

    public void cleanup() {
        stopAllSounds();
    }
}