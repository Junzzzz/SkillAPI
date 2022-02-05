package skillapi.skill;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import skillapi.Application;
import skillapi.common.SkillRuntimeException;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Jun
 */
public class PlayerSkills implements IExtendedEntityProperties {
    public static final int MAX_SKILL_BAR = 5;
    public static final int MAX_KNOWN_SKILL = 10;

    private static final String TAG_KNOWN = "knownSkills";
    private static final String TAG_MANA = "mana";
    private static final String TAG_TIME = "lastManaUpdateTime";
    private static final String TAG_SKILL_BAR = "skillBar";

    @Getter
    private EntityPlayer player;
    private AbstractSkill[] skillBar;
    private Cooldown[] cooldowns;
    @Getter
    private Set<AbstractSkill> knownSkills;

    private int mana;
    private long lastManaUpdateTime;

    public synchronized void consumeMana(int num) {
        this.mana = getMana() - num;
        this.lastManaUpdateTime = player.getEntityWorld().getTotalWorldTime();
    }

    public int getMana() {
        return (int) Math.min(mana + (player.getEntityWorld().getTotalWorldTime() - lastManaUpdateTime) / 20, 20);
    }

    public AbstractSkill[] getSkillBar() {
        return skillBar;
    }

    public boolean learnSkill(AbstractSkill skill) {
        return this.knownSkills.add(skill);
    }

    private boolean insureIndex(int index) {
        return 0 <= index && index < MAX_SKILL_BAR;
    }

    public AbstractSkill getSkill(int index) {
        return insureIndex(index) ? this.skillBar[index] : null;
    }

    public Cooldown getSkillCooldown(int index) {
        return insureIndex(index) ? this.cooldowns[index] : null;
    }

    public void setSkillBar(int index, AbstractSkill skill) {
        if (index < skillBar.length && index >= 0) {
            this.skillBar[index] = skill;

            // Reset cooldown
            if (this.cooldowns[index] == null) {
                this.cooldowns[index] = Cooldown.get(player, skill.getCooldown());
            } else {
                this.cooldowns[index].setCooldown(skill.cooldown);
            }
            this.cooldowns[index].setCooling();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound tag = new NBTTagCompound();
        compound.setTag(Application.MOD_ID, tag);
        NBTTagList list = new NBTTagList();
        // Known skill
        tag.setTag(TAG_KNOWN, list);
        for (AbstractSkill skill : knownSkills) {
            list.appendTag(new NBTTagString(skill.getUnlocalizedName()));
        }

        // Skill bar
        list = new NBTTagList();
        tag.setTag(TAG_SKILL_BAR, list);
        for (AbstractSkill skill : skillBar) {
            String name = (skill == null ? "" : skill.getUnlocalizedName());
            list.appendTag(new NBTTagString(name));
        }

        // Mana
        tag.setInteger(TAG_MANA, this.mana);

        // Update time
        tag.setLong(TAG_TIME, this.lastManaUpdateTime);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        knownSkills.clear();

        NBTTagCompound tag = compound.getCompoundTag(Application.MOD_ID);
        NBTTagList list = tag.getTagList(TAG_KNOWN, 8);
        for (int i = 0; i < list.tagCount(); i++) {
            AbstractSkill skill = Skills.get(list.getStringTagAt(i));
            if (skill != null) {
                knownSkills.add(skill);
            }
        }
        list = tag.getTagList(TAG_SKILL_BAR, 8);
        if (list.tagCount() <= MAX_SKILL_BAR) {
            for (int i = 0; i < list.tagCount(); i++) {
                AbstractSkill skill = Skills.get(list.getStringTagAt(i));
                if (skill != null) {
                    cooldowns[i] = Cooldown.get(player, skill.getCooldown());
                    cooldowns[i].setCooling();
                    skillBar[i] = skill;
                }
            }
        }

        this.mana = tag.getInteger(TAG_MANA);
        this.lastManaUpdateTime = tag.getLong(TAG_TIME);
    }

    @Override
    public void init(Entity entity, World world) {
        if (entity instanceof EntityPlayer) {
            this.player = (EntityPlayer) entity;
            this.skillBar = new AbstractSkill[MAX_SKILL_BAR];
            this.cooldowns = new Cooldown[MAX_SKILL_BAR];
            this.knownSkills = new LinkedHashSet<>();
            this.mana = Skills.MAX_MANA;
            this.lastManaUpdateTime = System.currentTimeMillis();
        } else {
            throw new SkillRuntimeException("Unsupported operation");
        }
    }

    public static PlayerSkills get(EntityPlayer player) {
        return (PlayerSkills) player.getExtendedProperties(Application.MOD_ID);
    }
}
