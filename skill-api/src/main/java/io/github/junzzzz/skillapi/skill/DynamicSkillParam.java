package io.github.junzzzz.skillapi.skill;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Jun
 */
@Getter
@Setter
public class DynamicSkillParam {
    protected final Class<?> originalType;
    protected String value;

    public DynamicSkillParam(Object originalParam) {
        this.originalType = originalParam.getClass();
        this.value = originalParam.toString();
    }

    public DynamicSkillParam(Object originalParam, String strValue) {
        this.originalType = originalParam.getClass();
        this.value = strValue;
    }

    public DynamicSkillParam(Class<?> clz) {
        this.originalType = clz;
        this.value = "";
    }

    public DynamicSkillParam(Class<?> clz, String strValue) {
        this.originalType = clz;
        this.value = strValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DynamicSkillParam that = (DynamicSkillParam) o;

        if (!Objects.equals(originalType, that.originalType)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = originalType != null ? originalType.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
