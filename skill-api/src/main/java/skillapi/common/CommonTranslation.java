package skillapi.common;

import net.minecraft.util.StatCollector;

/**
 * @author Jun
 */
public class CommonTranslation implements TranslationProxy {
    @Override
    public String format(String translationKey, Object... params) {
        return StatCollector.translateToLocalFormatted(translationKey, params);
    }
}
