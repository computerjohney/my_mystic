package com.computerjohney.my_mystic.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.computerjohney.my_mystic.component.Animation2D;
import com.computerjohney.my_mystic.component.Animation2D.AnimationType;
import com.computerjohney.my_mystic.component.Fsm;
import com.computerjohney.my_mystic.component.Move;

public enum AnimationState implements State<Entity>  {
    IDLE {
        @Override
        public void enter(Entity entity) {
            Animation2D.MAPPER.get(entity).setType(Animation2D.AnimationType.IDLE);
        }

        @Override
        public void update(Entity entity) {
            Move move = Move.MAPPER.get(entity);
            if (move != null && !move.isRooted() && !move.getDirection().isZero()) {
                Fsm.MAPPER.get(entity).getAnimationFsm().changeState(WALK);
                return;
            }

//            Attack attack = Attack.MAPPER.get(entity);
//            if (attack != null && attack.isAttacking()) {
//                Fsm.MAPPER.get(entity).getAnimationFsm().changeState(ATTACK);
//                return;
//            }
//
//            Damaged damaged = Damaged.MAPPER.get(entity);
//            if (damaged != null) {
//                Fsm.MAPPER.get(entity).getAnimationFsm().changeState(DAMAGED);
//            }
        }

        @Override
        public void exit(Entity entity) {
        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    },

    WALK {
        @Override
        public void enter(Entity entity) {
            Animation2D.MAPPER.get(entity).setType(AnimationType.WALK);
        }

        @Override
        public void update(Entity entity) {
            Move move = Move.MAPPER.get(entity);
            if (move.getDirection().isZero() || move.isRooted()) {
                Fsm.MAPPER.get(entity).getAnimationFsm().changeState(IDLE);
            }
        }

        @Override
        public void exit(Entity entity) {
        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    }

//    ATTACK {
//        @Override
//        public void enter(Entity entity) {
//            Animation2D.MAPPER.get(entity).setType(AnimationType.ATTACK);
//        }
//
//        @Override
//        public void update(Entity entity) {
//            Attack attack = Attack.MAPPER.get(entity);
//            if (attack.canAttack()) {
//                Fsm.MAPPER.get(entity).getAnimationFsm().changeState(IDLE);
//            }
//        }
//
//        @Override
//        public void exit(Entity entity) {
//        }
//
//        @Override
//        public boolean onMessage(Entity entity, Telegram telegram) {
//            return false;
//        }
//    },
//
//    DAMAGED {
//        @Override
//        public void enter(Entity entity) {
//            Animation2D.MAPPER.get(entity).setType(AnimationType.DAMAGED);
//        }
//
//        @Override
//        public void update(Entity entity) {
//            Animation2D animation2D = Animation2D.MAPPER.get(entity);
//            if (animation2D.isFinished()) {
//                Fsm.MAPPER.get(entity).getAnimationFsm().changeState(IDLE);
//            }
//        }
//
//        @Override
//        public void exit(Entity entity) {
//        }
//
//        @Override
//        public boolean onMessage(Entity entity, Telegram telegram) {
//            return false;
//        }
//    }
}
