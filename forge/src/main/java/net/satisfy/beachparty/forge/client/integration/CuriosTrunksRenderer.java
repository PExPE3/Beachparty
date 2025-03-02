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
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.model.TrunksModel;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

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

        ItemStack[] trunksHolder = new ItemStack[]{ItemStack.EMPTY};
        boolean[] inCurioSlotHolder = new boolean[]{false};

        CuriosApi.getCuriosInventory(entity).ifPresent(curios -> curios.findFirstCurio(stackX -> stackX.is(ObjectRegistry.TRUNKS.get()))
                .ifPresent(curio -> {
                    inCurioSlotHolder[0] = true;
                    trunksHolder[0] = curio.stack();
                }));

        boolean inLegsSlot = entity.getItemBySlot(EquipmentSlot.LEGS).is(ObjectRegistry.TRUNKS.get());
        if (inLegsSlot) trunksHolder[0] = entity.getItemBySlot(EquipmentSlot.LEGS);
        if (!inCurioSlotHolder[0] && !inLegsSlot) return;

        if (trunksHolder[0].hasTag()) {
            assert trunksHolder[0].getTag() != null;
            if (trunksHolder[0].getTag().contains("Visible") && !trunksHolder[0].getTag().getBoolean("Visible")) return;
        }

        DyeableBeachpartyArmorItem item = trunksHolder[0].getItem() instanceof DyeableBeachpartyArmorItem ? (DyeableBeachpartyArmorItem) trunksHolder[0].getItem() : null;
        if (item == null) return;

        int colorInt = item.getColor(trunksHolder[0]);
        float red = ((colorInt >> 16) & 0xFF) / 255f;
        float green = ((colorInt >> 8) & 0xFF) / 255f;
        float blue = (colorInt & 0xFF) / 255f;

        poseStack.pushPose();
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
        poseStack.popPose();
    }
}
