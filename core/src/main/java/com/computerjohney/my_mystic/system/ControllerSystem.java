package com.computerjohney.my_mystic.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.computerjohney.my_mystic.GdxGame;
import com.computerjohney.my_mystic.component.Controller;
import com.computerjohney.my_mystic.component.Move;
import com.computerjohney.my_mystic.input.Command;

public class ControllerSystem extends IteratingSystem {

    private final GdxGame game;

    public ControllerSystem(GdxGame game) {
        super(Family.all(Controller.class).get());
        this.game = game;
    }

    /**
     * Processes input commands for the entity, handling movement and actions.
     */
    //need release cause still works with mult keys pressed at once

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Controller controller = Controller.MAPPER.get(entity);
        if (controller.getPressedCommands().isEmpty() && controller.getReleasedCommands().isEmpty()) {
            return;
        }

        for (Command command : controller.getPressedCommands()) {
            switch (command) {
                case UP -> moveEntity(entity, 0f, 1f);
                case DOWN -> moveEntity(entity, 0f, -1f);
                case LEFT -> moveEntity(entity, -1f, 0f);
                case RIGHT -> moveEntity(entity, 1f, 0f);
                //case SELECT -> startEntityAttack(entity);
                //case CANCEL -> game.setScreen(MenuScreen.class);
            }
        }
        controller.getPressedCommands().clear();

        for (Command command : controller.getReleasedCommands()) {
            switch (command) {
                case UP -> moveEntity(entity, 0f, -1f);
                case DOWN -> moveEntity(entity, 0f, 1f);
                case LEFT -> moveEntity(entity, 1f, 0f);
                case RIGHT -> moveEntity(entity, -1f, 0f);
            }
        }
        controller.getReleasedCommands().clear();
    }

    // 2d vectors
    private void moveEntity(Entity entity, float dx, float dy) {
        Move move = Move.MAPPER.get(entity);
        if (move != null) {
            move.getDirection().x += dx;
            move.getDirection().y += dy;
        }
    }
}
