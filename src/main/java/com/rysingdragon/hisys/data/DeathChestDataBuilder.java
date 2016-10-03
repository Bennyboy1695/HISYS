package com.rysingdragon.hisys.data;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class DeathChestDataBuilder extends AbstractDataBuilder<DeathChestData> implements DataManipulatorBuilder<DeathChestData, ImmutableDeathChestData> {

    public DeathChestDataBuilder() {
        super(DeathChestData.class, 0);
    }

    @Override
    protected Optional<DeathChestData> buildContent(DataView container) throws InvalidDataException {
        if (container.contains(HISYSKeys.DEATH_CHEST_OWNER.getQuery())) {
            DeathChestData data = create();
            String owner = container.getString(HISYSKeys.DEATH_CHEST_OWNER.getQuery()).get();
            data.set(HISYSKeys.DEATH_CHEST_OWNER, owner);
            return Optional.of(data);
        }
        return Optional.empty();
    }

    @Override
    public DeathChestData create() {
        return new DeathChestData();
    }

    @Override
    public Optional<DeathChestData> createFrom(DataHolder dataHolder) {
        if (dataHolder.get(DeathChestData.class).isPresent()) {
            return Optional.of(dataHolder.get(DeathChestData.class).get());
        }
        return Optional.empty();
    }
}
