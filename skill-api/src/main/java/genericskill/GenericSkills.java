package genericskill;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import genericskill.common.CommonProxy;
import genericskill.entity.EntityShockWave;
import genericskill.skill.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import skillapi.SkillRegistry;

import static skillapi.item.SkillItemLoader.*;

@Mod(modid = "genericskills", name = "Generic Skills Pack", useMetadata = true, dependencies = "required-after:skillapi")
public final class GenericSkills {
    public static final String[] skills = {"Creeper Blast", "Levitate", "Summon Wolf", "Super Jump", "Healing Breeze", "Binding Signet", "Unrelenting Force", "Barrage"};

    @SidedProxy(clientSide = "genericskill.client.ClientProxy", serverSide = "genericskill.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        SkillRegistry.registerSkill(new SkillCreeperBlast().setName(skills[0]).setTexture("creeperblast"));
        SkillRegistry.registerSkill(new SkillLevitate().setName(skills[1]).setTexture("levitate"));
        SkillRegistry.registerSkill(new SkillSummonWolf().setName(skills[2]).setTexture("summonwolf"));
        SkillRegistry.registerSkill(new SkillSuperJump().setName(skills[3]).setTexture("superjump"));
        SkillRegistry.registerSkill(new SkillHealingBreeze().setName(skills[4]).setTexture("healingbreeze"));
        SkillRegistry.registerSkill(new SkillBindingSignet().setName(skills[5]).setTexture("bindingsignet"));
        SkillRegistry.registerSkill(new SkillUnrelentingForce().setName(skills[6]).setTexture("unrelentingforce"));
        SkillRegistry.registerSkill(new SkillBarrage().setName(skills[7]).setTexture("barrage"));
        EntityRegistry.registerModEntity(EntityShockWave.class, "FusRoDah", 0, this, 20, 4, true);
        GameRegistry.addShapelessRecipe(new ItemStack(genSkillBook), Items.gold_ingot, Items.book);
        GameRegistry.addRecipe(new ItemStack(heritageAmulet), " S ", "S S", "GDG", 'S', Items.string, 'G', Items.gold_ingot, 'D', Items.diamond);
        GameRegistry.addShapelessRecipe(new ItemStack(manaPotion), Items.glass_bottle, new ItemStack(Items.dye, 1, 4));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);


        // Update
        if (event.getSourceFile().getName().endsWith(".jar") && event.getSide().isClient()) {
            try {
                Class.forName("mods.mud.ModUpdateDetector").getDeclaredMethod("registerMod", ModContainer.class, String.class, String.class).invoke(null,
                        FMLCommonHandler.instance().findContainerFor(this),
                        "https://raw.github.com/GotoLink/SkillAPI/master/Pack_update.xml",
                        "https://raw.github.com/GotoLink/SkillAPI/master/Pack_changelog.md"
                );
            } catch (Throwable ignored) {
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void remap(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping missingMapping : event.get()) {
            if (missingMapping.type == GameRegistry.Type.ITEM) {
                missingMapping.remap(GameData.getItemRegistry().getObject(missingMapping.name.replace(" ", "")));
            }
        }
    }
}