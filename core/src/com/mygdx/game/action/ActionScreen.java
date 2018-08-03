package com.mygdx.game.action;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class ActionScreen implements Screen {
    private Stage stage;

    public ActionScreen(){
        stage = new Stage(new ScreenViewport());
        Texture texture = new Texture(Gdx.files.absolute("faces/Dinoface.png"));

        int X_left= Gdx.graphics.getWidth()/3-texture.getWidth()/2;
        int X_right = Gdx.graphics.getWidth()*2/3-texture.getWidth()/2;
        int Y_top = Gdx.graphics.getHeight()*2/3-texture.getHeight()/2;
        int Y_bottom = Gdx.graphics.getHeight()/3-texture.getHeight()/2;

        Image image1 = new Image(texture);
        image1.setPosition(X_left,Y_top);
        image1.setOrigin(image1.getWidth()/2,image1.getHeight()/2);
        stage.addActor(image1);

        ParallelAction topLeftRightParallelAction = new ParallelAction();
        topLeftRightParallelAction.addAction(Actions.moveTo(X_right,Y_top,1, Interpolation.exp5Out));
        topLeftRightParallelAction.addAction(Actions.scaleTo(2,2,1,Interpolation.exp5Out));

        MoveToAction moveBottomRightAction = new MoveToAction();
        moveBottomRightAction.setPosition(X_right,Y_bottom);
        moveBottomRightAction.setDuration(1);
        moveBottomRightAction.setInterpolation(Interpolation.smooth);

        ParallelAction bottomLeftRightParallelAction = new ParallelAction();
        bottomLeftRightParallelAction.addAction(Actions.moveTo(X_left,Y_bottom,1,Interpolation.sineOut));
        bottomLeftRightParallelAction.addAction(Actions.scaleTo(1,1,1));

        ParallelAction leftBottomTopParallelAction = new ParallelAction();
        leftBottomTopParallelAction.addAction(Actions.moveTo(X_left,Y_top,1,Interpolation.swingOut));
        leftBottomTopParallelAction.addAction(Actions.rotateBy(90,1));

        SequenceAction overallSequence = new SequenceAction();
        overallSequence.addAction(topLeftRightParallelAction);
        overallSequence.addAction(moveBottomRightAction);
        overallSequence.addAction(bottomLeftRightParallelAction);
        overallSequence.addAction(leftBottomTopParallelAction);

        RepeatAction infiniteLoop = new RepeatAction();
        infiniteLoop.setCount(RepeatAction.FOREVER);
        infiniteLoop.setAction(overallSequence);
        image1.addAction(infiniteLoop);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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

    }
}
