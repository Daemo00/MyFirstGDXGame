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
import com.badlogic.gdx.math.Vector3;

class Shot extends ModelInstance {
    private final static Vector3 tmpV = new Vector3();
    public final boolean isInvaderShot;
    public boolean hasLeftField = false;

    Shot(Model model, Vector3 position, boolean isInvaderShot) {
        super(model, position);
        this.isInvaderShot = isInvaderShot;
    }

    public void update(float delta) {
        float SHOT_VELOCITY = 10;
        if (isInvaderShot)
            transform.trn(0, 0, SHOT_VELOCITY * delta);
        else
            transform.trn(0, 0, -SHOT_VELOCITY * delta);

        transform.getTranslation(tmpV);
        if (tmpV.z > Simulation.PLAYFIELD_MAX_Z) hasLeftField = true;
        if (tmpV.z < Simulation.PLAYFIELD_MIN_Z) hasLeftField = true;
    }
}
