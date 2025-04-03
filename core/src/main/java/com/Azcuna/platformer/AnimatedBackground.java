package com.Azcuna.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class AnimatedBackground extends Image {
    private Array<Texture> frames;
    private float frameDuration;
    private float stateTime = 0;
    private boolean looping;

    public AnimatedBackground(Array<Texture> frames, float frameDuration, boolean looping) {
        super(new TextureRegionDrawable(new TextureRegion(frames.first())));
        this.frames = frames;
        this.frameDuration = frameDuration;
        this.looping = looping;
        this.setFillParent(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        int frameNumber = (int)(stateTime / frameDuration);
        if (looping) {
            frameNumber = frameNumber % frames.size;
        } else if (frameNumber >= frames.size) {
            frameNumber = frames.size - 1;
        }

        ((TextureRegionDrawable)getDrawable()).setRegion(new TextureRegion(frames.get(frameNumber)));
    }

    public void dispose() {
        for (Texture frame : frames) {
            frame.dispose();
        }
    }
}
