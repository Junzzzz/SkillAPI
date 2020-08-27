package skillapi.api.impl;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import skillapi.api.SkillAnnotation;
import skillapi.api.SkillAnnotationRegister;
import skillapi.api.annotation.SkillEvent;
import skillapi.api.common.EffectSide;
import skillapi.base.BaseSkillEvent;
import skillapi.skill.SkillRuntimeException;
import skillapi.utils.ClassUtils;
import skillapi.utils.EventBusUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Jun
 * @date 2020/8/26.
 */
@SkillAnnotation
public final class SkillEventAnnotationImpl implements SkillAnnotationRegister<SkillEvent> {
    private static Field FIELD_SEVER;
    private static Field FIELD_CLIENT;

    static {
        try {
            FIELD_SEVER = BaseSkillEvent.class.getDeclaredField("onServer");
            FIELD_SEVER.setAccessible(true);

            FIELD_CLIENT = BaseSkillEvent.class.getDeclaredField("onClient");
            FIELD_CLIENT.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new SkillRuntimeException("Impossible error", e);
        }
    }

    @Override
    public void register(Class<?> target, SkillEvent annotation) {
        if (!BaseSkillEvent.class.isAssignableFrom(target)) {
            return;
        }

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
            final Method method = target.getMethod("run", Object.class);

            if (!method.isAnnotationPresent(SubscribeEvent.class)) {
                return;
            }

            final Object instance = newInstance(target);

            injection(instance, annotation);

            if (eventPackage.startsWith("cpw.mods.fml.common.gameevent")) {
                if (!EventBusUtils.forceRegisterFMLEvent(instance, method, eventClass)) {
                    FMLLog.log(Level.ERROR, "FML event registration failed. From: %s", target.getName());
                }
            } else if (eventPackage.startsWith("net.minecraftforge.event")) {
                if (!EventBusUtils.forceRegisterMCEvent(instance, method, eventClass)) {
                    FMLLog.log(Level.ERROR, "Minecraft event registration failed From: %s", target.getName());
                }
            } else {
                FMLLog.log(Level.ERROR, "Unable to register event: %s", eventName);
            }
        } catch (NoSuchMethodException e) {
            throw new SkillRuntimeException("Impossible error", e);
        }
    }

    private Object newInstance(Class<?> target) {
        try {
            return target.newInstance();
        } catch (InstantiationException e) {
            FMLLog.log(Level.ERROR, e, "Minecraft event registration failed From: %s", target.getName());
            return null;
        } catch (IllegalAccessException e) {
            FMLLog.log(Level.ERROR, e, "Minecraft event registration failed From: %s", target.getName());
            return null;
        }
    }

    private void injection(Object instance, SkillEvent annotation) {
        boolean server = false, client = false;

        for (EffectSide effectSide : annotation.value()) {
            if (effectSide == EffectSide.CLIENT) {
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
