
package com.mygdx.game.cubocy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;
import com.mygdx.game.cubocy.Map;
import com.mygdx.game.cubocy.MapRenderer;
import com.mygdx.game.cubocy.OnscreenControlRenderer;

public class GameScreen extends CubocScreen {
    private Map map;
    private MapRenderer renderer;
    private OnscreenControlRenderer controlRenderer;

    GameScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, mainMenuScreen);
    }

    @Override
    public void show() {
        super.show();
        map = new Map();
        renderer = new MapRenderer(map);
        controlRenderer = new OnscreenControlRenderer(map);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
        map.update(delta);
        renderer.render(delta);
        controlRenderer.render();

        if (map.bob.bounds.overlaps(map.endDoor.bounds)) {
            game.setScreen(new GameOverScreen(game, mainMenuScreen));
        }

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            game.setScreen(new CubocyMenu(game, mainMenuScreen));
        }
    }

    @Override
    public void hide() {
        Gdx.app.debug("Cubocy", "dispose game screen");
        renderer.dispose();
        controlRenderer.dispose();
    }
}
