package skillapi.skill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;

import java.util.Objects;

/**
 * @author Jun
 */
public abstract class AbstractSkill extends AbstractSkillEffect {
    protected String name;

    protected int mana;
    protected long cooldown;
    protected int charge;

    public final int getMana() {
        return mana;
    }

    public final long getCooldown() {
        return cooldown;
    }

    public final int getCharge() {
        return charge;
    }

    @Override
    public String getUnlocalizedName() {
        return this.name;
    }

    @SideOnly(Side.CLIENT)
    public String getLocalizedName() {
        return I18n.format(this.name);
    }

    @SideOnly(Side.CLIENT)
    public String getDescription() {
        return I18n.format(this.name + ".description");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractSkill that = (AbstractSkill) o;

        return Objects.equals(getUnlocalizedName(), that.getUnlocalizedName());
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
