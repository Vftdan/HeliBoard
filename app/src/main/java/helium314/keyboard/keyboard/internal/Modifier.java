package helium314.keyboard.keyboard.internal;

import helium314.keyboard.keyboard.internal.keyboard_parser.floris.KeyCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Modifier {
    SHIFT("Shift", KeyCode.SHIFT, KeyCode.CAPS_LOCK),
    CTRL("Ctrl", KeyCode.CTRL, KeyCode.CTRL_LOCK),
    ALT("Alt", KeyCode.ALT, KeyCode.ALT_LOCK),
    META("Meta", KeyCode.META, KeyCode.META_LOCK),
    FN("Fn", KeyCode.FN, KeyCode.FN_LOCK);

    public static final Modifier[] list = Modifier.class.getEnumConstants();
    // toUnmodifiableMap only appears since Android 13
    public static final Map<Integer, Modifier> byKeyCode = Collections.unmodifiableMap(Arrays.stream(list).collect(Collectors.toMap(m -> m.keyCode, Function.identity())));
    public static final Map<Integer, Modifier> byLockKeyCode = Collections.unmodifiableMap(Arrays.stream(list).collect(Collectors.toMap(m -> m.lockKeyCode, Function.identity())));

    public final String name;
    public final int keyCode;
    public final int lockKeyCode;

    Modifier(String name, int code, int lockCode) {
        this.name = name;
        this.keyCode = code;
        this.lockKeyCode = lockCode;
    }
}
