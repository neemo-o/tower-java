package util;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
// somzinho porque ninguem merece jogo sem som né amigos e amigas?
public class Sound {
    private static final Map<String, Clip> activeClips = new ConcurrentHashMap<>();
    private static final Map<String, Clip> loopClips = new ConcurrentHashMap<>();
    private static float globalVolume = 1.0f;
    private static boolean soundEnabled = true;

    // Toca um som uma vez
    public Clip play(String fileName, float volume) {
        if (!soundEnabled)
            return null;

        try {
            InputStream is = getClass().getResourceAsStream("/assets/" + fileName);
            if (is == null) {
                System.err.println("Arquivo não encontrado: " + fileName);
                return null;
            }

            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bis);
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

    // Toca um som em loop contínuo
    public Clip playLoop(String fileName, float volume) {
        if (!soundEnabled)
            return null;

        try {
            InputStream is = getClass().getResourceAsStream("/assets/" + fileName);
            if (is == null) {
                System.err.println("Arquivo não encontrado: " + fileName);
                return null;
            }

            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bis);
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

    // Para um som específico
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

    // Para um clip específico
    public void stop(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    // Para todos os sons
    public void stopAllSounds() {
        activeClips.values().forEach(this::stop);
        activeClips.clear();

        loopClips.values().forEach(this::stop);
        loopClips.clear();
    }

    // Para apenas os loops
    public void stopAllLoops() {
        loopClips.values().forEach(this::stop);
        loopClips.clear();
    }

    // Pausa sons
    public void pause(String fileName) {
        Clip clip = activeClips.get(fileName);
        if (clip != null && clip.isRunning())
            clip.stop();

        Clip loopClip = loopClips.get(fileName);
        if (loopClip != null && loopClip.isRunning())
            loopClip.stop();
    }

    // Resume sons
    public void resume(String fileName) {
        Clip clip = activeClips.get(fileName);
        if (clip != null && !clip.isRunning())
            clip.start();

        Clip loopClip = loopClips.get(fileName);
        if (loopClip != null && !loopClip.isRunning())
            loopClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Volume global
    public void setGlobalVolume(float volume) {
        globalVolume = Math.max(0.0f, Math.min(1.0f, volume));

        activeClips.values().forEach(clip -> {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
                setVolume(clip, globalVolume);
        });

        loopClips.values().forEach(clip -> {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
                setVolume(clip, globalVolume);
        });
    }

    // Liga/desliga sons
    public void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
        if (!enabled)
            stopAllSounds();
    }

    // Checa se um som está tocando
    public boolean isPlaying(String fileName) {
        Clip clip = activeClips.get(fileName);
        Clip loopClip = loopClips.get(fileName);
        return (clip != null && clip.isRunning()) || (loopClip != null && loopClip.isRunning());
    }

    public int getActiveSoundsCount() {
        return activeClips.size() + loopClips.size();
    }

    // Ajusta volume de um clip
    private void setVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float gain = min + (max - min) * volume;
            volumeControl.setValue(gain);
        }
    }

    // Cleanup
    public void cleanup() {
        stopAllSounds();
    }
}
