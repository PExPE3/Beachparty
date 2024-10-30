package net.satisfy.beachparty.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.satisfy.beachparty.registry.EntityTypeRegistry;
import net.satisfy.beachparty.registry.ObjectRegistry;

import java.util.function.IntFunction;
public class BeachpartyBoatEntity extends Boat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(BeachpartyBoatEntity.class, EntityDataSerializers.INT);

    public BeachpartyBoatEntity(EntityType<BeachpartyBoatEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BeachpartyBoatEntity(Level pLevel, double pX, double pY, double pZ) {
        this(EntityTypeRegistry.FLOATY_BOAT.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }
    @Override
    public Item getDropItem() {
        switch (getModVariant()) {
            case KELP -> {
                return ObjectRegistry.FLOATY.get();
            }
        }
        return super.getDropItem();
    }
    public void setVariant(BeachpartyBoatEntity.Type pVariant) {
        this.entityData.set(DATA_ID_TYPE, pVariant.ordinal());
    }
    public BeachpartyBoatEntity.Type getModVariant() {
        return BeachpartyBoatEntity.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, Type.KELP.ordinal());
    }
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putString("Type", this.getModVariant().getSerializedName());
    }
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Type", 8)) {
            this.setVariant(BeachpartyBoatEntity.Type.byName(pCompound.getString("Type")));
        }
    }
    public static enum Type implements StringRepresentable {
        KELP(Blocks.KELP, "kelp");
        private final String name;
        private final Block planks;
        public static final StringRepresentable.EnumCodec<BeachpartyBoatEntity.Type> CODEC = StringRepresentable.fromEnum(BeachpartyBoatEntity.Type::values);
        private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        private Type(Block pPlanks, String pName) {
            this.name = pName;
            this.planks = pPlanks;
        }
        public String getSerializedName() {
            return this.name;
        }
        public String getName() {
            return this.name;
        }
        public Block getPlanks() {
            return this.planks;
        }
        public String toString() {
            return this.name;
        }

        public static BeachpartyBoatEntity.Type byId(int pId) {
            return BY_ID.apply(pId);
        }
        public static BeachpartyBoatEntity.Type byName(String pName) {
            return CODEC.byName(pName, KELP);
        }
    }
}