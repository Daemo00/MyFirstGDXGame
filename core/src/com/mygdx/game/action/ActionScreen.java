package com.mygdx.game.action;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GenericGameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MainMenuScreen;

public class ActionScreen extends GenericGameScreen {
    private final Group lapsLabelContainer;
    private final Label lapsLabel;
    private static int lapsCount;

    public ActionScreen(MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Actions", mainMenuScreen);

        lapsCount = 3;
        Texture texture = new Texture(Gdx.files.internal("faces/Dinoface.png"));

        int X_left = Gdx.graphics.getWidth() / 3 - texture.getWidth() / 2;
        int X_right = Gdx.graphics.getWidth() * 2 / 3 - texture.getWidth() / 2;
        int Y_top = Gdx.graphics.getHeight() * 2 / 3 - texture.getHeight() / 2;
        int Y_bottom = Gdx.graphics.getHeight() / 3 - texture.getHeight() / 2;

        Image image1 = new Image(texture);
        image1.setPosition(X_left, Y_top);
        image1.setOrigin(image1.getWidth() / 2, image1.getHeight() / 2);
        gameStage.addActor(image1);

        ParallelAction topLeftRightParallelAction = new ParallelAction();
        topLeftRightParallelAction.addAction(Actions.moveTo(X_right, Y_top, 1, Interpolation.exp5Out));
        topLeftRightParallelAction.addAction(Actions.scaleTo(2, 2, 1, Interpolation.exp5Out));

        MoveToAction moveBottomRightAction = new MoveToAction();
        moveBottomRightAction.setPosition(X_right, Y_bottom);
        moveBottomRightAction.setDuration(1);
        moveBottomRightAction.setInterpolation(Interpolation.smooth);

        ParallelAction bottomLeftRightParallelAction = new ParallelAction();
        bottomLeftRightParallelAction.addAction(Actions.moveTo(X_left, Y_bottom, 1, Interpolation.sineOut));
        bottomLeftRightParallelAction.addAction(Actions.scaleTo(1, 1, 1));

        ParallelAction leftBottomTopParallelAction = new ParallelAction();
        leftBottomTopParallelAction.addAction(Actions.moveTo(X_left, Y_top, 1, Interpolation.swingOut));
        leftBottomTopParallelAction.addAction(Actions.rotateBy(90, 1));

        RunnableAction updateLapCountAction = new RunnableAction();
        updateLapCountAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                updateLapsCount();
            }
        });

        SequenceAction overallSequence = new SequenceAction();
        overallSequence.addAction(updateLapCountAction);
        overallSequence.addAction(topLeftRightParallelAction);
        overallSequence.addAction(moveBottomRightAction);
        overallSequence.addAction(bottomLeftRightParallelAction);
        overallSequence.addAction(leftBottomTopParallelAction);

        RepeatAction loopNbrAction = new RepeatAction();
        loopNbrAction.setCount(lapsCount);
        loopNbrAction.setAction(overallSequence);

        lapsLabel = new Label(" Loop :", game.skin);
        lapsLabel.setAlignment(Align.center);

        lapsLabelContainer = new Group();
        lapsLabelContainer.addActor(lapsLabel);
        lapsLabelContainer.setPosition(X_left + (X_right - X_left) / 2, Y_top + (Y_bottom - Y_top) / 2);

        gameStage.addActor(lapsLabelContainer);

        RunnableAction completedAction = new RunnableAction();
        completedAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                finished();
            }
        });

        image1.addAction(Actions.sequence(loopNbrAction, completedAction));

    }

    private void updateLapsCount() {
        lapsCount--;
        lapsLabelContainer.setScale(0);
        SequenceAction FadingSequenceAction = new SequenceAction();
        FadingSequenceAction.addAction(Actions.fadeIn(1));
        FadingSequenceAction.addAction(Actions.fadeOut(2));

        ParallelAction parallelAction = new ParallelAction();

        lapsLabel.setText("Laps : " + (lapsCount + 1));
        parallelAction.addAction(Actions.scaleTo(5, 5, 4));
        parallelAction.addAction(FadingSequenceAction);
        lapsLabelContainer.addAction(parallelAction);
    }

    private void finished() {
        lapsLabelContainer.setScale(0);
        SequenceAction FadingSequenceAction = new SequenceAction();
        FadingSequenceAction.addAction(Actions.fadeIn(1));

        ParallelAction parallelAction = new ParallelAction();

        lapsLabel.setText("Finished!");
        parallelAction.addAction(Actions.rotateBy(360, 3));
        parallelAction.addAction(Actions.scaleBy(5, 5, 4, Interpolation.bounceOut));
        parallelAction.addAction(FadingSequenceAction);
        lapsLabelContainer.addAction(parallelAction);
    }

    @Override
    public void show() {
        super.show();
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
        super.dispose();
    }
}
