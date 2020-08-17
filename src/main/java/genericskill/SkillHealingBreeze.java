package genericskill;

import net.minecraft.entity.player.EntityPlayer;

public class SkillHealingBreeze extends SkillGeneric {
    @Override
    public String getDescription() {
        return "If your health is less than 2\nhearts, you're instantly healed 2\nhearts. Then for 10 seconds,\nyou're randomly healed half a\nheart at a time.";
    }

    @Override
    public int getManaCost(EntityPlayer player) {
        return 5;
    }

    @Override
    public float getChargeupTime(EntityPlayer player) {
        return 4;
    }

    @Override
    public float getCooldownTime(EntityPlayer player) {
        return 4;
    }

    @Override
    public float getDuration(EntityPlayer player) {
        return 10;
    }

    @Override
    public boolean onSkillTrigger(EntityPlayer player) {
        if (!(player.getHealth() < 4)) {
            return true;
        }
        player.heal(4);
        return true;
    }

    @Override
    public void onTickWhileActive(EntityPlayer player) {
        if (player.getHealth() < 20 && player.getRNG().nextFloat() > 0.9) {
            player.heal(1);
        }
    }
}
