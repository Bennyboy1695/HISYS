package com.rysingdragon.hisys;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.inject.Inject;

@Plugin(id = HISYS.pluginID,
        name = HISYS.pluginName,
        version = HISYS.pluginVersion,
        description = HISYS.pluginDescription)
public class HISYS {

    public static final String pluginID = "hisys";
    public static final String pluginName = "Hey I Saved Your Stuff";
    public static final String pluginVersion = "1.0.0";
    public static final String pluginDescription = pluginName + " is a plugin which allows players to much more easily recover their items on death" +
            " by spawning a chest containing their items when they die";

    @Inject
    private PluginContainer container;

    @Listener
    public void onDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity") Player player) {
        if (player.getInventory().isEmpty())
            return;

        Location<World> location = player.getLocation();
        BlockState state = BlockState.builder().blockType(BlockTypes.CHEST).build();
        location.setBlock(state, Cause.of(NamedCause.owner(container)));

        if (!location.getTileEntity().isPresent())
            return;
        TileEntity chest = location.getTileEntity().get();
        chest.offer(Keys.DISPLAY_NAME, Text.of(player.getName() + "'s Stuff"));

        Location<World> newLocation = location.add(Direction.SOUTH.asBlockOffset());
        newLocation.setBlock(state, Cause.of(NamedCause.owner(container)));

        player.getInventory().slots().forEach(slot -> {
            if (slot.peek().isPresent()) {
                TileEntityInventory inventory = (TileEntityInventory) chest;

                if (inventory.offer(slot.peek().get()).getType() != InventoryTransactionResult.Type.SUCCESS) {
                    inventory = (TileEntityInventory) newLocation.getTileEntity().get();
                    inventory.offer(slot.peek().get());
                }
            }
        });
    }
}