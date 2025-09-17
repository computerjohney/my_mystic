package com.computerjohney.my_mystic.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.computerjohney.my_mystic.component.Controller;

public class GameControllerState implements ControllerState{

   // Using Ashley so...
    // for eg. could give enemy controller components
    private final ImmutableArray<Entity> controllerEntities;

    public GameControllerState(Engine engine) {
        this.controllerEntities = engine.getEntitiesFor(Family.all(Controller.class).get());
    }


    @Override
    public void keyDown(Command command) {
        for (Entity entity : controllerEntities) {
            Controller.MAPPER.get(entity).getPressedCommands().add(command);
        }
    }

    @Override
    public void keyUp(Command command) {
        for (Entity entity : controllerEntities) {
            Controller.MAPPER.get(entity).getReleasedCommands().add(command);
        }
    }
}
