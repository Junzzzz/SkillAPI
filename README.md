# SkillAPI

Minecraft动态技能编写MOD

## 接口用法

### 初始化

在初始化监听事件中增加以下内容

```java
@Mod(modid = "demo", name = "Demo", useMetadata = true, dependencies = "required-after:skillapi")
public final class DemoMod {
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // 扫面全部包
        SkillApi.preInit(event);
        // 或者 限定扫描范围
        SkillApi.preInit(event, "demo");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        SkillApi.init(event);
    }
}
```

上面部分主要用于收集一些MOD的基本信息

### 创建一个静态技能

如下列代码所示
- 使用`@StaticSkill`来标记静态技能类
- 在初始化函数`init`中设置技能的基本信息
- 在效果函数中添加你所要产生的效果，示例是向玩家发送一段文本

```java
@StaticSkill
public class DemoStaticSkill extends AbstractStaticSkill {
    @Override
    protected void init(StaticSkillBuilder builder) {
        builder.mana(1)
                .cooldown(1)
                .charge(1);
    }

    @Override
    public void effect(EntityPlayer player, EntityLivingBase target) {
        player.addChatComponentMessage(new ChatComponentText("Static Skill!"));
    }
}
```

### 创建一个技能效果

如下列代码所示

- 使用`@SkillEffect`来标记技能效果类
- 使用`@SkillParam`来标记可变参数，在配置界面将会增加该变量
- 在`canUnleash`函数中设置该技能效果执行前提
- `unleash`函数中的返回值`return true`表示执行成功，若返回`false`则一系列技能效果执行至此中断并且不会执行`afterUnleash`函数
- 在`afterUnleash`函数将在所有技能效果释放完成后执行，根据所需使用

```java
@SkillEffect
public class DemoSkillEffect extends AbstractSkillEffect {
    @SkillParam
    private int demoParam;

    @Override
    public boolean canUnleash(EntityPlayer player, EntityLivingBase target) {
        return target != null;
    }

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target) {
        player.addChatComponentMessage(new ChatComponentText("param: " + demoParam));
        return true;
    }

    @Override
    public void afterUnleash(EntityPlayer player, EntityLivingBase target) {
        // Default: none
    }
}
```

### 创建一个自定义数据包

该部分对FML提供的`Packet`接收事件做了封装，无需进行繁琐的定义，更不需要进行手动变量序列化/反序列化

如下列代码所示
- 使用`@SkillPacket`来标记数据包类，并使该类继承`AbstractPacket`
- 在`run`函数中填写接收数据包后所执行的内容

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket
public class DemoPacket extends AbstractPacket {
    private String testString;

    @Override
    protected void run(EntityPlayer player, Side from) {
        player.addChatComponentMessage(new ChatComponentText(testString));
    }
}
```

默认的序列化工具是`Jackson`，所有对象将会转换成`Json`格式进行传输，因此需要定义一个空的构造器和`Getter/Setter`方法。示例使用了`lombok`的注解去自动生成了上述内容。

#### 自定义序列化器

以下代码为本MOD的默认序列化器，可参照该内容自定义特殊的序列化器

使用时只需要在注解中填写该类即可

如：`@SkillPacket(serializer = JsonPacketSerializer.class)`

```java
public class JsonPacketSerializer implements PacketSerializer<AbstractPacket> {
    @Override
    public void serialize(AbstractPacket packet, ByteBuf buffer) throws Exception {
        PacketSerializer.writeString(buffer, JsonUtils.getMapper().writeValueAsString(packet));
    }

    @Override
    public AbstractPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        return (AbstractPacket) JsonUtils.getMapper().readValue(PacketSerializer.readString(buffer), packetClass);
    }
}
```

### 创建一个监听事件

免去了原有的手动监听函数设置，并且不需要再去手动区分`FML`还是`Minecraft`事件，只需要在类上添加`@SkillEvent`即可，其余部分与原有的使用一致

```java
@SkillEvent
public class DemoEvents {
    @SubscribeEvent
    public void onConstructing(EntityEvent.EntityConstructing event) {
        // TODO
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        // TODO
    }
    
    // ...
}
```