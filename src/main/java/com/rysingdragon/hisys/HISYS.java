package com.rysingdragon.hisys;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.nio.file.Path;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

@Plugin(id = HISYS.pluginID,
        name = HISYS.pluginName,
        version = HISYS.pluginVersion,
        description = HISYS.pluginDescription)
public class HISYS {

    public static final String pluginID = "hisys";
    public static final String pluginName = "Hey I Saved Your Stuff";
    public static final String pluginVersion = "1.0.1";
    public static final String pluginDescription = pluginName + " is a plugin which allows players to much more easily recover their items on death" +
            " by spawning a chest containing their items when they die";

    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;
    private Config generalConfig;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(configDirectory.resolve(pluginID + ".conf")).build();
        this.generalConfig = new Config(configDirectory.resolve(pluginID + ".conf"), loader);
    }

    @Listener
    public void onDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity") Player player) {
        if (!this.generalConfig.getNode().getNode("Worlds", player.getWorld().getUniqueId().toString(), "Enabled").getBoolean(true))
            return;

        if (player.getInventory().size() == 0)
            return;

        BlockState state = BlockState.builder().blockType(BlockTypes.CHEST).build();
        Location<World> location = player.getLocation();
        Location<World> newLocation = location.add(Direction.SOUTH.asBlockOffset());

        location.setBlock(state, Cause.of(NamedCause.owner(container), NamedCause.source(player)));
        newLocation.setBlock(state, Cause.of(NamedCause.owner(container), NamedCause.source(player)));

        if (!location.getTileEntity().isPresent())
            return;
        TileEntity chest = location.getTileEntity().get();
        chest.offer(Keys.DISPLAY_NAME, Text.of(player.getName() + "'s Stuff"));

        player.getInventory().slots().forEach(slot -> {
            if (slot.peek().isPresent()) {
                ItemStack stack = slot.poll().orElse(null);
                TileEntityInventory inventory = (TileEntityInventory) chest;

                if (inventory.offer(stack).getType() != InventoryTransactionResult.Type.SUCCESS) {
                    inventory = (TileEntityInventory) newLocation.getTileEntity().get();
                    inventory.offer(stack);
                }
            }
        });
    }

    @Listener
    public void onWorldLoad(LoadWorldEvent event) {
        CommentedConfigurationNode root = generalConfig.getNode();
        root.getNode("Worlds", event.getTargetWorld().getUniqueId().toString(), "Enabled").setValue(true).setComment("Whether or not deathchests are able to be created in this world");
        root.getNode("Worlds", event.getTargetWorld().getUniqueId().toString()).setComment("Name: " + event.getTargetWorld().getName());
        generalConfig.save();
    }
}