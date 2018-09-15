package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

class PreferencesScreen extends GenericGameScreen {

    PreferencesScreen(final MainGame game, MainMenuScreen mainMenuScreen) {
        super(game, "Preferences", mainMenuScreen);

        Label titleLabel = new Label("Preferences", game.skin);
        titleLabel.setStyle(mainMenuScreen.labelStyle);
        titleLabel.setFontScale(2);
        Label volumeMusicLabel = new Label("Music volume", game.skin);
        final CheckBox musicCheckbox = new CheckBox(null, game.skin);
        musicCheckbox.setChecked(game.getPreferences().isMusicEnabled());
        musicCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferences().setMusicEnabled(enabled);
                return false;
            }
        });
        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.1f, false, game.skin);
        volumeMusicSlider.setValue(game.getPreferences().getMusicVolume());
        volumeMusicSlider.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
                game.getPreferences().setMusicVolume(volumeMusicSlider.getValue());
            }
        });
        //sound
        final Slider soundMusicSlider = new Slider(0f, 1f, 0.1f, false, game.skin);
        soundMusicSlider.setValue(game.getPreferences().getSoundVolume());
        soundMusicSlider.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
                game.getPreferences().setSoundVolume(soundMusicSlider.getValue());
            }
        });
        Label volumeSoundLabel = new Label("Sound volume", game.skin);
        //sound
        final CheckBox soundCheckbox = new CheckBox(null, game.skin);
        soundCheckbox.setChecked(game.getPreferences().isSoundEffectsEnabled());
        soundCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundCheckbox.isChecked();
                game.getPreferences().setSoundEffectsEnabled(enabled);
                return false;
            }
        });
        Label musicOnOffLabel = new Label("Music active", game.skin);
        Label soundOnOffLabel = new Label("Sound effects active", game.skin);


        Table table = new Table();
        table.setFillParent(true);
        table.add(titleLabel).colspan(2).pad(10);
        table.row().pad(5);
        table.add(volumeMusicLabel);
        table.add(volumeMusicSlider);
        table.row().pad(5);
        table.add(musicOnOffLabel);
        table.add(musicCheckbox);
        table.row().pad(5);
        table.add(volumeSoundLabel);
        table.add(soundMusicSlider);
        table.row().pad(5);
        table.add(soundOnOffLabel);
        table.add(soundCheckbox);
        gameStage.addActor(table);
    }
}
