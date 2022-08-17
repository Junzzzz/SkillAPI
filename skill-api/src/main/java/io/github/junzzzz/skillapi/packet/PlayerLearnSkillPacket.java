package io.github.junzzzz.skillapi.packet;

import com.fasterxml.jackson.annotation.JsonProperty;
import cpw.mods.fml.relauncher.Side;
import io.github.junzzzz.skillapi.api.annotation.SkillPacket;
import io.github.junzzzz.skillapi.common.Message;
import io.github.junzzzz.skillapi.packet.base.AbstractPacket;
import io.github.junzzzz.skillapi.skill.AbstractSkill;
import io.github.junzzzz.skillapi.skill.PlayerSkills;
import io.github.junzzzz.skillapi.skill.Skills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket
public class PlayerLearnSkillPacket extends AbstractPacket {
    @JsonProperty("n")
    private String unlocalizedSkillName;

    @Override
    protected void run(EntityPlayer player, Side from) {
        if (from.isServer()) {
            AbstractSkill skill = Skills.get(unlocalizedSkillName);
            if (skill == null) {
                Message.sendTranslation(player, "skill.constant.error.sync");
                return;
            }
            PlayerSkills playerSkill = PlayerSkills.get(player);
            if (playerSkill.learnSkill(skill)) {
                Message.sendTranslation(player, "skill.constant.learnSkill", skill.getLocalizedName());
            }
        }
    }
}
