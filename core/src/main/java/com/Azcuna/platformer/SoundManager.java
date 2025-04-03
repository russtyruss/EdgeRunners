package com.Azcuna.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

public class SoundManager {
    private static Sound hoverSound;
    private static Music backgroundMusic;
    private static boolean soundLoaded = false;
    private static boolean musicLoaded = false;

    public static void load() {
        if (!soundLoaded) {
            hoverSound = Gdx.audio.newSound(Gdx.files.internal("menu/sound/hover_soundfx.mp3"));
            soundLoaded = true;
        }
    }

    public static void playMenuSoundtrack() {
        if (!musicLoaded) {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("menu/sound/Cyberpunk_Edgerunners_OST.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.1f); // Set volume to 50%
            musicLoaded = true;
        }

        // Add a delay before playing the music (e.g., 1.5 seconds)
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
                    backgroundMusic.play();
                }
            }
        }, 1.5f); // Delay of 1.5 seconds
    }

    public static void stopMenuSoundtrack() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    public static void playHoverSound() {
        if (soundLoaded) {
            hoverSound.play(0.3f); // Play at 30% volume
        }
    }

    public static void dispose() {
        if (soundLoaded) {
            hoverSound.dispose();
            soundLoaded = false;
        }
        if (musicLoaded && backgroundMusic != null) {
            backgroundMusic.dispose();
            musicLoaded = false;
        }
    }
}
