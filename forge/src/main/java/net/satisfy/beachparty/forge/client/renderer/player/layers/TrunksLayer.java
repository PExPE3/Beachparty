package net.satisfy.beachparty.forge.client.renderer.player.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.satisfy.beachparty.client.model.BikiniModel;
import net.satisfy.beachparty.client.model.TrunksModel;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.antlr.v4.runtime.misc.NotNull;

public class TrunksLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final TrunksModel<T> model;

    public TrunksLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new TrunksModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(TrunksModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        poseStack.pushPose();
        renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, 0.0f, 0.0f, 1.0f);
        poseStack.popPose();
    }

    @Override
    protected ResourceLocation getTextureLocation(@NotNull T entity) {
        return new BeachpartyIdentifier("textures/models/armor/trunks.png");
    }
}
