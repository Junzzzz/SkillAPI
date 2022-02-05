package skillapi.packet;

import com.fasterxml.jackson.annotation.JsonProperty;
import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.common.Message;
import skillapi.packet.base.AbstractPacket;
import skillapi.skill.AbstractSkill;
import skillapi.skill.PlayerSkills;
import skillapi.skill.Skills;

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
