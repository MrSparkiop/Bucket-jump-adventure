package com.hat_quest;

import com.badlogic.gdx.utils.TimeUtils;

public class Shield {
    private boolean active;
    private long lastActivatedTime;
    private long activationDuration; // in nanoseconds
    private long cooldownTime; // in nanoseconds
    private boolean justDeactivated;

    public Shield() {
        this.active = false;
        this.activationDuration = 10 * 1000000000L; // 10 seconds
        this.cooldownTime = 120 * 1000000000L; // 120 seconds
        this.lastActivatedTime = TimeUtils.nanoTime() - cooldownTime; // Initialize so it can be activated immediately
        this.justDeactivated = false;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        if (canActivate()) {
            active = true;
            justDeactivated = false;
            lastActivatedTime = TimeUtils.nanoTime();
        }
    }

    public void update() {
        if (active && TimeUtils.nanoTime() - lastActivatedTime > activationDuration) {
            active = false;
            justDeactivated = true;
            lastActivatedTime = TimeUtils.nanoTime();
        }
    }

    public boolean canActivate() {
        if (active || justDeactivated) {
            return false;
        }
        long timeElapsedSinceLastActivation = TimeUtils.nanoTime() - lastActivatedTime;
        return timeElapsedSinceLastActivation > cooldownTime;
    }

    public long getCooldownRemaining() {
        if (justDeactivated) {
            long timeElapsedSinceDeactivation = TimeUtils.nanoTime() - lastActivatedTime;
            long cooldownRemaining = cooldownTime - timeElapsedSinceDeactivation;
            if (cooldownRemaining < 0) {
                justDeactivated = false; // Reset the flag after cooldown ends
                return 0;
            }
            return cooldownRemaining / 1000000000L;
        }
        return 0;
    }
}
