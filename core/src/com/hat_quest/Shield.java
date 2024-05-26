package com.hat_quest;

import com.badlogic.gdx.utils.TimeUtils;

public class Shield {
    private boolean active;
    private long lastActivatedTime;
    private long activationDuration; // in nanoseconds
    private long cooldownTime; // in nanoseconds

    public Shield() {
        this.active = false;
        this.activationDuration = 10 * 1000000000L; // 10 seconds
        this.cooldownTime = 120 * 1000000000L; // 120 seconds
        this.lastActivatedTime = TimeUtils.nanoTime() - cooldownTime; // Initialize so it can be activated immediately
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        if (canActivate()) {
            active = true;
            lastActivatedTime = TimeUtils.nanoTime();
        }
    }

    public void update() {
        if (active && TimeUtils.nanoTime() - lastActivatedTime > activationDuration) {
            active = false;
        }
    }

    public boolean canActivate() {
        return TimeUtils.nanoTime() - lastActivatedTime > cooldownTime;
    }

    public long getCooldownRemaining() {
        return (cooldownTime - (TimeUtils.nanoTime() - lastActivatedTime)) / 1000000000L;
    }
}
