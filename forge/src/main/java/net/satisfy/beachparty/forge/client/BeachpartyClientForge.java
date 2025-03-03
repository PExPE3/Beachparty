package net.satisfy.beachparty.forge.client;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.satisfy.beachparty.Beachparty;
import net.satisfy.beachparty.client.BeachPartyClient;
import net.satisfy.beachparty.core.entity.PalmBoatEntity;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.forge.client.integration.CuriosRubberRingBlueRenderer;
import net.satisfy.beachparty.forge.client.integration.CuriosTrunksRenderer;
import net.satisfy.beachparty.forge.client.integration.CuriosBikiniRenderer;
import net.satisfy.beachparty.forge.client.renderer.player.layers.BikiniLayer;
import net.satisfy.beachparty.forge.client.renderer.player.layers.RubberRingBlueLayer;
import net.satisfy.beachparty.forge.client.renderer.player.layers.TrunksLayer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Beachparty.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeachpartyClientForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        BeachPartyClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BeachPartyClient.initClient();
            CuriosRendererRegistry.register(ObjectRegistry.BIKINI.get(), CuriosBikiniRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.TRUNKS.get(), CuriosTrunksRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_BLUE.get(), CuriosRubberRingBlueRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_PINK.get(), CuriosRubberRingBlueRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_STRIPPED.get(), CuriosRubberRingBlueRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_PELICAN.get(), CuriosTrunksRenderer::new);
            CuriosRendererRegistry.register(ObjectRegistry.RUBBER_RING_AXOLOTL.get(), CuriosTrunksRenderer::new);
        });
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (PalmBoatEntity.Type type : PalmBoatEntity.Type.values()) {
            event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(Beachparty.MOD_ID, type.getModelLocation()), "main"), BoatModel::createBodyModel);
            event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(Beachparty.MOD_ID, type.getChestModelLocation()), "main"), ChestBoatModel::createBodyModel);
        }
    }

    @SubscribeEvent
    public static void registerLayersForRenderers(EntityRenderersEvent.AddLayers event) {

        addLayerToPlayerSkin(event, "default", BikiniLayer::new);
        addLayerToPlayerSkin(event, "slim", BikiniLayer::new);

        addLayerToPlayerSkin(event, "default", TrunksLayer::new);
        addLayerToPlayerSkin(event, "slim", TrunksLayer::new);

        addLayerToPlayerSkin(event, "default", RubberRingBlueLayer::new);
        addLayerToPlayerSkin(event, "slim", RubberRingBlueLayer::new);
    }

    private static <E extends Player, M extends HumanoidModel<E>>
    void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName, Function<LivingEntityRenderer<E, M>, ? extends RenderLayer<E, M>> factory) {
        LivingEntityRenderer renderer = event.getSkin(skinName);
        if (renderer != null) renderer.addLayer(factory.apply(renderer));
    }
}
