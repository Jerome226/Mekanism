package mekanism.common.recipe.impl;

import java.util.Map;
import java.util.function.Consumer;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import mekanism.common.recipe.ISubRecipeProvider;
import mekanism.common.registries.MekanismGases;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.common.Tags;

class ChemicalInjectorRecipeProvider implements ISubRecipeProvider {

    @Override
    public void addRecipes(Consumer<FinishedRecipe> consumer) {
        String basePath = "injecting/";
        //Brick -> clay ball
        ItemStackChemicalToItemStackRecipeBuilder.injecting(
              IngredientCreatorAccess.item().from(Tags.Items.INGOTS_BRICK),
              IngredientCreatorAccess.gas().from(MekanismTags.Gases.WATER_VAPOR, 1),
              new ItemStack(Items.CLAY_BALL)
        ).build(consumer, Mekanism.rl(basePath + "brick_to_clay_ball"));
        //Dirt -> clay
        ItemStackChemicalToItemStackRecipeBuilder.injecting(
              IngredientCreatorAccess.item().from(Blocks.DIRT),
              IngredientCreatorAccess.gas().from(MekanismTags.Gases.WATER_VAPOR, 1),
              new ItemStack(Blocks.CLAY)
        ).build(consumer, Mekanism.rl(basePath + "dirt_to_clay"));
        //Gunpowder -> sulfur
        ItemStackChemicalToItemStackRecipeBuilder.injecting(
              IngredientCreatorAccess.item().from(Tags.Items.GUNPOWDER),
              IngredientCreatorAccess.gas().from(MekanismGases.HYDROGEN_CHLORIDE, 1),
              MekanismItems.SULFUR_DUST.getItemStack()
        ).build(consumer, Mekanism.rl(basePath + "gunpowder_to_sulfur"));
        //Terracotta -> clay
        ItemStackChemicalToItemStackRecipeBuilder.injecting(
              IngredientCreatorAccess.item().from(Blocks.TERRACOTTA),
              IngredientCreatorAccess.gas().from(MekanismTags.Gases.WATER_VAPOR, 1),
              new ItemStack(Blocks.CLAY)
        ).build(consumer, Mekanism.rl(basePath + "terracotta_to_clay"));
        addChemicalInjectorConcreteRecipes(consumer, basePath + "concrete/");
        addChemicalInjectorCoralRevivalRecipes(consumer, basePath + "coral/");
        addChemicalInjectorOxidizingRecipe(consumer, basePath + "oxidizing/");
    }

    private void addChemicalInjectorConcreteRecipes(Consumer<FinishedRecipe> consumer, String basePath) {
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.BLACK_CONCRETE_POWDER, Blocks.BLACK_CONCRETE, "black");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.BLUE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE, "blue");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.BROWN_CONCRETE_POWDER, Blocks.BROWN_CONCRETE, "brown");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.CYAN_CONCRETE_POWDER, Blocks.CYAN_CONCRETE, "cyan");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.GRAY_CONCRETE_POWDER, Blocks.GRAY_CONCRETE, "gray");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.GREEN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE, "green");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE, "light_blue");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE, "light_gray");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.LIME_CONCRETE_POWDER, Blocks.LIME_CONCRETE, "lime");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE, "magenta");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.ORANGE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE, "orange");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.PINK_CONCRETE_POWDER, Blocks.PINK_CONCRETE, "pink");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.PURPLE_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE, "purple");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.RED_CONCRETE_POWDER, Blocks.RED_CONCRETE, "red");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.WHITE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE, "white");
        addChemicalInjectorConcreteRecipe(consumer, basePath, Blocks.YELLOW_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE, "yellow");
    }

    private void addChemicalInjectorConcreteRecipe(Consumer<FinishedRecipe> consumer, String basePath, ItemLike powder, ItemLike concrete, String name) {
        ItemStackChemicalToItemStackRecipeBuilder.injecting(
              IngredientCreatorAccess.item().from(powder),
              IngredientCreatorAccess.gas().from(MekanismTags.Gases.WATER_VAPOR, 1),
              new ItemStack(concrete)
        ).build(consumer, Mekanism.rl(basePath + name));
    }

    private void addChemicalInjectorCoralRevivalRecipes(Consumer<FinishedRecipe> consumer, String basePath) {
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK, 5);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK, 5);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK, 5);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK, 5);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_BLOCK, 5);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Items.DEAD_BRAIN_CORAL_FAN, Items.BRAIN_CORAL_FAN, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Items.DEAD_BUBBLE_CORAL_FAN, Items.BUBBLE_CORAL_FAN, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Items.DEAD_FIRE_CORAL_FAN, Items.FIRE_CORAL_FAN, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Items.DEAD_HORN_CORAL_FAN, Items.HORN_CORAL_FAN, 3);
        addChemicalInjectorCoralRevivalRecipe(consumer, basePath, Items.DEAD_TUBE_CORAL_FAN, Items.TUBE_CORAL_FAN, 3);
    }

    private void addChemicalInjectorCoralRevivalRecipe(Consumer<FinishedRecipe> consumer, String basePath, ItemLike dead, ItemLike living, int water) {
        ItemStackChemicalToItemStackRecipeBuilder.injecting(
              IngredientCreatorAccess.item().from(dead),
              IngredientCreatorAccess.gas().from(MekanismTags.Gases.WATER_VAPOR, water),
              new ItemStack(living)
        ).build(consumer, Mekanism.rl(basePath + living.asItem().getRegistryName().getPath()));
    }

    private void addChemicalInjectorOxidizingRecipe(Consumer<FinishedRecipe> consumer, String basePath) {
        //Generate baseline recipes from weathering recipe set
        GasStackIngredient oxygen = IngredientCreatorAccess.gas().from(MekanismGases.OXYGEN, 1);
        for (Map.Entry<Block, Block> entry : WeatheringCopper.NEXT_BY_BLOCK.get().entrySet()) {
            Block result = entry.getValue();
            ItemStackChemicalToItemStackRecipeBuilder.injecting(
                  IngredientCreatorAccess.item().from(entry.getKey()),
                  oxygen,
                  new ItemStack(result)
            ).build(consumer, Mekanism.rl(basePath + result.asItem()));
        }
    }
}