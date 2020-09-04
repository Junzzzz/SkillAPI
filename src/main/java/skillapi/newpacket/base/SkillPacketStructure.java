package skillapi.newpacket.base;

import skillapi.common.SkillLog;
import skillapi.utils.JsonUtils;

import java.io.IOException;

/**
 * @author Jun
 * @date 2020/8/30.
 */
public final class SkillPacketStructure {
    private Class<? extends BaseSkillPacket> clz;

    public SkillPacketStructure(Class<? extends BaseSkillPacket> target) {
        this.clz = target;
    }

    public BaseSkillPacket newInstance(byte[] data) {
        try {
            return JsonUtils.getMapper().readValue(data, this.clz);
        } catch (IOException e) {
            SkillLog.error("Packet conversion failed", e);
        }
        return null;
    }

    public String getName() {
        return clz.getName();
    }
}