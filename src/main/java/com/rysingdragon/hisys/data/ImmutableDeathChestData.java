package com.rysingdragon.hisys.data;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableDeathChestData extends AbstractImmutableData<ImmutableDeathChestData, DeathChestData> {

    private String owner;

    public ImmutableDeathChestData(String deathChestOwner) {
        this.owner = deathChestOwner;
        registerGetters();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(HISYSKeys.DEATH_CHEST_OWNER, () -> this.owner);
        registerKeyValue(HISYSKeys.DEATH_CHEST_OWNER, this::owner);
    }

    public ImmutableValue<String> owner() {
        return Sponge.getRegistry().getValueFactory().createValue(HISYSKeys.DEATH_CHEST_OWNER, this.owner).asImmutable();
    }

    @Override
    public DeathChestData asMutable() {
        return new DeathChestData(this.owner);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(HISYSKeys.DEATH_CHEST_OWNER, this.owner);
    }
}
