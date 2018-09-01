/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.mygdx.game.invaders.simulation;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class Invader extends ModelInstance {
    private final static int STATE_MOVE_LEFT = 0;
    private final static int STATE_MOVE_DOWN = 1;
    private final static int STATE_MOVE_RIGHT = 2;
    public static float INVADER_RADIUS = 0.75f;
    public static int INVADER_POINTS = 40;
    private int state = STATE_MOVE_LEFT;
    private boolean wasLastStateLeft = true;
    private float movedDistance = Simulation.PLAYFIELD_MAX_X / 2;

    Invader(Model model, float x, float y, float z) {
        super(model, x, y, z);
    }

    public void update(float delta, float speedMultiplier) {
        float INVADER_VELOCITY = 1;
        movedDistance += delta * INVADER_VELOCITY * speedMultiplier;
        if (state == STATE_MOVE_LEFT) {
            transform.trn(-delta * INVADER_VELOCITY * speedMultiplier, 0, 0);
            if (movedDistance > Simulation.PLAYFIELD_MAX_X) {
                state = STATE_MOVE_DOWN;
                movedDistance = 0;
                wasLastStateLeft = true;
            }
        }
        if (state == STATE_MOVE_RIGHT) {
            transform.trn(delta * INVADER_VELOCITY * speedMultiplier, 0, 0);
            if (movedDistance > Simulation.PLAYFIELD_MAX_X) {
                state = STATE_MOVE_DOWN;
                movedDistance = 0;
                wasLastStateLeft = false;
            }
        }
        if (state == STATE_MOVE_DOWN) {
            transform.trn(0, 0, delta * INVADER_VELOCITY * speedMultiplier);
            if (movedDistance > 1) {
                if (wasLastStateLeft)
                    state = STATE_MOVE_RIGHT;
                else
                    state = STATE_MOVE_LEFT;
                movedDistance = 0;
            }
        }
        float INVADER_ROTATION = 45f;
        transform.rotate(1, 1, 1, INVADER_ROTATION * delta);
    }
}
