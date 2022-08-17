package com.github.junzzzz.skillapi.packet.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.junzzzz.skillapi.api.annotation.SkillPacket;
import com.github.junzzzz.skillapi.packet.serializer.CallbackResultSerializer;
import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket(serializer = CallbackResultSerializer.class)
public class CallbackResultPacket<T> extends AbstractPacket {
    private UUID uuid;
    private T data;

    @JsonIgnore
    private ParameterizedDataType dataType;

    @Override
    protected void run(EntityPlayer player, Side from) {
        Packet.callback(this.uuid, this.data);
    }
}
