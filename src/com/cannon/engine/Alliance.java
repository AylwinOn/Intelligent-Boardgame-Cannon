package com.cannon.engine;

import com.cannon.engine.player.DarkPlayer;
import com.cannon.engine.player.LightPlayer;
import com.cannon.engine.player.Player;

public enum Alliance {
    LIGHT {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isLight() {
            return true;
        }

        @Override
        public boolean isDark() {
            return false;
        }

        @Override
        public Player choosePlayer(final LightPlayer lightPlayer, final DarkPlayer darkPlayer) {
            return lightPlayer;
        }
    },
    DARK {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isLight() {
            return false;
        }

        @Override
        public boolean isDark() {
            return true;
        }

        @Override
        public Player choosePlayer(final LightPlayer lightPlayer, final DarkPlayer darkPlayer) {
            return darkPlayer;
        }
    };

    public abstract int getDirection();
    public abstract boolean isLight();
    public abstract boolean isDark();

    public abstract Player choosePlayer(LightPlayer lightPlayer, DarkPlayer darkPlayer);
}
