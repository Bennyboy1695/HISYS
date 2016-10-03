package com.rysingdragon.hisys;

import com.rysingdragon.hisys.data.DeathChestData;
import com.rysingdragon.hisys.data.DeathChestDataBuilder;
import com.rysingdragon.hisys.data.ImmutableDeathChestData;
import com.rysingdragon.hisys.listeners.BlockListener;
import com.rysingdragon.hisys.listeners.PlayerDeathListener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;

@Plugin(id = HISYS.PLUGIN_ID,
        name = HISYS.PLUGIN_NAME,
        version = HISYS.PLUGIN_VERSION,
        description = HISYS.PLUGIN_DESCRIPTION)
public class HISYS {

    public static final String PLUGIN_ID = "hisys";
    public static final String PLUGIN_NAME = "Hey I Saved Your Stuff";
    public static final String PLUGIN_VERSION = "1.1.0";
    public static final String PLUGIN_DESCRIPTION = PLUGIN_NAME + " is a plugin which allows players to much more easily recover their items on death" +
            " by spawning a chest containing their items when they die";

    public static HISYS INSTANCE;

    @Inject
    private PluginContainer PLUGIN_CONTAINER;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;
    private static Config generalConfig;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        INSTANCE = this;
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(configDirectory.resolve(PLUGIN_ID + ".conf")).build();
        generalConfig = new Config(configDirectory.resolve(PLUGIN_ID + ".conf"), loader);
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        Sponge.getDataManager().register(DeathChestData.class, ImmutableDeathChestData.class, new DeathChestDataBuilder());
        Sponge.getEventManager().registerListeners(this, new PlayerDeathListener());
        Sponge.getEventManager().registerListeners(this, new BlockListener());
    }

    @Listener
    public void onWorldLoad(LoadWorldEvent event) {
        CommentedConfigurationNode root = generalConfig.getNode();
        root.getNode("Worlds", event.getTargetWorld().getUniqueId().toString(), "Enabled").setValue(true).setComment("Whether or not deathchests are able to be created in this world");
        root.getNode("Worlds", event.getTargetWorld().getUniqueId().toString()).setComment("Name: " + event.getTargetWorld().getName());
        generalConfig.save();
    }

    public static Config getGeneralConfig() {
        return generalConfig;
    }

    public static Logger getLogger() {
        return INSTANCE.logger;
    }

    public static PluginContainer getPluginContainer() {
        return INSTANCE.PLUGIN_CONTAINER;
    }
}