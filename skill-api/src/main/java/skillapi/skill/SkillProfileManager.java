package skillapi.skill;

import net.minecraft.nbt.NBTTagCompound;
import skillapi.common.SkillNBT;
import skillapi.skill.SkillProfile.SkillProfileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 */
public class SkillProfileManager {
    private final List<String> profileNames;

    public SkillProfileManager() {
        // Default
        profileNames = new ArrayList<>(4);
    }

    void init(NBTTagCompound profilesTag) {
        for (Object key : profilesTag.func_150296_c()) {
            String profileName = key.toString();
            profileNames.add(profileName);
        }
    }

    public SkillProfileInfo getProfileInfo(String name) {
        NBTTagCompound profile = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC, Skills.TAG_DYNAMIC_PROFILES);
        byte[] bytes = profile.getByteArray(name);
        if (bytes.length == 0) {
            return null;
        }
        return SkillProfile.getInfo(bytes);
    }

    public SkillProfileInfo getProfileInfo(int index) {
        final int size = profileNames.size();
        if (0 <= index && index < size) {
            return getProfileInfo(this.profileNames.get(index));
        }
        return null;
    }

    public List<SkillProfileInfo> getInfos() {
        List<SkillProfileInfo> result = new ArrayList<>(profileNames.size());
        for (String profileName : profileNames) {
            result.add(getProfileInfo(profileName));
        }
        return result;
    }
}
