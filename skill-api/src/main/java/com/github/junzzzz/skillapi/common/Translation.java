package com.github.junzzzz.skillapi.common;

import com.github.junzzzz.skillapi.Application;
import cpw.mods.fml.common.SidedProxy;

import static com.github.junzzzz.skillapi.Application.PACKAGE_PREFIX;

/**
 * @author Jun
 */
public final class Translation {
    @SidedProxy(
            modId = Application.MOD_ID,
            serverSide = PACKAGE_PREFIX + "skillapi.common.CommonTranslation",
            clientSide = PACKAGE_PREFIX + "skillapi.common.ClientTranslation"
    )
    private static TranslationProxy proxy;

    public static String format(String translationKey, Object... params) {
        return proxy.format(translationKey, params);
    }
}
