package com.github.junzzzz.skillapi.packet.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParameterizedDataType {
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ParameterizedDataType[] parameterizedTypes;

    public static ParameterizedDataType get(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            ParameterizedDataType[] types = new ParameterizedDataType[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {
                types[i] = get(actualTypeArguments[i]);
            }
            return new ParameterizedDataType(parameterizedType.getRawType().getTypeName(), types);
        } else {
            return new ParameterizedDataType(type.getTypeName(), new ParameterizedDataType[0]);
        }
    }
}