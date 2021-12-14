package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.skill.AbstractSkill;
import skillapi.skill.PlayerSkillProperties;
import skillapi.skill.Skills;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@SkillPacket
public class SkillBarSyncPacket extends AbstractPacket {
    private String[] skillNames;

    public SkillBarSyncPacket(AbstractSkill[] skills) {
        this.skillNames = new String[skills.length];
        for (int i = 0; i < skills.length; i++) {
            this.skillNames[i] = skills[i] == null ? "" : skills[i].getUnlocalizedName();
        }
    }

    @Override
    void run(EntityPlayer player, Side from) {
        PlayerSkillProperties properties = PlayerSkillProperties.get(player);
        for (int i = 0; i < this.skillNames.length; i++) {
            AbstractSkill skill = Skills.get(skillNames[i]);
            properties.setSkillBar(i, skill);
        }
    }
}
