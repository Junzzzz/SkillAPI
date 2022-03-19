package skillapi.skill;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import skillapi.common.SkillLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jun
 */
public class SkillExtraInfo {
    private final static Map<String, ExtraObject> EMPTY_MAP = new HashMap<>(0);
    private final static SkillExtraInfo EMPTY_INFO = new SkillExtraInfo(null);

    private final Map<String, ExtraObject> extraInfo;

    public SkillExtraInfo() {
        this.extraInfo = new HashMap<>(8);
    }

    public SkillExtraInfo(Map<String, ExtraObject> extraInfo) {
        this.extraInfo = extraInfo == null ? EMPTY_MAP : extraInfo;
    }

    public static SkillExtraInfo empty() {
        return EMPTY_INFO;
    }

    public static SkillExtraInfo get(Map<String, ExtraObject> extraInfo) {
        return extraInfo == null ? EMPTY_INFO : new SkillExtraInfo(extraInfo);
    }

    public void put(String name, Object object) {
        ExtraObject old = this.extraInfo.get(name);
        if (old == null) {
            this.extraInfo.put(name, new ExtraObject(object.getClass(), object));
        } else {
            SkillLog.error("Contains conflicting keys: %s. If you want to replace value, please use replace() function.", name);
        }
    }

    public void replace(String name, Object object) {
        ExtraObject old = this.extraInfo.get(name);
        if (old != null) {
            old.setClz(object.getClass());
            old.setObj(object);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) this.extraInfo.get(name).obj;
    }

    public ExtraObject getExtraObject(String name) {
        return this.extraInfo.get(name);
    }

    public boolean contains(String name) {
        return this.extraInfo.containsKey(name);
    }

    public boolean contains(String name, Class<?> type) {
        ExtraObject extraObject = this.extraInfo.get(name);
        return extraObject.clz == type;
    }

    public void reset() {
        this.extraInfo.clear();
    }

    Map<String, ExtraObject> getMap() {
        return this.extraInfo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonSerialize(using = ExtraObjectSerializer.class)
    @JsonDeserialize(using = ExtraObjectDeserializer.class)
    public static class ExtraObject {
        Class<?> clz;
        Object obj;
    }

    static class ExtraObjectSerializer extends JsonSerializer<ExtraObject> {
        @Override
        public void serialize(ExtraObject infoObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeString(infoObject.clz.getName());
            jsonGenerator.writeObject(infoObject.obj);

            jsonGenerator.writeEndObject();
        }
    }

    static class ExtraObjectDeserializer extends JsonDeserializer<ExtraObject> {
        @Override
        public ExtraObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.readValueAsTree();
            ExtraObject extraObject = new ExtraObject();
            try {
                extraObject.setClz(deserializationContext.findClass(node.get(0).textValue()));
            } catch (ClassNotFoundException e) {
                throw new IOException("Class not found", e);
            }
            Object obj = node.get(1).traverse(jsonParser.getCodec()).readValueAs(extraObject.clz);
            extraObject.setObj(obj);
            return extraObject;
        }
    }
}
