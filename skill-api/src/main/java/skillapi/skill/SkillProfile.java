package skillapi.skill;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import skillapi.api.annotation.SkillParam;
import skillapi.common.SkillLog;
import skillapi.common.SkillRuntimeException;
import skillapi.utils.JsonUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jun
 */
@JsonSerialize(using = SkillProfile.Serializer.class)
@JsonDeserialize(using = SkillProfile.Deserializer.class)
public class SkillProfile {
    /**
     * Skill config name
     */
    protected String name;
    protected String lastUpdater;
    protected long lastUpdateTime;

    /**
     * Number of auto-increment skills
     */
    protected int num;

    protected final Map<String, String> constant;
    protected final Map<String, DynamicSkill> dynamicSkills;

    public SkillProfile() {
        this.constant = new HashMap<>(32);
        this.dynamicSkills = new HashMap<>(16);
    }

    SkillProfile(Map<String, String> constant, Map<String, DynamicSkill> dynamicSkills) {
        this.constant = constant;
        this.dynamicSkills = dynamicSkills;
    }

    public SkillProfile(String name, String lastUpdater, long lastUpdateTime) {
        this();
        this.name = name;
        this.lastUpdater = lastUpdater;
        this.lastUpdateTime = lastUpdateTime;
    }

    public synchronized DynamicSkill put(DynamicSkillBuilder builder) {
        DynamicSkill skill = builder.build(this);
        this.dynamicSkills.put(skill.getUnlocalizedName(), skill);
        this.constant.put(skill.getUniqueId() + ".name", builder.getName());
        this.constant.put(skill.getUniqueId() + ".description", builder.getDescription());
        return skill;
    }

    public synchronized void remove(DynamicSkillBuilder builder) {
        this.dynamicSkills.remove(Skills.PREFIX_DYNAMIC + builder.getUniqueId());
        this.constant.remove(builder.getUniqueId() + ".name");
        this.constant.remove(builder.getUniqueId() + ".description");
    }

    public String getLocalizedName(DynamicSkill skill) {
        return this.constant.get(skill.getUniqueId() + ".name");
    }

    public String getDescription(DynamicSkill skill) {
        return this.constant.get(skill.getUniqueId() + ".description");
    }

    public List<DynamicSkillBuilder> getDynamicSkillBuilders() {
        return Collections.unmodifiableList(
                this.dynamicSkills.values().stream()
                        .map(e -> new DynamicSkillBuilder(this, e))
                        .sorted(Comparator.comparingInt(DynamicSkillBuilder::getUniqueId))
                        .collect(Collectors.toList())
        );
    }

    protected SkillProfile copy() {
        SkillProfile c = new SkillProfile(new HashMap<>(this.constant), new HashMap<>(this.dynamicSkills));
        c.name = this.name;
        c.lastUpdater = this.lastUpdater;
        c.lastUpdateTime = this.lastUpdateTime;
        c.num = this.num;
        return c;
    }

    public String getName() {
        return name;
    }

    public synchronized int getSkillUniqueId() {
        return ++this.num;
    }

    public SkillProfileInfo getInfo() {
        return new SkillProfileInfo(this.name, this.lastUpdater, this.lastUpdateTime);
    }

    public static SkillProfileInfo getInfo(byte[] jsonBytes) {
        return getInfo(new String(jsonBytes, StandardCharsets.UTF_8));
    }

    public static SkillProfileInfo getInfo(String json) {
        try {
            JsonNode node = JsonUtils.getMapper().readTree(json);
            String name = node.get("name").asText("error");
            String lastUpdater = node.get("lastUpdater").asText("");
            long lastUpdateTime = node.get("lastUpdateTime").asLong(System.currentTimeMillis());
            return new SkillProfileInfo(name, lastUpdater, lastUpdateTime);
        } catch (IOException e) {
            SkillLog.warn("Getting profile information failed. Json: %s", json);
            return new SkillProfileInfo("error", "", System.currentTimeMillis());
        }
    }

    public static SkillProfile valueOf(byte[] data) throws IOException {
        return JsonUtils.getMapper().readValue(new String(data, StandardCharsets.UTF_8), SkillProfile.class);
    }

    public synchronized byte[] getBytes() throws JsonProcessingException {
        return JsonUtils.getMapper().writeValueAsString(this).getBytes(StandardCharsets.UTF_8);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SkillProfileInfo {
        private String name;
        private String lastUpdater;
        private long lastUpdateTime;
    }

    static class Serializer extends JsonSerializer<SkillProfile> {
        @Override
        public void serialize(SkillProfile skillProfile, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("name", skillProfile.name);
            jsonGenerator.writeStringField("lastUpdater", skillProfile.lastUpdater);
            jsonGenerator.writeNumberField("lastUpdateTime", skillProfile.lastUpdateTime);
            jsonGenerator.writeNumberField("num", skillProfile.num);

            jsonGenerator.writeObjectField("constant", skillProfile.constant);

            jsonGenerator.writeArrayFieldStart("skills");
            for (DynamicSkill skill : skillProfile.dynamicSkills.values()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("id", skill.getUniqueId());
                jsonGenerator.writeNumberField("mana", skill.getMana());
                jsonGenerator.writeNumberField("cooldown", skill.getCooldown());
                jsonGenerator.writeNumberField("charge", skill.getCharge());

                jsonGenerator.writeArrayFieldStart("effects");
                for (SkillEffect effect : skill.effects) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeStringField("name", effect.getUnlocalizedName());
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

    static class Deserializer extends JsonDeserializer<SkillProfile> {
        @Override
        public SkillProfile deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            SkillProfile config = new SkillProfile();
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            config.name = node.get("name").textValue();
            config.lastUpdater = node.get("lastUpdater").textValue();
            config.lastUpdateTime = node.get("lastUpdateTime").longValue();
            config.num = node.get("num").intValue();

            TreeNode constantNode = node.get("constant");
            Map<String, String> constant = new HashMap<>(32);
            constantNode.fieldNames().forEachRemaining(field ->
                    constant.put(field, ((TextNode) constantNode.get(field)).textValue())
            );
            node.get("skills").elements().forEachRemaining(skillNode -> {
                DynamicSkillBuilder builder = new DynamicSkillBuilder(skillNode.get("id").intValue());
                builder.setMana(skillNode.get("mana").intValue());
                builder.setCooldown(skillNode.get("cooldown").longValue());
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