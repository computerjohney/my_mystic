package com.computerjohney.my_mystic.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class KeyboardController extends InputAdapter {

    private static final Map<Integer, Command> KEY_MAPPING = Map.ofEntries(
        Map.entry(Input.Keys.W, Command.UP),
        Map.entry(Input.Keys.S, Command.DOWN),
        Map.entry(Input.Keys.A, Command.LEFT),
        Map.entry(Input.Keys.D, Command.RIGHT),
        Map.entry(Input.Keys.SPACE, Command.SELECT),
        Map.entry(Input.Keys.ESCAPE, Command.CANCEL)
    );

    private final boolean[] commandState; // for holding state when change controllerState - smooth
    private final Map<Class<? extends ControllerState>, ControllerState> stateCache;
    private ControllerState activeState;

    public KeyboardController(Class<? extends ControllerState> initialState, Engine engine) {
        this.stateCache = new HashMap<>();
        this.activeState = null;
        this.commandState = new boolean[Command.values().length];

        this.stateCache.put(IdleControllerState.class, new IdleControllerState());
        this.stateCache.put(GameControllerState.class, new GameControllerState(engine));
        setActiveState(initialState);
    }

    public void setActiveState(Class<? extends ControllerState> stateClass) {
        ControllerState controllerState = stateCache.get(stateClass);
        if (controllerState == null) {
            throw new GdxRuntimeException("No state with class " + stateClass + " found in the state cache");
        }
        // iterate over commands we have avail.
        for (Command command : Command.values()) {
            // if switch to active state release active commands
            if (this.activeState != null && this.commandState[command.ordinal()]) {
                this.activeState.keyUp(command);
            }
            this.commandState[command.ordinal()] = false; // start fresh
        }

        this.activeState = controllerState;

    }

    @Override
    public boolean keyDown(int keycode) {
        Command command = KEY_MAPPING.get(keycode);
        if (command == null) return false;

        this.commandState[command.ordinal()] = true;
        this.activeState.keyDown(command);
        return true; // we handled everything
    }

    @Override
    public boolean keyUp(int keycode) {
        Command command = KEY_MAPPING.get(keycode);
        if (command == null) return false;
        if(!this.commandState[command.ordinal()]) return false; // abort
        // if it was pressed set to false..
        this.commandState[command.ordinal()] = false;
        this.activeState.keyUp(command);
        return true; // we handled everything
    }
}
