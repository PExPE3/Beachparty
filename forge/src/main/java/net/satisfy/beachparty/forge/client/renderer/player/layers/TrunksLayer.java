package net.satisfy.beachparty.forge.client.renderer.player.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.satisfy.beachparty.client.model.TrunksModel;
import net.satisfy.beachparty.core.item.DyeableBeachpartyArmorItem;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.util.BeachpartyIdentifier;
import org.antlr.v4.runtime.misc.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TrunksLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final TrunksModel<T> model;

    public TrunksLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
        this.model = new TrunksModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(TrunksModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        AtomicReference<ItemStack> trunks = new AtomicReference<ItemStack>(ItemStack.EMPTY);

        AtomicBoolean inCurioSlot = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(entity).ifPresent(curios -> {
            if (curios.isEquipped(ObjectRegistry.TRUNKS.get())) inCurioSlot.set(true);
            if (inCurioSlot.get()) trunks.set(curios.findFirstCurio(stack -> stack.is(ObjectRegistry.TRUNKS.get())).get().stack());
        });

        boolean inLegsSlot = entity.getItemBySlot(EquipmentSlot.LEGS).is(ObjectRegistry.TRUNKS.get());
        if (inLegsSlot) trunks.set(entity.getItemBySlot(EquipmentSlot.LEGS));

        if (!inCurioSlot.get() && !inLegsSlot) return;

        DyeableBeachpartyArmorItem item = trunks.get().getItem() instanceof DyeableBeachpartyArmorItem ? (DyeableBeachpartyArmorItem) trunks.get().getItem() : null;
        if (item == null) return;

        DyeColor retrieved = DyeColor.getColor(trunks.get());
        DyeColor dyeColor = retrieved == null ? DyeColor.BLUE : retrieved;

        poseStack.pushPose();
        renderColoredCutoutModel(this.model, getTextureLocation(entity), poseStack, multiBufferSource, i, entity, dyeColor.getTextureDiffuseColors()[0], dyeColor.getTextureDiffuseColors()[1], dyeColor.getTextureDiffuseColors()[2]);
        poseStack.popPose();
    }

    @Override
    protected ResourceLocation getTextureLocation(@NotNull T entity) {
        return new BeachpartyIdentifier("textures/models/armor/trunks.png");
    }
}
