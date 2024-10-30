package net.satisfy.beachparty.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.satisfy.beachparty.registry.EntityTypeRegistry;
import net.satisfy.beachparty.registry.ObjectRegistry;

public class BeachpartyChestBoatEntity extends ChestBoat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    public BeachpartyChestBoatEntity(EntityType<BeachpartyChestBoatEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public BeachpartyChestBoatEntity(Level pLevel, double pX, double pY, double pZ) {
        this(EntityTypeRegistry.FLOATY_CHEST_BOAT.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    @Override
    public Item getDropItem() {
        switch (getModVariant()) {
            case KELP -> {
                return ObjectRegistry.FLOATY_CHEST_BOAT.get();
            }
        }
        return super.getDropItem();
    }

    public void setVariant(BeachpartyBoatEntity.Type pVariant) {
        this.entityData.set(DATA_ID_TYPE, pVariant.ordinal());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, BeachpartyBoatEntity.Type.KELP.ordinal());
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putString("Type", this.getModVariant().getSerializedName());
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Type", 8)) {
            this.setVariant(BeachpartyBoatEntity.Type.byName(pCompound.getString("Type")));
        }
    }

    public BeachpartyBoatEntity.Type getModVariant() {
        return BeachpartyBoatEntity.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }
}