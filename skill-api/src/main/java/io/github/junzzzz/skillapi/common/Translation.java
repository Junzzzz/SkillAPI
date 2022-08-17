package io.github.junzzzz.skillapi.common;

import cpw.mods.fml.common.SidedProxy;
import io.github.junzzzz.skillapi.Application;

/**
 * @author Jun
 */
public final class Translation {
    @SidedProxy(
            modId = Application.MOD_ID,
            serverSide = Application.PACKAGE_PREFIX + "skillapi.common.CommonTranslation",
            clientSide = Application.PACKAGE_PREFIX + "skillapi.common.ClientTranslation"
    )
    private static TranslationProxy proxy;

    public static String format(String translationKey, Object... params) {
        return proxy.format(translationKey, params);
    }
}
