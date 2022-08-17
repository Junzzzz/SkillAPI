package com.github.junzzzz.skillapi.common;

import com.github.junzzzz.skillapi.server.SkillServer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

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
    private static File bak;
    private static NBTTagCompound nbt;

    public static synchronized void init() {
        file = new File(SkillServer.getWorldDirectory(), "skill.dat");
        bak = new File(SkillServer.getWorldDirectory(), "skill.dat.bak");
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

    public static synchronized NBTTagCompound getTag(String... name) {
        NBTTagCompound tag = nbt;
        for (String str : name) {
            tag = getTag(tag, str);
        }
        return tag;
    }

    public static synchronized NBTTagCompound getTag(String name) {
        return getTag(nbt, name);
    }

    private static synchronized NBTTagCompound getTag(NBTTagCompound tag, String name) {
        if (tag.hasKey(name, 10)) {
            return tag.getCompoundTag(name);
        } else {
            NBTTagCompound result = new NBTTagCompound();
            tag.setTag(name, result);
            return result;
        }
    }

    public static synchronized void save() {
        try {
            if (bak.exists()) {
                bak.delete();
            }
            file.renameTo(bak);
            CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(file));
        } catch (IOException e) {
            SkillLog.error(e, "Failed to save skill data.");
        }
    }
}
