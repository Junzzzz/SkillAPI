package skillapi.common;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.server.SkillServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Jun
 */
public class SkillNBT {
    public static final String TAG_DYNAMIC = "dynamic";

    private static File file;
    private static NBTTagCompound nbt;

    public static synchronized void init() {
        file = new File(SkillServer.getWorldDirectory(), "skill.dat");
        if (!file.exists()) {
            nbt = new NBTTagCompound();
        } else {
            try {
                nbt = CompressedStreamTools.readCompressed(new FileInputStream(file));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read skill data.");
            }
        }
    }

    public static synchronized NBTTagCompound getTag(String name) {
        if (nbt.hasKey(name, 10)) {
            return nbt.getCompoundTag(name);
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            nbt.setTag(name, tag);
            return tag;
        }
    }

    public static synchronized void save() {
        try {
            CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(file));
        } catch (IOException e) {
            SkillLog.error(e, "Failed to save skill data.");
        }
    }
}
