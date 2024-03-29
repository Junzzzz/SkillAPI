package io.github.junzzzz.skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import io.github.junzzzz.skillapi.Application;
import io.github.junzzzz.skillapi.api.annotation.SkillAnnotation;
import io.github.junzzzz.skillapi.api.annotation.SkillAnnotationRegister;
import io.github.junzzzz.skillapi.api.annotation.SkillEvent;
import io.github.junzzzz.skillapi.common.EventBusHandler;
import io.github.junzzzz.skillapi.common.SkillLog;
import io.github.junzzzz.skillapi.common.SkillRuntimeException;
import io.github.junzzzz.skillapi.event.base.AbstractSkillEvent;
import io.github.junzzzz.skillapi.utils.ClassUtils;
import io.github.junzzzz.skillapi.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Jun
 */
@SkillAnnotation
public final class SkillEventAnnotationImpl implements SkillAnnotationRegister<SkillEvent> {
    private static final Field FIELD_SEVER;
    private static final Field FIELD_CLIENT;

    static {
        try {
            FIELD_SEVER = AbstractSkillEvent.class.getDeclaredField("onServer");
            FIELD_SEVER.setAccessible(true);

            FIELD_CLIENT = AbstractSkillEvent.class.getDeclaredField("onClient");
            FIELD_CLIENT.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new SkillRuntimeException("Impossible error", e);
        }
    }

    @Override
    public void register(Class<?> target, SkillEvent annotation, ModMetadata mod) {
        if (Application.isPhysicalServer) {
            boolean server = false;
            for (Side side : annotation.value()) {
                if (side == Side.SERVER) {
                    server = true;
                    break;
                }
            }
            // Skip only client
            if (!server) {
                return;
            }
        }
        if (AbstractSkillEvent.class.isAssignableFrom(target)) {
            registerClass(target, annotation);
        } else {
            registerMethod(target);
        }
    }

    private void registerClass(Class<?> target, SkillEvent annotation) {
        final Type superClass = target.getGenericSuperclass();

        if (superClass == null) {
            return;
        }

        if (!(superClass instanceof ParameterizedType)) {
            return;
        }

        final Type[] genericTypes = ((ParameterizedType) superClass).getActualTypeArguments();
        if (genericTypes.length != 1) {
            return;
        }
        final Class<?> eventClass = (Class<?>) genericTypes[0];
        final String eventName = eventClass.getName();
        final String eventPackage = ClassUtils.getClassPackage(eventName);

        try {
            final Method method = target.getMethod("run", Event.class);

            if (!method.isAnnotationPresent(SubscribeEvent.class)) {
                return;
            }

            final Object instance = ReflectionUtils.newEmptyInstance(target, "Minecraft event registration failed From: %s", target.getName());

            injection(instance, annotation);

            registerEvent(target, method, eventClass, eventName, eventPackage, instance);
        } catch (NoSuchMethodException e) {
            throw new SkillRuntimeException("Impossible error", e);
        }
    }

    private void registerMethod(Class<?> target) {
        final Object instance = ReflectionUtils.newEmptyInstance(target, "Minecraft event registration failed From: %s", target.getName());

        for (Method method : target.getMethods()) {
            if (!method.isAnnotationPresent(SubscribeEvent.class)) {
                continue;
            }

            final Class<?>[] parameterTypes = method.getParameterTypes();

            // Single parameter event method
            if (parameterTypes.length != 1) {
                continue;
            }
            final Class<?> eventClass = parameterTypes[0];
            final String eventName = eventClass.getName();
            final String eventPackage = ClassUtils.getClassPackage(eventName);

            registerEvent(target, method, eventClass, eventName, eventPackage, instance);
        }
    }

    private void registerEvent(Class<?> target, Method method, Class<?> eventClass, String eventName, String eventPackage, Object instance) {
        if (eventPackage.startsWith("cpw.mods.fml.common.gameevent") || eventName.startsWith("cpw.mods.fml.common.network.FMLNetworkEvent$")) {
            if (!EventBusHandler.forceRegisterFmlEvent(instance, method, eventClass)) {
                SkillLog.error("FML event registration failed. From: %s", target.getName());
            }
        } else if (eventPackage.startsWith("net.minecraftforge.event") || eventPackage.startsWith("net.minecraftforge.client.event")) {
            if (!EventBusHandler.forceRegisterMcEvent(instance, method, eventClass)) {
                SkillLog.error("Minecraft event registration failed From: %s", target.getName());
            }
        } else {
            SkillLog.error("Unable to register event: %s", eventName);
        }
    }


    private void injection(Object instance, SkillEvent annotation) {
        boolean server = false, client = false;

        for (Side effectSide : annotation.value()) {
            if (effectSide == Side.CLIENT) {
                client = true;
            } else {
                server = true;
            }
        }

        try {
            FIELD_SEVER.set(instance, server);
            FIELD_CLIENT.set(instance, client);
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("Impossible error", e);
        }
    }
}
