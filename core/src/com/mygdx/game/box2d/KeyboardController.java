package com.mygdx.game.box2d;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class KeyboardController implements InputProcessor {
    final Vector2 mouseLocation = new Vector2();
    public boolean right;
    public boolean left;
    public boolean up;
    boolean down;
    boolean isMouse1Down, isMouse2Down, isMouse3Down;
    private boolean isDragged;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) // switch code base on the variable keycode
        {
            case Keys.LEFT:    // if keycode is the same as Keys.LEFT a.k.a 21
                left = true;    // do this
                return true;    // we have reacted to a keypress so stop checking for more
            case Keys.RIGHT:    // if keycode is the same as Keys.LEFT a.k.a 22
                right = true;    // do this
                return true;    // we have reacted to a keypress so stop checking for more
            case Keys.UP:        // if keycode is the same as Keys.LEFT a.k.a 19
                up = true;        // do this
                return true;    // we have reacted to a keypress so stop checking for more
            case Keys.DOWN:    // if keycode is the same as Keys.LEFT a.k.a 20
                down = true;    // do this
                return true;    // we have reacted to a keypress so stop checking for more
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) // switch code base on the variable keycode
        {
            case Keys.LEFT:    // if keycode is the same as Keys.LEFT a.k.a 21
                left = false;    // do this
                return true;    // we have reacted to a keypress so stop checking for more
            case Keys.RIGHT:    // if keycode is the same as Keys.LEFT a.k.a 22
                right = false;    // do this
                return true;    // we have reacted to a keypress so stop checking for more
            case Keys.UP:        // if keycode is the same as Keys.LEFT a.k.a 19
                up = false;        // do this
                return true;    // we have reacted to a keypress so stop checking for more
            case Keys.DOWN:    // if keycode is the same as Keys.LEFT a.k.a 20
                down = false;    // do this
                return true;    // we have reacted to a keypress so stop checking for more
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            isMouse1Down = true;
        } else if (button == 1) {
            isMouse2Down = true;
        } else if (button == 2) {
            isMouse3Down = true;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;
        //System.out.println(button);
        if (button == 0) {
            isMouse1Down = false;
        } else if (button == 1) {
            isMouse2Down = false;
        } else if (button == 2) {
            isMouse3Down = false;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
