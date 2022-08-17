package com.github.junzzzz.skillapi.common;

/**
 * @author Jun
 */
public interface TranslationProxy {
    String format(String translationKey, Object... params);
}
