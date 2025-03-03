package net.satisfy.beachparty.forge.client.integration;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.model.RubberRingColoredModel;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosRubberRingBlueRenderer implements ICurioRenderer {

    private final RubberRingColoredModel<LivingEntity> model;
    private final ResourceLocation texture;

    public CuriosRubberRingBlueRenderer() {
        this.model = new RubberRingColoredModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RubberRingColoredModel.LAYER_LOCATION));
        this.texture = new BeachpartyIdentifier("textures/models/armor/rubber_ring_blue.png");
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
                                                                          RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
                                                                          int light, float limbSwing, float limbSwingAmount, float partialTicks,
                                                                          float ageInTicks, float netHeadYaw, float headPitch) {
        if (!slotContext.identifier().equals("belt")) return;
        if (stack.isEmpty() || !stack.is(net.satisfy.beachparty.core.registry.ObjectRegistry.RUBBER_RING_BLUE.get()))
            return;
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity == null) return;
        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        matrixStack.pushPose();
        matrixStack.translate(0.1F, -0.6F, -0.5F);
        model.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(texture)),
                light, OverlayTexture.NO_OVERLAY, 0.0f, 0.0f, 1.0f, 1.0f);
        matrixStack.popPose();
    }
}
