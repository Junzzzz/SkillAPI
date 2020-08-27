package skillapi.api.impl;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import skillapi.api.SkillAnnotation;
import skillapi.api.SkillAnnotationRegister;
import skillapi.api.annotation.SkillEvent;
import skillapi.base.BaseSkillEvent;
import skillapi.utils.ClassUtils;
import skillapi.utils.EventBusUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Jun
 * @date 2020/8/26.
 */
@SkillAnnotation(SkillEvent.class)
public final class SkillEventAnnotationImpl implements SkillAnnotationRegister {
    @Override
    public void register(Class<?> target) {
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

        for (Method method : target.getMethods()) {
            if (!method.isAnnotationPresent(SubscribeEvent.class)) {
                continue;
            }
            if (eventPackage.startsWith("cpw.mods.fml.common.gameevent")) {
                if (!EventBusUtils.forceRegisterFMLEvent(target, method, eventClass)) {
                    FMLLog.log(Level.ERROR, "FML event registration failed. From: %s.%s", target.getName(), method.getName());
                }
            } else if (eventPackage.startsWith("net.minecraftforge.event")) {
                if (!EventBusUtils.forceRegisterMCEvent(target, method, eventClass)) {
                    FMLLog.log(Level.ERROR, "Minecraft event registration failed From: %s.%s", target.getName(), method.getName());
                }
            } else {
                FMLLog.log(Level.ERROR, "Unable to register event: %s", eventName);
            }
        }
    }
}
