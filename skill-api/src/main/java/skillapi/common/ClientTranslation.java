package skillapi.common;

import net.minecraft.client.resources.I18n;

/**
 * @author Jun
 */
public class ClientTranslation implements TranslationProxy {
    @Override
    public String format(String translationKey, Object... params) {
        return I18n.format(translationKey, params);
    }
}
