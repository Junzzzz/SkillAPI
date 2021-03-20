package skillapi.api.gui.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jun
 * @date 2020/11/18.
 */
public abstract class BaseComponent extends GenericGui {
    @Getter
    protected final Layout layout;

    @Getter
    @Setter
    protected boolean visible;

    protected BaseComponent(Layout layout) {
        this.layout = layout;
        this.visible = true;
    }
}
