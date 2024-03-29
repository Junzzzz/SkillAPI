package io.github.junzzzz.skillapi.common;

/**
 * @author Jun
 */
public class SkillRuntimeException extends RuntimeException {
    public SkillRuntimeException(Throwable cause) {
        super(cause);
    }

    public SkillRuntimeException(String message) {
        super(message);
    }

    public SkillRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkillRuntimeException(String format, Object... args) {
        super(String.format(format, args));
    }

    public SkillRuntimeException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }
}
