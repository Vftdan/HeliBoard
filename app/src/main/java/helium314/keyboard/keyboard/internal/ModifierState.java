/*
 * Copyright (C) 2010 The Android Open Source Project
 * modified from AlphabetShiftState
 * SPDX-License-Identifier: Apache-2.0 AND GPL-3.0-only
 */

package helium314.keyboard.keyboard.internal;

import helium314.keyboard.latin.utils.Log;

import androidx.annotation.NonNull;

// Like AlphabetShiftState, but cannot be automatic and is responsible
// for prefixed state tracking
public final class ModifierState {
    private static final String TAG = AlphabetShiftState.class.getSimpleName();
    private static final boolean DEBUG = false;

    public static final int FLAG_TOGGLED = 1;
    public static final int FLAG_LOCKED = 2;
    public static final int FLAG_PREFIXED = 4;

    private static final int DISABLED = 0;
    private static final int ENABLED = FLAG_TOGGLED;
    private static final int LOCKED = FLAG_LOCKED;
    private static final int LOCK_TOGGLED = FLAG_LOCKED | FLAG_TOGGLED;
    private static final int PREFIXED = FLAG_PREFIXED;

    private final String name;
    private int mState = DISABLED;

    public ModifierState(String name) {
        this.name = name;
    }

    public void setEnabled(boolean newEnabledState) {
        final int oldState = mState;
        if (newEnabledState) {
            switch (oldState) {
                case DISABLED, PREFIXED -> mState = ENABLED;
                case LOCKED -> mState = LOCK_TOGGLED;
            }
        } else {
            switch (oldState) {
                case ENABLED -> mState = DISABLED;
                case LOCK_TOGGLED -> mState = LOCKED;
            }
        }
        if (DEBUG)
            Log.d(TAG, name + ".setEnabled(" + newEnabledState + "): " + toString(oldState) + " > " + this);
    }

    public void setLocked(boolean newLockedState) {
        final int oldState = mState;
        if (newLockedState) {
            switch (oldState) {
                case DISABLED, PREFIXED, ENABLED -> mState = LOCKED;
            }
        } else {
            mState = DISABLED;
        }
        if (DEBUG)
            Log.d(TAG, name + ".setLocked(" + newLockedState + "): " + toString(oldState) + " > " + this);
    }

    public void setPrefixed(boolean newPrefixedState) {
        final int oldState = mState;
        if (newPrefixedState) {
            mState = PREFIXED;
        } else {
            switch (oldState) {
                case PREFIXED -> mState = DISABLED;
            }
        }
        if (DEBUG)
            Log.d(TAG, name + ".setPrefixed(" + newPrefixedState + "): " + toString(oldState) + " > " + this);
    }

    public final void unsetPrefixed() {
        setPrefixed(false);
    }

    public boolean isDisabled() {
        return mState == DISABLED;
    }

    public boolean isEnabled() {
        return mState == ENABLED;
    }

    public boolean isEnabledOrLocked() {
        return mState != DISABLED;
    }

    public boolean isLocked() {
        return mState == LOCKED || mState == LOCK_TOGGLED;
    }

    public boolean isLockToggled() {
        return mState == LOCK_TOGGLED;
    }

    public boolean isPrefixed() {
        return mState == PREFIXED;
    }

    public int getFlags() {
        return mState;
    }

    public void setFlags(int flags) {
        if ((flags & FLAG_PREFIXED) != 0) {
            mState = PREFIXED;
        } else {
            mState = flags & (FLAG_TOGGLED | FLAG_LOCKED);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return toString(mState);
    }

    private static String toString(int state) {
        return switch (state) {
            case DISABLED -> "DISABLED";
            case ENABLED -> "ENABLED";
            case LOCKED -> "LOCKED";
            case LOCK_TOGGLED -> "LOCK_TOGGLED";
            case PREFIXED -> "PREFIXED";
            default -> "UNKNOWN";
        };
    }
}
