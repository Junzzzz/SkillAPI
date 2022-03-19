package genericskill;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import genericskill.entity.EntityShockWave;
import skillapi.api.SkillApi;

@Mod(modid = "genericskills", name = "Generic Skills Pack", useMetadata = true, dependencies = "required-after:skillapi")
public final class GenericSkills {
//    public static final String[] skills = {"Creeper Blast", "Levitate", "Summon Wolf", "Super Jump", "Healing Breeze", "Binding Signet", "Unrelenting Force", "Barrage"};

    @EventHandler
    public void init(FMLInitializationEvent event) {
        SkillApi.init(event);
//        SkillRegistry.registerSkill(new SkillCreeperBlast().setName(skills[0]).setTexture("creeperblast"));
//        SkillRegistry.registerSkill(new SkillLevitate().setName(skills[1]).setTexture("levitate"));
//        SkillRegistry.registerSkill(new SkillSummonWolf().setName(skills[2]).setTexture("summonwolf"));
//        SkillRegistry.registerSkill(new SkillSuperJump().setName(skills[3]).setTexture("superjump"));
//        SkillRegistry.registerSkill(new SkillHealingBreeze().setName(skills[4]).setTexture("healingbreeze"));
//        SkillRegistry.registerSkill(new SkillBindingSignet().setName(skills[5]).setTexture("bindingsignet"));
//        SkillRegistry.registerSkill(new SkillUnrelentingForce().setName(skills[6]).setTexture("unrelentingforce"));
//        SkillRegistry.registerSkill(new SkillBarrage().setName(skills[7]).setTexture("barrage"));
        EntityRegistry.registerModEntity(EntityShockWave.class, "FusRoDah", 0, this, 20, 4, true);
//        GameRegistry.addShapelessRecipe(new ItemStack(genSkillBook), Items.gold_ingot, Items.book);
//        GameRegistry.addRecipe(new ItemStack(heritageAmulet), " S ", "S S", "GDG", 'S', Items.string, 'G', Items.gold_ingot, 'D', Items.diamond);
//        GameRegistry.addShapelessRecipe(new ItemStack(manaPotion), Items.glass_bottle, new ItemStack(Items.dye, 1, 4));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SkillApi.preInit(event,"genericskill");
    }
}