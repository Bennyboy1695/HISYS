package com.rysingdragon.hisys.data;

import com.rysingdragon.hisys.HISYS;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;

import com.google.common.reflect.TypeToken;

public class HISYSKeys {

    private static final TypeToken<Value<String>> STRING_VALUE = new TypeToken<Value<String>>() {};
    public static final Key<Value<String>> DEATH_CHEST_OWNER = KeyFactory.makeSingleKey(
            TypeToken.of(String.class), STRING_VALUE, DataQuery.of("DeathChestOwner"), HISYS.PLUGIN_ID +":deathchestowner", "Death Chest Owner"
    );

}
