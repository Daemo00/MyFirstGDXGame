package com.mygdx.game.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.MainGame;

import java.util.HashMap;
import java.util.Map;


public class GameScreen implements Screen {
    private final MainGame game;
	private OrthographicCamera camera;
	private Vector3 touchPos = new Vector3();
	private Music rainMusic;
	private Sound raindropSound;

	private Texture bucketImage;
	private Rectangle bucket;

	/**
	 * Array containing the name of each raindrop
	 */
	private Array<String> raindropNames;
	/**
	 * Array containing the image of each raindrop
	 */
	private Array<Texture> raindropImages;

	/**
	 * Array containing the rectangle of each generated raindrop
	 */
	private Array<Rectangle> raindropRectangles;
    /**
     * Array containing the index (in raindropNames) of the raindrop of each generated raindrop
     */
	private Array<Integer> raindropIndexes;

    /**
     * Mapping the score of each name in raindropNames
     */
	private Map<String, Integer> dropsGathered;
	private long lastRaindropTime;

    public GameScreen(final MainGame game) {
        this.game = game;

        initBucket();
        initRaindrops();
        initEnvironment();
        spawnRaindrop();
	}

    private void initEnvironment() {
        // Load the drop sound effect and the rain background "music"
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    private void initBucket() {
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
    }

    private void initRaindrops() {
        // Load the images for the droplet and the bucket, 64x64 pixels each
        raindropNames = new Array<String>();
        raindropNames.add("Ale");
        raindropNames.add("Dino");
        raindropNames.add("Fra");
        raindropNames.add("Pili");
        raindropNames.add("Roby");
        raindropNames.add("Simo");
        raindropNames.add("Voie");

        dropsGathered = new HashMap<String, Integer>();
        for (String rainropName : raindropNames) {
            dropsGathered.put(rainropName, 0);
        }

        raindropImages = new Array<Texture>();
        raindropImages.add(new Texture(Gdx.files.internal("faces/Aleface.png")));
        raindropImages.add(new Texture(Gdx.files.internal("faces/Dinoface.png")));
        raindropImages.add(new Texture(Gdx.files.internal("faces/Fraface.png")));
        raindropImages.add(new Texture(Gdx.files.internal("faces/Piliface.png")));
        raindropImages.add(new Texture(Gdx.files.internal("faces/Robyface.png")));
        raindropImages.add(new Texture(Gdx.files.internal("faces/myface.png")));
        raindropImages.add(new Texture(Gdx.files.internal("faces/Voieface.png")));

        raindropRectangles = new Array<Rectangle>();
        raindropIndexes = new Array<Integer>();

        raindropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
    }

    @Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
        renderScore();
        renderRaindrops();
        renderBucket();
        game.batch.end();

        if(TimeUtils.nanoTime() - lastRaindropTime > 500000000)
            spawnRaindrop();

        manageUserInput();
        manageBucket();
        for (int i = 0; i < raindropRectangles.size; i++) {
            manageRaindrops(i);
		}
	}

    private void manageBucket() {
        if(bucket.x < 0) bucket.x = 0;
        if(bucket.x > 800 - 64) bucket.x = 800 - 64;
    }

    private void manageRaindrops(int i) {
        Rectangle raindrop = raindropRectangles.get(i);
        raindrop.y -= 150 * Gdx.graphics.getDeltaTime();
        if(raindrop.y + 64 < 0) {
            removeRainDrop(i, false);
        }

        if (raindrop.overlaps(bucket)) {
            removeRainDrop(i, true);
        }
    }

    private void manageUserInput() {
        if(Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucket.x -= 1000 * 0.2 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bucket.x += 1000 * 0.2 * Gdx.graphics.getDeltaTime();
    }

    private void renderRaindrops() {
        for (int i = 0; i < raindropRectangles.size; i++) {
            Rectangle raindrop = raindropRectangles.get(i);
            Integer raindropIndex = raindropIndexes.get(i);
            game.batch.draw(raindropImages.get(raindropIndex), raindrop.x, raindrop.y);
        }
    }

    private void renderBucket() {
        game.batch.draw(bucketImage, bucket.x, bucket.y);
    }

    private void renderScore() {
        float span = Gdx.graphics.getWidth() / raindropNames.size;
        float nameX = 0;
        for (String name : raindropNames) {
            game.font.draw(game.batch, name + ": " + dropsGathered.get(name), nameX, 480);
            nameX += span;
        }
    }

    private void removeRainDrop(int i, boolean countIt) {
        if (countIt){
            raindropSound.play();
            int raindropIndex = raindropIndexes.get(i);
            String raindropName = raindropNames.get(raindropIndex);
            int previousScore = dropsGathered.get(raindropName);
            dropsGathered.put(raindropName,previousScore + 1);
        }
        raindropRectangles.removeIndex(i);
        raindropIndexes.removeIndex(i);
    }

    private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindropRectangles.add(raindrop);
        raindropIndexes.add(MathUtils.random(0, raindropImages.size - 1));
		lastRaindropTime = TimeUtils.nanoTime();
	}

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
	public void dispose() {
        for (Texture dropImage : raindropImages) {
            dropImage.dispose();
        }
		bucketImage.dispose();
		raindropSound.dispose();
		rainMusic.dispose();
	}
}
