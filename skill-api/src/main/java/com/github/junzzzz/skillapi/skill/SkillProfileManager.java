package com.github.junzzzz.skillapi.skill;

import com.github.junzzzz.skillapi.common.SkillLog;
import com.github.junzzzz.skillapi.common.SkillNBT;
import com.github.junzzzz.skillapi.skill.SkillProfile.SkillProfileInfo;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jun
 */
public class SkillProfileManager {
    private final Set<String> profileNames;

    public SkillProfileManager() {
        // Default
        profileNames = new LinkedHashSet<>(4);
    }

    void init(NBTTagCompound profilesTag) {
        for (Object key : profilesTag.func_150296_c()) {
            String profileName = key.toString();
            profileNames.add(profileName);
        }
    }

    private byte[] getProfileBytes(String name) {
        NBTTagCompound profile = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC, Skills.TAG_DYNAMIC_PROFILES);
        byte[] bytes = profile.getByteArray(name);
        if (bytes.length == 0) {
            return null;
        }
        return bytes;
    }

    public SkillProfile get(String name) {
        byte[] bytes = getProfileBytes(name);
        if (bytes == null) {
            SkillLog.warn("Can't find skill profile named %s", name);
            return null;
        }
        try {
            return SkillProfile.valueOf(bytes);
        } catch (IOException e) {
            SkillLog.error(e, "Skill profile %s deserialization failed", name);
            return null;
        }
    }

    public boolean contains(String name) {
        return this.profileNames.contains(name);
    }

    public boolean remove(String name) {
        boolean success = this.profileNames.remove(name);
        if (success) {
            NBTTagCompound tag = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC, Skills.TAG_DYNAMIC_PROFILES);
            tag.removeTag(name);
            SkillNBT.save();
        }
        return success;
    }

    public SkillProfileInfo getProfileInfo(String name) {
        byte[] bytes = getProfileBytes(name);
        if (bytes == null) {
            return null;
        }
        return SkillProfile.getInfo(bytes);
    }

    public List<SkillProfileInfo> getInfos() {
        List<SkillProfileInfo> result = new ArrayList<>(profileNames.size());
        for (String profileName : profileNames) {
            result.add(getProfileInfo(profileName));
        }
        return result;
    }

    public void saveProfile(SkillProfile profile) {
        NBTTagCompound tag = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC, Skills.TAG_DYNAMIC_PROFILES);
        try {
            tag.setByteArray(profile.name, profile.getBytes());
            SkillNBT.save();
            profileNames.add(profile.name);
        } catch (Exception e) {
            SkillLog.error(e, "Saving profile [%s] failed.", profile.name);
        }
    }
}
