package com.mygdx.game.box2d;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;

public class Box2DLoadingScreen extends GenericGameScreen {
    private static final int FONT = 0;
    private static final int PARTY = 1;
    private static final int SOUND = 2;
    private static final int MUSIC = 3;
    private final TextureAtlas atlas;
    private final TextureAtlas.AtlasRegion gameTitle;
    private final TextureAtlas.AtlasRegion dash;
    private final SpriteBatch sb;
    private final Animation<TextureRegion> flameAnimation;
    private Box2DAssetManager assetManager = new Box2DAssetManager();
    private int currentLoadingStage;
    private float countDown = 5;
    private float stateTime;

    public Box2DLoadingScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Box2D Loading", mainMenuScreen);
        // load loading images and wait until finished
        assetManager.queueAddLoadingImages();
        assetManager.manager.finishLoading();

        // get images used to display loading progress
        atlas = assetManager.manager.get(assetManager.loadingImages);
        gameTitle = atlas.findRegion("staying-alight-logo");
        dash = atlas.findRegion("loading-dash");
        flameAnimation = new Animation(0.07f, atlas.findRegions("flames/flames"), Animation.PlayMode.LOOP);  //new
        // initiate queueing of images but don't start loading
        assetManager.queueAddGameImages();
        System.out.println("Loading images....");
        sb = new SpriteBatch();
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    }

    @Override
    public void show() {
        super.show();
        stateTime = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // start SpriteBatch and draw the logo
        sb.begin();
        stateTime += delta; // Accumulate elapsed animation time  
        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = flameAnimation.getKeyFrame(stateTime, true);

        drawLoadingBar(currentLoadingStage * 2, currentFrame);
        sb.draw(gameTitle, 135, 250);
        sb.end();

        // check if the asset manager has finished loading
        if (assetManager.manager.update()) { // Load some, will return true if done loading
            currentLoadingStage += 1;
            switch (currentLoadingStage) {
                case FONT:
                    System.out.println("Loading fonts....");
                    assetManager.queueAddFonts(); // first load done, now start fonts
                    break;
                case PARTY:
                    System.out.println("Loading Particle Effects....");
                    assetManager.queueAddParticleEffects(); // fonts are done now do party effects
                    break;
                case SOUND:
                    System.out.println("Loading Sounds....");
                    assetManager.queueAddSounds();
                    break;
                case MUSIC:
                    System.out.println("Loading music....");
                    assetManager.queueAddMusic();
                    break;
                case 5:
                    System.out.println("Finished"); // all done
                    break;
            }
            if (currentLoadingStage > 5) {
                countDown -= delta;  // timer to stay on loading screen for short preiod once done loading
                currentLoadingStage = 5;  // cap loading stage to 5 as will use later to display progress bar anbd more than 5 would go off the screen
                if (countDown < 0) { // countdown is complete
                    game.setScreen(new Box2DScreen(game, mainMenuScreen, assetManager));  /// go to menu screen
                }
            }
        }
    }

    private void drawLoadingBar(int stage, TextureRegion currentFrame) {
        for (int i = 0; i < stage; i++) {
            sb.draw(currentFrame, 50 + (i * 50), 150, 50, 50);
            sb.draw(dash, 35 + (i * 50), 140, 80, 80);
        }
    }
}
