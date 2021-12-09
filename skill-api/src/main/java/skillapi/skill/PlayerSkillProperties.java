package skillapi.skill;

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
public class PlayerSkillProperties implements IExtendedEntityProperties {
    private static final String TAG_KNOWN = "knownSkills";
    private static final String TAG_MANA = "mana";
    private static final String TAG_TIME = "lastUpdateTime";

    private EntityPlayer player;
    private final Set<AbstractSkill> knownSkills = new LinkedHashSet<>();

    private int mana;
    private long lastUpdateTime;

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound tag = new NBTTagCompound();
        compound.setTag(Application.MOD_ID, tag);
        NBTTagList list = new NBTTagList();
        tag.setTag(TAG_KNOWN, list);
        for (AbstractSkill skill : knownSkills) {
            list.appendTag(new NBTTagString(skill.getUnlocalizedName()));
        }
        tag.setInteger(TAG_MANA, this.mana);
        tag.setLong(TAG_TIME, this.lastUpdateTime);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        knownSkills.clear();

        NBTTagCompound tag = compound.getCompoundTag(Application.MOD_ID);
        NBTTagList knownSkillList = tag.getTagList(TAG_KNOWN, 8);
        for (int i = 0; i < knownSkillList.tagCount(); i++) {
            AbstractSkill skill = Skills.get(knownSkillList.getStringTagAt(i));
            if (skill != null) {
                knownSkills.add(skill);
            }
        }
        this.mana = tag.getInteger(TAG_MANA);
        this.lastUpdateTime = tag.getLong(TAG_TIME);
    }

    @Override
    public void init(Entity entity, World world) {
        if (entity instanceof EntityPlayer) {
            this.player = (EntityPlayer) entity;
            this.mana = Skills.MAX_MANA;
            this.lastUpdateTime = System.currentTimeMillis();
        } else {
            throw new SkillRuntimeException("Unsupported operation");
        }
    }

    public static PlayerSkillProperties get(EntityPlayer player) {
        return (PlayerSkillProperties) player.getExtendedProperties(Application.MOD_ID);
    }
}
