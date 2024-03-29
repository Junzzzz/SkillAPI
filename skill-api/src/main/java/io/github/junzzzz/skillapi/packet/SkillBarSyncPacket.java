package io.github.junzzzz.skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import io.github.junzzzz.skillapi.api.annotation.SkillPacket;
import io.github.junzzzz.skillapi.common.Translation;
import io.github.junzzzz.skillapi.packet.base.AbstractPacket;
import io.github.junzzzz.skillapi.server.SkillServer;
import io.github.junzzzz.skillapi.skill.AbstractSkill;
import io.github.junzzzz.skillapi.skill.PlayerSkills;
import io.github.junzzzz.skillapi.skill.Skills;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.StringUtils;

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
    protected void run(EntityPlayer player, Side from) {
        if (from.isServer()) {
            return;
        }
        PlayerSkills properties = PlayerSkills.get(player);
        Set<AbstractSkill> knownSkills = properties.getKnownSkills();
        for (int i = 0; i < this.skillNames.length; i++) {
            if (StringUtils.isEmpty(skillNames[i])) {
                continue;
            }
            AbstractSkill skill = Skills.get(skillNames[i]);
            if (knownSkills.contains(skill)) {
                properties.setSkillBar(i, skill);
            } else if (from.isClient()) {
                // Data is out of sync
                String message = Translation.format("skill.constant.error.sync");
                SkillServer.kick(player, message);
            }
        }
    }
}
