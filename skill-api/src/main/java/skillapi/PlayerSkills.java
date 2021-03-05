package skillapi;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import skillapi.packets.LearnSkillPacket;
import skillapi.packets.SkillPacket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class PlayerSkills implements IExtendedEntityProperties {
    public List<String> knownSkills = new ArrayList<String>();
    public List<String> activeSkills = new LinkedList<String>();
    public Skill[] skillBar = new Skill[5];
    public Skill skillJustLearnt;
    public Skill chargingSkill;
    private int mana, prevMana;
    private EntityPlayer player;

    public PlayerSkills(EntityPlayer player, int mana) {
        this.player = player;
        this.mana = mana;
        this.prevMana = mana;
    }

    public static PlayerSkills get(EntityPlayer player) {
        return (PlayerSkills) player.getExtendedProperties("SKILLAPI");
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getPrevMana() {
        return prevMana;
    }

    public void setPrevMana(int mana) {
        this.prevMana = mana;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public void spendMana(int amount) {
        mana -= amount;
        if (mana < 0) {
            mana = 0;
        }
    }

    public void restoreMana(int amount) {
        if (player.getHealth() <= 0) {
            return;
        }
        mana += amount;
        if (mana > 20) {
            mana = 20;
        }
    }

    /**
     * 学习技能
     *
     * @param skill 技能名
     */
    public void skillGet(Skill skill) {
        knownSkills.add(skill.getName());
        player.worldObj.playSoundAtEntity(player, "note.snare", 0.2F, 1.0F);
        skillJustLearnt = skill;
        if (player instanceof EntityPlayerMP) {
            SkillPacket pkt = new LearnSkillPacket(player.getEntityId(), skill.getName());
            Application.channels.get(pkt.getChannel()).sendTo(pkt.getPacket(Side.CLIENT), (EntityPlayerMP) player);
        }
    }

    public boolean chargeSkill(Skill skill) {
        if (chargingSkill != null) {
            cancelCharge();
            chargingSkill = null;
            player.worldObj.playSoundAtEntity(player, "note.bass", 1.0F, 0F);
            return false;
        }
        chargingSkill = skill;
        startCharge();
        return true;
    }

    public void startCharge() {
        chargingSkill.charge = 1;
        if (!player.worldObj.isRemote) {
            int[] data = SkillTickHandler.getData(player, chargingSkill);
            data[0] = 1;
            SkillTickHandler.get(player).put(chargingSkill.getName(), data);
        }
    }

    public void cancelCharge() {
        if (chargingSkill != null) {
            chargingSkill.charge = 0;
            if (!player.worldObj.isRemote) {
                int[] data = SkillTickHandler.getData(player, chargingSkill);
                data[0] = 0;
                SkillTickHandler.get(player).put(chargingSkill.getName(), data);
            }
        }
    }

    public void resetSkills() {
        cancelCharge();
        if (!player.worldObj.isRemote) {
            Map<String, int[]> map = SkillTickHandler.get(player);
            for (String skill : activeSkills) {
                int[] data = map.get(skill);
                data[2] = 0;
                map.put(skill, data);
            }
            for (Skill skill : skillBar) {
                if (skill != null && skill.cooldownFrame >= 1) {
                    int[] data = map.get(skill.getName());
                    data[0] = 0;
                    map.put(skill.getName(), data);
                }
            }
        }
        chargingSkill = null;
        activeSkills.clear();
        for (Skill skill : skillBar) {
            if (skill != null && skill.cooldownFrame >= 1) {
                skill.cooldownFrame = 0;
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        for (int i = 0; i < knownSkills.size(); i++) {
            compound.setString("KnownSkill" + i, knownSkills.get(i));
        }
        for (int i = 0; i < skillBar.length; i++) {
            if (skillBar[i] != null) {
                compound.setString("SkillBarSlot" + i, skillBar[i].getName());
            }
        }
    }

    /**
     * 服务端读取实体NBT内技能
     *
     * @param compound NBT
     */
    @Override
    public void loadNBTData(NBTTagCompound compound) {
        knownSkills.clear();
        int i = 0;
        String skillName;
        while (true) {
            skillName = compound.getString("KnownSkill" + i);
            if ("".equals(skillName)) {
                break;
            }
            if (SkillRegistry.get(skillName) != null) {
                knownSkills.add(skillName);
            }
            i++;
        }
        for (int j = 0; j < skillBar.length; j++) {
            skillName = compound.getString("SkillBarSlot" + j);
            if (!"".equals(skillName)) {
                skillBar[j] = SkillRegistry.get(skillName);
            }
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }
}
