package com.rysingdragon.hisys.data;

import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;

public class ImmutableDeathChestData extends AbstractImmutableData<ImmutableDeathChestData, DeathChestData> {

    @Override
    protected void registerGetters() {

    }

    @Override
    public DeathChestData asMutable() {
        return new DeathChestData();
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
