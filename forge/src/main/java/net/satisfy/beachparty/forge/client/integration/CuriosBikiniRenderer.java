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
import net.satisfy.beachparty.client.model.BikiniModel;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosBikiniRenderer implements ICurioRenderer {

    private final BikiniModel<LivingEntity> model;
    private final ResourceLocation texture;

    public CuriosBikiniRenderer() {
        this.model = new BikiniModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BikiniModel.LAYER_LOCATION));
        this.texture = new BeachpartyIdentifier("textures/models/armor/bikini.png");
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity == null) return;

        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        matrixStack.pushPose();
        renderColoredCutoutModel(model, texture, matrixStack, renderTypeBuffer, light, livingEntity, 0.0f, 0.0f, 1.0f);
        matrixStack.popPose();
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void renderColoredCutoutModel(M model, ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float red, float green, float blue) {
        poseStack.pushPose();
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
        poseStack.popPose();
    }
}
