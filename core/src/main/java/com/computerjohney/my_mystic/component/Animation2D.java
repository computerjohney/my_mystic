package com.computerjohney.my_mystic.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.computerjohney.my_mystic.asset.AtlasAsset;
import com.computerjohney.my_mystic.component.Facing.FacingDirection;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

public class Animation2D implements Component {

    public static final ComponentMapper<Animation2D> MAPPER = ComponentMapper.getFor(Animation2D.class);

    private final AtlasAsset atlasAsset;
    private final String atlasKey;
    private AnimationType type;
    private Facing.FacingDirection direction;
    // libGDX animation type (usually use Normal or Loop)...
    private PlayMode playMode;
    private float speed;
    // calc. what frame to render using...
    private float stateTime;
    // didn't use sprite class (a container, keeps resetting vals)
    private Animation<TextureRegion> animation;
    // add component to entity, flag indicates change direction say, have to recalc the animation
    private boolean dirty;

    public Animation2D(AtlasAsset atlasAsset,
                       String atlasKey,
                       AnimationType type,
                       PlayMode playMode,
                       float speed) {
        this.atlasKey = atlasKey;
        this.atlasAsset = atlasAsset;
        this.type = type;
        this.direction = null;
        this.playMode = playMode;
        this.speed = speed;
        this.stateTime = 0f;
        this.animation = null;
    }

    public void setAnimation(Animation<TextureRegion> animation, FacingDirection direction) {
        this.animation = animation;
        this.direction = direction;
        this.stateTime = 0f;
        this.dirty = false;
    }

    public FacingDirection getDirection() {
        return direction;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public AtlasAsset getAtlasAsset() {
        return atlasAsset;
    }

    public String getAtlasKey() {
        return atlasKey;
    }

    public void setType(AnimationType type) {
        this.type = type;
        this.dirty = true;
    }

    public AnimationType getType() {
        return type;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        this.playMode = playMode;
    }

    public boolean isDirty() {
        return dirty;
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(stateTime);
    }

    public float incAndGetStateTime(float deltaTime) {
        this.stateTime += deltaTime * speed;
        return this.stateTime;
    }

    public enum AnimationType {
        IDLE, WALK;

        private final String atlasKey;

        AnimationType() {
            this.atlasKey = name().toLowerCase();
        }

        public String getAtlasKey() {
            return atlasKey;
        }
    }

}
