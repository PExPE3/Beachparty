package net.satisfy.beachparty.forge.client.integration;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.model.TrunksModel;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CuriosTrunksRenderer implements ICurioRenderer {

    private final TrunksModel<LivingEntity> model;
    private final ResourceLocation texture;

    public CuriosTrunksRenderer() {
        this.model = new TrunksModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(TrunksModel.LAYER_LOCATION));
        this.texture = new BeachpartyIdentifier("textures/models/armor/trunks.png");
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        LivingEntity entity = slotContext.entity();
        if (entity == null || stack.isEmpty()) return;
        if (!slotContext.identifier().equals("body")) return;
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        AtomicReference<ItemStack> trunks = new AtomicReference<ItemStack>(ItemStack.EMPTY);

        AtomicBoolean inCurioSlot = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(entity).ifPresent(curios -> {
            if (curios.isEquipped(ObjectRegistry.TRUNKS.get())) inCurioSlot.set(true);
            if (inCurioSlot.get()) trunks.set(curios.findFirstCurio(stackX -> stackX.is(ObjectRegistry.TRUNKS.get())).get().stack());
        });

        boolean inLegsSlot = entity.getItemBySlot(EquipmentSlot.LEGS).is(ObjectRegistry.TRUNKS.get());
        if (inLegsSlot) trunks.set(entity.getItemBySlot(EquipmentSlot.LEGS));

        if (!inCurioSlot.get() && !inLegsSlot) return;

        DyeableBeachpartyArmorItem item = trunks.get().getItem() instanceof DyeableBeachpartyArmorItem ? (DyeableBeachpartyArmorItem) trunks.get().getItem() : null;
        if (item == null) return;

        DyeColor retrieved = DyeColor.getColor(trunks.get());
        DyeColor dyeColor = retrieved == null ? DyeColor.BLUE : retrieved;

        poseStack.pushPose();
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY, dyeColor.getTextureDiffuseColors()[0], dyeColor.getTextureDiffuseColors()[1], dyeColor.getTextureDiffuseColors()[2], 1.0f);
        poseStack.popPose();
    }
}
