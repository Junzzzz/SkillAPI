package skillapi.skill;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.TextNode;
import skillapi.api.annotation.SkillParam;
import skillapi.common.SkillRuntimeException;
import skillapi.utils.JsonUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jun
 */
@JsonSerialize(using = DynamicSkillConfig.Serializer.class)
@JsonDeserialize(using = DynamicSkillConfig.Deserializer.class)
public class DynamicSkillConfig {
    /**
     * Skill config name
     */
    protected String name;
    protected final Map<String, String> constant;
    protected final Set<DynamicSkill> dynamicSkills;

    public DynamicSkillConfig() {
        this.constant = new HashMap<>(32);
        this.dynamicSkills = new HashSet<>(16);
    }

    public DynamicSkillConfig(Map<String, String> constant, Set<DynamicSkill> dynamicSkills) {
        this.constant = constant;
        this.dynamicSkills = dynamicSkills;
    }

    public synchronized DynamicSkill put(DynamicSkillBuilder builder) {
        DynamicSkill skill = builder.build();
        this.dynamicSkills.remove(skill);
        this.dynamicSkills.add(skill);
        this.constant.put(skill.getUniqueId() + ".name", builder.getName());
        this.constant.put(skill.getUniqueId() + ".description", builder.getDescription());
        return skill;
    }

    public String getSkillName(DynamicSkill skill) {
        return this.constant.get(skill.getUniqueId() + ".name");
    }

    public String getSkillDescription(DynamicSkill skill) {
        return this.constant.get(skill.getUniqueId() + ".description");
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public List<DynamicSkillBuilder> getDynamicSkillBuilders() {
        return Collections.unmodifiableList(
                this.dynamicSkills.stream()
                        .map(e -> new DynamicSkillBuilder(this, e)).collect(Collectors.toList())
        );
    }

    protected DynamicSkillConfig copy() {
        DynamicSkillConfig c = new DynamicSkillConfig(new HashMap<>(this.constant), new HashSet<>(this.dynamicSkills));
        c.name = this.name;
        return c;
    }

    public static DynamicSkillConfig valueOf(byte[] data) throws IOException {
        return JsonUtils.getMapper().readValue(data, DynamicSkillConfig.class);
    }

    public synchronized byte[] getBytes() throws JsonProcessingException {
        return JsonUtils.getMapper().writeValueAsBytes(this);
    }

    static class Serializer extends JsonSerializer<DynamicSkillConfig> {
        @Override
        public void serialize(DynamicSkillConfig dynamicSkillConfig, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("name", dynamicSkillConfig.name);
            jsonGenerator.writeObjectField("constant", dynamicSkillConfig.constant);

            jsonGenerator.writeArrayFieldStart("skills");
            for (DynamicSkill skill : dynamicSkillConfig.dynamicSkills) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("id", skill.getUniqueId());
                jsonGenerator.writeNumberField("mana", skill.getMana());
                jsonGenerator.writeNumberField("cooldown", skill.getCooldown());
                jsonGenerator.writeNumberField("charge", skill.getCharge());

                jsonGenerator.writeArrayFieldStart("effects");
                for (SkillEffect effect : skill.effects) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeStringField("name", effect.getName());
                    jsonGenerator.writeObjectFieldStart("fields");
                    Class<? extends SkillEffect> clz = effect.getClass();
                    for (Field field : clz.getDeclaredFields()) {
                        if (field.getAnnotation(SkillParam.class) == null) {
                            continue;
                        }
                        field.setAccessible(true);
                        try {
                            jsonGenerator.writeObjectField(field.getName(), field.get(effect));
                        } catch (IllegalAccessException e) {
                            throw new SkillRuntimeException("Failed to serialize dynamic skill config", e);
                        }
                    }
                    jsonGenerator.writeEndObject();

                    jsonGenerator.writeEndObject();
                }
                jsonGenerator.writeEndArray();

                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }
    }

    static class Deserializer extends JsonDeserializer<DynamicSkillConfig> {
        @Override
        public DynamicSkillConfig deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            DynamicSkillConfig config = new DynamicSkillConfig();
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            config.name = node.get("name").textValue();
            TreeNode constantNode = node.get("constant");
            Map<String, String> constant = new HashMap<>(32);
            constantNode.fieldNames().forEachRemaining(field ->
                    constant.put(field, ((TextNode) constantNode.get(field)).textValue())
            );
            node.get("skills").elements().forEachRemaining(skillNode -> {
                DynamicSkillBuilder builder = new DynamicSkillBuilder(skillNode.get("id").intValue());
                builder.setMana(skillNode.get("mana").intValue());
                builder.setCooldown(skillNode.get("cooldown").intValue());
                builder.setCharge(skillNode.get("charge").intValue());
                builder.setName(constant.get(builder.getUniqueId() + ".name"));
                builder.setDescription(constant.get(builder.getUniqueId() + ".description"));

                int index = 0;
                Iterator<JsonNode> effects = skillNode.get("effects").elements();
                while (effects.hasNext()) {
                    JsonNode effectNode = effects.next();
                    builder.addEffect(Skills.getSkillEffect(effectNode.get("name").textValue()));
                    Iterator<Map.Entry<String, JsonNode>> fields = effectNode.get("fields").fields();
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> entry = fields.next();
                        builder.setParam(index, entry.getKey(), entry.getValue().asText());
                    }
                    index++;
                }
                config.put(builder);
            });
            return config;
        }
    }
}