package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import skillapi.api.annotation.SkillPacket;
import skillapi.server.SkillServer;
import skillapi.skill.AbstractSkill;
import skillapi.skill.PlayerSkills;
import skillapi.skill.Skills;

import java.util.Set;

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
        PlayerSkills properties = PlayerSkills.get(player);
        Set<AbstractSkill> knownSkills = properties.getKnownSkills();
        for (int i = 0; i < this.skillNames.length; i++) {
            AbstractSkill skill = Skills.get(skillNames[i]);
            if (knownSkills.contains(skill)) {
                properties.setSkillBar(i, skill);
            } else if (from.isClient()) {
                // Data is out of sync
                String message = StatCollector.translateToLocal("skill.constant.error.sync");
                SkillServer.kick(player, message);
            }
        }
    }
}
