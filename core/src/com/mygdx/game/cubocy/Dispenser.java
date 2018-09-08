
package com.mygdx.game.cubocy;

import com.badlogic.gdx.math.Rectangle;

class Dispenser {
    final Rectangle bounds = new Rectangle();
    boolean active = false;

    Dispenser(float x, float y) {
        bounds.x = x;
        bounds.y = y;
        bounds.width = bounds.height = 1;
    }
}
