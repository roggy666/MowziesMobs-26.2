package com.bobmowzie.mowziesmobs.client.render.entity;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashMap;

public class RenderDataHelper {
    private static final Map<Object, Map<Object, Object>> DATA = new WeakHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T get(Object holder, Object key) {
        Map<Object, Object> map = DATA.get(holder);
        return map != null ? (T) map.get(key) : null;
    }

    public static void set(Object holder, Object key, Object value) {
        DATA.computeIfAbsent(holder, k -> new HashMap<>()).put(key, value);
    }
}
