package com.mygdx.game.box2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;
import com.mygdx.game.box2d.actors.LoadingBarPart;

public class Box2DLoadingScreen extends GenericGameScreen {
    private static final int FONT = 0;
    private static final int PARTY = 1;
    private static final int SOUND = 2;
    private static final int MUSIC = 3;
    private final Stage stage;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion gameTitle;
    private TextureAtlas.AtlasRegion dash;
    private Animation<TextureRegion> flameAnimation;
    private Box2DAssetManager assetManager = new Box2DAssetManager();
    private int currentLoadingStage;
    private float countDown = 5;
    private float stateTime;
    private Image titleImage;
    private Table loadingTable;
    private Table table;
    private TextureAtlas.AtlasRegion copyright;
    private TextureAtlas.AtlasRegion background;

    public Box2DLoadingScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Box2D Loading", mainMenuScreen);
        stage = new Stage(new ScreenViewport());

        loadAssets();
        // initiate queueing of images but don't start loading
        assetManager.queueAddGameImages();
        System.out.println("Loading images....");
    }

    private void loadAssets() {
        assetManager.queueAddLoadingImages();
        assetManager.manager.finishLoading();

        // get images used to display loading progress
        atlas = assetManager.manager.get(assetManager.loadingImages);
        gameTitle = atlas.findRegion("staying-alight-logo");
        dash = atlas.findRegion("loading-dash");
        flameAnimation = new Animation(0.07f, atlas.findRegions("flames/flames"), Animation.PlayMode.LOOP);  //new
        background = atlas.findRegion("flamebackground");
        copyright = atlas.findRegion("copyright");
    }
    @Override
    public void show() {
        super.show();
        titleImage = new Image(gameTitle);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        table.setBackground(new TiledDrawable(background));

        loadingTable = new Table();
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));
        loadingTable.add(new LoadingBarPart(dash, flameAnimation));


        table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(10);
        table.row(); // move to next row
        table.add(loadingTable).width(400);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (assetManager.manager.update()) { // Load some, will return true if done loading
            currentLoadingStage += 1;
            if (currentLoadingStage <= 5) {
                loadingTable.getCells().get((currentLoadingStage - 1) * 2).getActor().setVisible(true);  // new
                loadingTable.getCells().get((currentLoadingStage - 1) * 2 + 1).getActor().setVisible(true);
            }
            switch (currentLoadingStage) {
                case FONT:
                    System.out.println("Loading fonts....");
                    assetManager.queueAddFonts();
                    break;
                case PARTY:
                    System.out.println("Loading Particle Effects....");
                    assetManager.queueAddParticleEffects();
                    break;
                case SOUND:
                    System.out.println("Loading Sounds....");
                    assetManager.queueAddSounds();
                    break;
                case MUSIC:
                    System.out.println("Loading fonts....");
                    assetManager.queueAddMusic();
                    break;
                case 5:
                    System.out.println("Finished");
                    break;
            }
            if (currentLoadingStage > 5) {
                countDown -= delta;
                currentLoadingStage = 5;
                if (countDown < 0) {
                    game.setScreen(new Box2DScreen(game, mainMenuScreen, assetManager));
                }
            }
        }

        stage.act();
        stage.draw();
    }
}
