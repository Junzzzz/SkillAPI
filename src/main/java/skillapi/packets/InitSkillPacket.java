package skillapi.packets;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.PlayerSkills;
import skillapi.Skill;
import skillapi.SkillAPI;
import skillapi.SkillRegistry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InitSkillPacket extends SkillPacket {
    private int id;
    private int mana;
    private List<String> known = new ArrayList<String>();
    private List<String> active = new LinkedList<String>();
    private Skill[] bar = new Skill[5];

    public InitSkillPacket() {
    }

    public InitSkillPacket(PlayerSkills skills) {
        this.id = skills.getPlayer().getEntityId();
        this.mana = skills.getMana();
        this.known = skills.knownSkills;
        this.active = skills.activeSkills;
        this.bar = skills.skillBar;
    }

    @Override
    public String getChannel() {
        return SkillPacketHandler.CHANNELS[0];
    }

    @Override
    public void toBytes(ByteBuf out) {
        out.writeInt(id);
        out.writeInt(mana);
        out.writeInt(known.size());
        byte[] temp;
        if (known.size() > 0) {
            for (String skill : known) {
                temp = skill.getBytes(Charsets.UTF_8);
                out.writeByte(temp.length);
                out.writeBytes(temp);
            }
        }
        out.writeInt(active.size());
        if (active.size() > 0) {
            for (String skill : active) {
                temp = skill.getBytes(Charsets.UTF_8);
                out.writeByte(temp.length);
                out.writeBytes(temp);
            }
        }
        for (Skill skill : bar) {
            if (skill != null) {
                temp = skill.getName().getBytes(Charsets.UTF_8);
                out.writeByte(temp.length);
                out.writeBytes(temp);
            } else {
                out.writeByte(0);
            }
        }
        out.writeByte(-1);
    }

    @Override
    public void fromBytes(ByteBuf in) {
        id = in.readInt();
        mana = in.readInt();
        int size = in.readInt();
        byte[] temp;
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                temp = new byte[in.readByte()];
                in.readBytes(temp);
                known.add(new String(temp, Charsets.UTF_8));
            }
        }
        size = in.readInt();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                temp = new byte[in.readByte()];
                in.readBytes(temp);
                active.add(new String(temp, Charsets.UTF_8));
            }
        }
        for (int i = 0; i < bar.length; i++) {
            size = in.readByte();
            if (size < 0) {
                break;
            }
            if (size > 0) {
                temp = new byte[size];
                in.readBytes(temp);
                bar[i] = SkillRegistry.get(new String(temp, Charsets.UTF_8));
            } else {
                bar[i] = null;
            }
        }
    }

    @Override
    boolean run(EntityPlayer player) {
        if (player.getEntityId() == id) {
            PlayerSkills skills = PlayerSkills.get(player);
            skills.setMana(mana);
            skills.knownSkills.clear();
            skills.knownSkills.addAll(known);

            skills.activeSkills.clear();
            skills.activeSkills.addAll(active);
            System.arraycopy(bar, 0, skills.skillBar, 0, bar.length);
            SkillAPI.proxy.updateKeyBindingTypes(player);
        }
        return false;
    }
}
