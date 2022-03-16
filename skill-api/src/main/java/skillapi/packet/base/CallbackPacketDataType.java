package skillapi.packet.base;

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
public class CallbackPacketDataType {
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private CallbackPacketDataType[] parameterizedTypes;

    public static CallbackPacketDataType get(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            CallbackPacketDataType[] types = new CallbackPacketDataType[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {
                types[i] = get(actualTypeArguments[i]);
            }
            return new CallbackPacketDataType(parameterizedType.getRawType().getTypeName(), types);
        } else {
            return new CallbackPacketDataType(type.getTypeName(), new CallbackPacketDataType[0]);
        }
    }
}