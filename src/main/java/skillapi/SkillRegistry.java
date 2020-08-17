package skillapi;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public final class SkillRegistry {
    private static Map<String, Skill> skillMap = new HashMap<String, Skill>();

    /**
     * @return an immutable map of the currently registered skills
     */
    public static Map<String, Skill> getSkillMap() {
        return ImmutableMap.copyOf(skillMap);
    }

    /**
     * Registers a skill.
     *
     * @param skill skill
     * @return false if a skill with the same name (case sensitive) has already
     * been registered. Returns true if skill was successfully
     * registered.
     */
    public static boolean registerSkill(Skill skill) {
        if (isSkillRegistered(skill.getName())) {
            return false;
        }
        skillMap.put(skill.getName(), skill);
        return true;
    }

    /**
     * @param skill to search for
     * @return true if skill is already in the database
     */
    public static boolean isSkillRegistered(Skill skill) {
        return isSkillRegistered(skill.getName());
    }

    /**
     * @param name of the skill to search for, case sensitive
     * @return true if a skill with this name is already in the database
     */
    public static boolean isSkillRegistered(String name) {
        return skillMap.containsKey(name);
    }

    /**
     * @param name of the skill to get the registered instance from, case
     *             sensitive
     * @return the registered skill instance or null if it isn't registered
     */
    public static Skill get(String name) {
        return skillMap.get(name);
    }

    /**
     * Teaches the player a new skill.
     *
     * @param player    to learn the skill to
     * @param skillName to teach to the player
     * @return true if the player learned the skill successfully. false if the
     * skill does not exist, the player already knows it or the player
     * isn't qualified to learn it.
     */
    public static boolean learnSkill(EntityPlayer player, String skillName) {
        if (player != null && !isSkillKnown(player, skillName)) {
            Skill skill = skillMap.get(skillName);
            if (skill != null && skill.canPlayerLearnSkill(player)) {
                if (!player.worldObj.isRemote) {
                    PlayerSkills.get(player).skillGet(skill);
                    SkillTickHandler.get(player).put(skillName, new int[]{0, 0, 0});
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player knows a skill. Skill name is case sensitive.
     *
     * @param player to evaluate the skills from
     * @param name   of the skill to search for
     * @return true if the player knows a skill with this name.
     */
    public static boolean isSkillKnown(EntityPlayer player, String name) {
        if (PlayerSkills.get(player) != null) {
            return PlayerSkills.get(player).knownSkills.contains(name);
        }
        return false;
    }

    /**
     * Triggers any skill currently in the skill bar. Can be used anywhere (e.g.
     * Item right-click)
     *
     * @param player    player
     * @param skillName name
     * @return true if trigger was successful.
     */
    public static boolean triggerSkill(EntityPlayer player, String skillName) {
        if (PlayerSkills.get(player) != null) {
            Skill[] skillBar = PlayerSkills.get(player).skillBar;
            for (Skill skill : skillBar) {
                if (skill != null && skill.getName().equals(skillName)) {
                    return skill.triggerSkill(player);
                }
            }
        }
        return false;
    }

    /**
     * @param name   name
     * @param player player
     * @return true if a skill is active. Only skills with a duration can be
     * active.
     */
    public static boolean isSkillActive(EntityPlayer player, String name) {
        if (PlayerSkills.get(player) != null) {
            return PlayerSkills.get(player).activeSkills.contains(name);
        }
        return false;
    }
}
