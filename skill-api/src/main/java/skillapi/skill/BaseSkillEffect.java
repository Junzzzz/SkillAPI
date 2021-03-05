package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public abstract class BaseSkillEffect {
    private String name;
    private Integer[] staticParams;

    protected final void init(List<Integer> params) {
        this.name = initName();
        if (!this.name.matches(StringUtils.DEFAULT_REGEX_NAME)) {
            throw new SkillRuntimeException("Invalid skill effect name: %s", this.name);
        }
        if (params.size() != initParamNum()) {
            throw new SkillRuntimeException("The number of parameters does not match. Required: %d. Provide: %d", initParamNum(), params.size());
        }
        this.staticParams = params.toArray(new Integer[0]);
    }

    public String getName() {
        return this.name;
    }

    protected int getStaticParam(int index) {
        return staticParams[index];
    }

    protected abstract String initName();

    protected abstract int initParamNum();

    protected abstract void effect(EntityPlayer player, Map<String, Object> params);
}
