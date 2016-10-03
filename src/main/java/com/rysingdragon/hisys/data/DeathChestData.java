package com.rysingdragon.hisys.data;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class DeathChestData extends AbstractData<DeathChestData, ImmutableDeathChestData> {

    private String owner;

    public DeathChestData() {
        registerGettersAndSetters();
    }

    public DeathChestData(String deathChestOwner) {
        this.owner = deathChestOwner;
        registerGettersAndSetters();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(HISYSKeys.DEATH_CHEST_OWNER, () -> this.owner);
        registerFieldSetter(HISYSKeys.DEATH_CHEST_OWNER, value -> this.owner = value);
        registerKeyValue(HISYSKeys.DEATH_CHEST_OWNER, this::owner);

    }

    public Value<String> owner() {
        return Sponge.getRegistry().getValueFactory().createValue(HISYSKeys.DEATH_CHEST_OWNER, this.owner);
    }

    @Override
    public Optional<DeathChestData> fill(DataHolder dataHolder, MergeFunction overlap) {
        return Optional.empty();
    }

    @Override
    public Optional<DeathChestData> from(DataContainer container) {
        if (!container.contains(HISYSKeys.DEATH_CHEST_OWNER.getQuery())) {
            return Optional.empty();
        }

        this.owner = container.getString(HISYSKeys.DEATH_CHEST_OWNER.getQuery()).get();
        return Optional.of(this);
    }

    @Override
    public DeathChestData copy() {
        return new DeathChestData(this.owner);
    }

    @Override
    public ImmutableDeathChestData asImmutable() {
        return new ImmutableDeathChestData(this.owner);
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
