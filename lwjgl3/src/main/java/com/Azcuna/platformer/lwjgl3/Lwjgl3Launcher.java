package com.Azcuna.platformer.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.Azcuna.platformer.PlatformerGame;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();

    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new PlatformerGame(), getDefaultConfiguration());

    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("PlatformerGame");

        // Enable VSync
        configuration.useVsync(true);

        // Set FPS limit based on monitor refresh rate
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);

        // Set window size
        configuration.setWindowedMode(1600, 900);

        // **Make the window unresizable**
        configuration.setResizable(false);

        // Set window icons
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        return configuration;
    }
}
