package skillapi.common;

import cpw.mods.fml.common.SidedProxy;
import skillapi.Application;

/**
 * @author Jun
 */
public final class Translation {
    @SidedProxy(
            modId = Application.MOD_ID,
            serverSide = "skillapi.common.CommonTranslation",
            clientSide = "skillapi.common.ClientTranslation"
    )
    private static TranslationProxy proxy;

    public static String format(String translationKey, Object... params) {
        return proxy.format(translationKey, params);
    }
}
