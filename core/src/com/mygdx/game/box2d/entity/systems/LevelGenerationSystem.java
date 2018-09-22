package com.mygdx.game.box2d.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.box2d.LevelFactory;
import com.mygdx.game.box2d.entity.components.PlayerComponent;
import com.mygdx.game.box2d.entity.components.TransformComponent;

public class LevelGenerationSystem extends IteratingSystem {

    // get transform component so we can check players height
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);
    private LevelFactory lf;

    public LevelGenerationSystem(LevelFactory lvlFactory) {
        super(Family.all(PlayerComponent.class).get());
        lf = lvlFactory;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent trans = tm.get(entity);
        int currentPosition = (int) trans.position.y;
        if ((currentPosition + 7) > lf.currentLevel) {
            lf.generateLevel(currentPosition + 7);
        }
    }
}