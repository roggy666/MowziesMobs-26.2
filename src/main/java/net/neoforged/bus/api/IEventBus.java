package net.neoforged.bus.api;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public interface IEventBus {
    <T> void addListener(Consumer<T> consumer);
    void register(Object target);
    void unregister(Object target);
    <T> T post(T event);

    static IEventBus create() {
        return new EventBusImpl();
    }
}

class EventBusImpl implements IEventBus {
    private final Map<Class<?>, List<Consumer<Object>>> listeners = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> void addListener(Consumer<T> consumer) {
        // Generic listener registration
        listeners.computeIfAbsent(Object.class, k -> new CopyOnWriteArrayList<>()).add((Consumer<Object>) consumer);
    }

    @Override
    public void register(Object target) {
        if (target == null) return;
        Class<?> clazz = target instanceof Class<?> ? (Class<?>) target : target.getClass();
        boolean isStatic = target instanceof Class<?>;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class)) {
                if (isStatic && !java.lang.reflect.Modifier.isStatic(method.getModifiers())) continue;
                if (!isStatic && java.lang.reflect.Modifier.isStatic(method.getModifiers())) continue;
                if (method.getParameterCount() != 1) continue;

                Class<?> eventType = method.getParameterTypes()[0];
                method.setAccessible(true);

                listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(event -> {
                    try {
                        method.invoke(isStatic ? null : target, event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void unregister(Object target) {
        // No-op for mod lifecycle
    }

    @Override
    public <T> T post(T event) {
        if (event == null) return event;
        Class<?> eventClass = event.getClass();
        
        for (Map.Entry<Class<?>, List<Consumer<Object>>> entry : listeners.entrySet()) {
            if (entry.getKey().isAssignableFrom(eventClass)) {
                for (Consumer<Object> consumer : entry.getValue()) {
                    try {
                        consumer.accept(event);
                    } catch (ClassCastException ignored) {
                        // Expected if consumer targets a more specific event type
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return event;
    }
}
