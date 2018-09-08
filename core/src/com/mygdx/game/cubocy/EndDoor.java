
package com.mygdx.game.cubocy;

import com.badlogic.gdx.math.Rectangle;

public class EndDoor {
    public final Rectangle bounds = new Rectangle();

    EndDoor(float x, float y) {
        this.bounds.x = x;
        this.bounds.y = y;
        this.bounds.width = this.bounds.height = 1;
    }
}
