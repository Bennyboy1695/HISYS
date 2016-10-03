package com.rysingdragon.hisys.listeners;

import com.rysingdragon.hisys.HISYS;
import com.rysingdragon.hisys.data.DeathChestData;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;

public class PlayerDeathListener {

    @Listener
    public void onDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity") Player player) {
        if (!HISYS.getGeneralConfig().getNode().getNode("Worlds", player.getWorld().getUniqueId().toString(), "Enabled").getBoolean(true))
            return;

        if (player.getInventory().size() == 0)
            return;

        BlockState state = BlockState.builder().blockType(BlockTypes.CHEST).build();
        Location<World> location = player.getLocation();
        Location<World> newLocation = location.add(Direction.SOUTH.asBlockOffset());

        //Send dummy place event to test for since most protection plugins aren't listening to ChangeBlockEvent.Modify
        ChangeBlockEvent.Place blockPlaceEvent = SpongeEventFactory.createChangeBlockEventPlace(
                Cause.of(NamedCause.owner(HISYS.getPluginContainer()), NamedCause.source(player)), location.getExtent(), Arrays.asList(
                        new Transaction<>(location.createSnapshot(), state.snapshotFor(location)), new Transaction<>(newLocation.createSnapshot(), state.snapshotFor(newLocation))
                )
        );
        if (Sponge.getEventManager().post(blockPlaceEvent)) {
            return;
        }
        //place deathchests
        location.setBlock(state, Cause.of(NamedCause.owner(HISYS.getPluginContainer()), NamedCause.source(player)));
        newLocation.setBlock(state, Cause.of(NamedCause.owner(HISYS.getPluginContainer()), NamedCause.source(player)));

        if (!location.hasTileEntity() || !newLocation.hasTileEntity())
            return;

        TileEntity chest = location.getTileEntity().get();
        TileEntity otherChest = newLocation.getTileEntity().get();
        //Add custom data to deathchests
        chest.offer(new DeathChestData(player.getUniqueId().toString()));
        otherChest.offer(new DeathChestData(player.getUniqueId().toString()));

        //Store player's inventory in deathchests and clear the player's inventory
        player.getInventory().slots().forEach(slot -> {
            if (slot.peek().isPresent()) {
                ItemStack stack = slot.poll().get();
                TileEntityInventory inventory = (TileEntityInventory) chest;

                if (inventory.offer(stack).getType() != InventoryTransactionResult.Type.SUCCESS) {
                    inventory = (TileEntityInventory) newLocation.getTileEntity().get();
                    inventory.offer(stack);
                }
            }
        });
    }
}
