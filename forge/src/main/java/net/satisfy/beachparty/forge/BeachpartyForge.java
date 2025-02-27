package net.satisfy.beachparty.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.core.block.BeachTowelBlock;
import net.satisfy.beachparty.core.entity.PalmBoatEntity;
import net.satisfy.beachparty.core.registry.CompostablesRegistry;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.forge.registry.BeachpartyConfig;
import net.satisfy.beachparty.platform.forge.PlatformHelperImpl;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

@Mod(Beachparty.MOD_ID)
public class BeachpartyForge {
    public BeachpartyForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Beachparty.MOD_ID, modEventBus);
        PlatformHelperImpl.ENTITY_TYPES.register(modEventBus);
        BeachpartyConfig.loadConfig(BeachpartyConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("beachparty.toml").toString());

        Beachparty.init();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
    }

    private static class SimpleBeachpartyCurio implements ICurioItem {}
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CompostablesRegistry::init);
        CuriosApi.registerCurio(ObjectRegistry.BIKINI.get(), new SimpleBeachpartyCurio());
        CuriosApi.registerCurio(ObjectRegistry.TRUNKS.get(), new SimpleBeachpartyCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_BLUE.get(), new SimpleBeachpartyCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_PINK.get(), new SimpleBeachpartyCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_STRIPPED.get(), new SimpleBeachpartyCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_PELICAN.get(), new SimpleBeachpartyCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_AXOLOTL.get(), new SimpleBeachpartyCurio());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (PalmBoatEntity.Type type : PalmBoatEntity.Type.values()) {
            event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(Beachparty.MOD_ID, type.getModelLocation()), "main"), BoatModel::createBodyModel);
            event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(Beachparty.MOD_ID, type.getChestModelLocation()), "main"), ChestBoatModel::createBodyModel);
        }
    }

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.getBlock() == ObjectRegistry.RADIO.get()) {
            event.setCanceled(true);
        }
    }

    @Mod.EventBusSubscriber(modid = Beachparty.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEventsHandler {

        @SubscribeEvent
        public static void playerSetSpawn(PlayerSetSpawnEvent event) {
            Level level = event.getEntity().level();

            if (event.getNewSpawn() != null) {
                Block block = level.getBlockState(event.getNewSpawn()).getBlock();

                if (!level.isClientSide && block instanceof BeachTowelBlock && !event.isForced()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
