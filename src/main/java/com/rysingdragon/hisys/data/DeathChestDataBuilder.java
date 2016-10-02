package com.rysingdragon.hisys.data;

import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class DeathChestDataBuilder extends AbstractDataBuilder<DeathChestData> {

    protected DeathChestDataBuilder(int supportedVersion) {
        super(DeathChestData.class, supportedVersion);
    }

    @Override
    protected Optional<DeathChestData> buildContent(DataView container) throws InvalidDataException {
        return Optional.empty();
    }

}
