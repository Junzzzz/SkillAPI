package skillapi.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ServerConfigurationManager;
import net.sf.cglib.proxy.CallbackHelper;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import skillapi.packet.PacketHandler;
import skillapi.skill.Skills;

import java.lang.reflect.Method;

/**
 * @author Jun
 */
public class PacketInjection extends CallbackHelper {
    public PacketInjection(Class<? extends ServerConfigurationManager> clz) {
        super(clz, new Class[0]);
    }

    @Override
    protected Object getCallback(Method method) {
        if ("func_96456_a".equals(method.getName())) {
            return new ServerSetTickInterceptor();
        }
        return NoOp.INSTANCE;
    }

    private static class ServerSetTickInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            EntityPlayerMP player = (EntityPlayerMP) args[1];
            PacketHandler.sendToClient(Skills.getInitPacket(player), player);
            return proxy.invokeSuper(obj, args);
        }
    }
}
