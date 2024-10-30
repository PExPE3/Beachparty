package net.satisfy.beachparty.fabric.registry;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.registry.ObjectRegistry;

public class FabricBoatRegistry {
    public static final ResourceLocation FLOATY_BOAT_ID = new ResourceLocation(Beachparty.MOD_ID, "floaty_boat");
    public static final ResourceLocation FLOATY_CHEST_BOAT_ID = new ResourceLocation(Beachparty.MOD_ID, "floaty_chest_boat");
    public static final ResourceKey<TerraformBoatType> FLOATY_BOAT_KEY = TerraformBoatTypeRegistry.createKey(FLOATY_BOAT_ID);

    public static void registerBoats() {
        TerraformBoatType floatyBoat = new TerraformBoatType.Builder()
                .item(ObjectRegistry.FLOATY.get())
                .chestItem(ObjectRegistry.FLOATY_CHEST_BOAT.get())
                .planks(Blocks.KELP.asItem())
                .build();
        Registry.register(TerraformBoatTypeRegistry.INSTANCE, FLOATY_BOAT_KEY, floatyBoat);
    }
}