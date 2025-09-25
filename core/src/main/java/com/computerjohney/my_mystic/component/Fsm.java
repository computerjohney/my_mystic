package com.computerjohney.my_mystic.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.computerjohney.my_mystic.ai.AnimationState;


// Finite State Machine
public class Fsm implements Component {
    public static final ComponentMapper<Fsm> MAPPER = ComponentMapper.getFor(Fsm.class);

    private final DefaultStateMachine<Entity, AnimationState> animationFsm;

    public Fsm(Entity owner) {
        this.animationFsm = new DefaultStateMachine<>(owner, AnimationState.IDLE);
    }

    public DefaultStateMachine<Entity, AnimationState> getAnimationFsm() {
        return animationFsm;
    }
}
