package net.development.mitw.utils.timer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Timer {

    @Getter
    protected final String name;
    @Getter
    protected final boolean async;
    @Getter
    protected final long defaultCooldown;

    public final String getDisplayName() {
        return this.name;
    }
}