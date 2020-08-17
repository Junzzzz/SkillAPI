package skillapi;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public abstract class Skill {
    public int charge;
    public int cooldownFrame;
    public float timeLeft;

    /**
     * Unique identifier for this skill. Case sensitive.
     */
    public abstract String getName();

    /**
     * Translation
     */
    public abstract String getI18nName();
    /**
     * First part of the description tool tip for this skill. Preferably short.
     */
    public abstract String getType();

    /**
     * Complete description tool tip for this skill. Use \n as separator.
     */
    public abstract String getDescription();

    /**
     * The texture for the skill icon.
     */
    public abstract ResourceLocation getTexture();

    /**
     * Checked against the player total mana before skill execution.
     *
     * @param player The player using the skill.
     * @return Amount of mana spent each time the skill is executed.
     */
    public abstract int getManaCost(EntityPlayer player);

    /**
     * @param player The player using the skill.
     * @return Amount of time before the skill is triggered. 0 for instant.
     */
    public abstract float getChargeupTime(EntityPlayer player);

    /**
     * @param player The player using the skill.
     * @return Amount of time the skill can't be reused after being triggered.
     */
    public abstract float getCooldownTime(EntityPlayer player);

    /**
     * @param player The player using the skill.
     * @return Amount of time the skill is in effect. Unused if skill is
     * instant.
     */
    public abstract float getDuration(EntityPlayer player);

    /**
     * GlColor used when displaying the skill name on the HUD and Skills GUI.
     * Alpha, red, blue, green, values.
     *
     * @return
     */
    public int getNameColour() {
        return 0xFFFFFF;
    }

    /**
     * Called from {@link SkillRegistry#learnSkill(EntityPlayer, String)}
     *
     * @param player The player trying to learn the skill.
     * @return true if given player can learn this skill. He doesn't know it
     * yet.
     */
    public abstract boolean canPlayerLearnSkill(EntityPlayer player);

    /**
     * @param player The player trying to use the skill.
     * @return false to avoid the skill being triggered.
     */
    public abstract boolean canPlayerUseSkill(EntityPlayer player);

    /**
     * Called once after charge time is finished. Instant if
     * {@link #getChargeupTime(EntityPlayer)} returns 0.
     *
     * @param player The player using the skill.
     * @return true for further processing
     */
    public abstract boolean onSkillTrigger(EntityPlayer player);

    /**
     * Called each tick while skill is in active list for the player, or once if
     * {@link #getChargeupTime(EntityPlayer)} and
     * {@link #getCooldownTime(EntityPlayer)} return 0.
     *
     * @param player The player having the skill as active.
     */
    public abstract void onTickWhileActive(EntityPlayer player);

    /**
     * The player just used up all this skill duration time and it has been
     * removed from his active list.
     *
     * @param player The player who used the skill.
     */
    public abstract void onSkillEnd(EntityPlayer player);

    /**
     * This skill has been triggered, called from
     * {@link SkillRegistry#triggerSkill(EntityPlayer, String)} or from the keybind if
     * this skill is in the skill bar
     *
     * @param player
     * @return
     */
    public boolean triggerSkill(EntityPlayer player) {
        PlayerSkills skills = PlayerSkills.get(player);
        if (getManaCost(player) > skills.getMana() || cooldownFrame >= 1 || !canPlayerUseSkill(player)) {
            player.worldObj.playSoundAtEntity(player, "note.bass", 1.0F, 0F);
            return false;
        } else if (getChargeupTime(player) <= 0) {
            if (getCooldownTime(player) <= 0) {
                skills.spendMana(getManaCost(player));
                onTickWhileActive(player);
                return true;
            } else if (onSkillTrigger(player)) {
                skills.spendMana(getManaCost(player));
                if (getDuration(player) > 0) {
                    if (!skills.activeSkills.contains(this.getName())) {
                        skills.activeSkills.add(this.getName());
                    }
                    if (!player.worldObj.isRemote) {
                        startSkillTimer(player);
                    }
                }
                if (!player.worldObj.isRemote) {
                    startCooldown(player);
                }
                cooldownFrame = 1;
                return true;
            }
            player.worldObj.playSoundAtEntity(player, "note.bass", 1.0F, 0F);
            return false;
        }
        return skills.chargeSkill(this);
    }

    /**
     * Skill duration timer started from {@link #triggerSkill(EntityPlayer)}
     *
     * @param player
     */
    private void startSkillTimer(EntityPlayer player) {
        Map<String, int[]> playerSkillMap = SkillTickHandler.get(player);
        int[] data = SkillTickHandler.getData(player, this);
        data[2] = 1;
        playerSkillMap.put(this.getName(), data);
    }

    /**
     * Skill cooldown timer started from {@link #triggerSkill(EntityPlayer)}
     *
     * @param player
     */
    private void startCooldown(EntityPlayer player) {
        Map<String, int[]> playerSkillMap = SkillTickHandler.get(player);
        int[] data = SkillTickHandler.getData(player, this);
        data[1] = 1;
        playerSkillMap.put(this.getName(), data);
    }
}
