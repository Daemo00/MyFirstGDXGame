
package com.mygdx.game.cubocy.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.cubocy.Map;
import com.mygdx.game.cubocy.MapRenderer;
import com.mygdx.game.cubocy.OnscreenControlRenderer;

public class GameScreen extends CubocScreen {
    private Map map;
    private MapRenderer renderer;
    private OnscreenControlRenderer controlRenderer;

    GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        map = new Map();
        renderer = new MapRenderer(map);
        controlRenderer = new OnscreenControlRenderer(map);
    }

    @Override
    public void render(float delta) {
        delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
        map.update(delta);
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render(delta);
        controlRenderer.render();

        if (map.bob.bounds.overlaps(map.endDoor.bounds)) {
            game.setScreen(new GameOverScreen(game));
        }

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            game.setScreen(new CubocyMenu(game));
        }
    }

    @Override
    public void hide() {
        Gdx.app.debug("Cubocy", "dispose game screen");
        renderer.dispose();
        controlRenderer.dispose();
    }
}
