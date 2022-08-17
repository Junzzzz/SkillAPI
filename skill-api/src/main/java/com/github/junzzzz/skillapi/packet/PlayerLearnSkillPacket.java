package com.github.junzzzz.skillapi.packet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.junzzzz.skillapi.api.annotation.SkillPacket;
import com.github.junzzzz.skillapi.common.Message;
import com.github.junzzzz.skillapi.packet.base.AbstractPacket;
import com.github.junzzzz.skillapi.skill.AbstractSkill;
import com.github.junzzzz.skillapi.skill.PlayerSkills;
import com.github.junzzzz.skillapi.skill.Skills;
import cpw.mods.fml.relauncher.Side;
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
