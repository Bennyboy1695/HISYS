package com.rysingdragon.hisys.data;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;

import java.util.Optional;

public class DeathChestData extends AbstractData<DeathChestData, ImmutableDeathChestData> {

    @Override
    protected void registerGettersAndSetters() {

    }

    @Override
    public Optional<DeathChestData> fill(DataHolder dataHolder, MergeFunction overlap) {
        return Optional.empty();
    }

    @Override
    public Optional<DeathChestData> from(DataContainer container) {
        return Optional.empty();
    }

    @Override
    public DeathChestData copy() {
        return new DeathChestData();
    }

    @Override
    public ImmutableDeathChestData asImmutable() {
        return new ImmutableDeathChestData();
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
