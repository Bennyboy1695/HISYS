package com.rysingdragon.hisys.listeners;

import com.rysingdragon.hisys.data.HISYSKeys;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class BlockListener {

    @Listener
    public void onInteract(InteractBlockEvent.Secondary event, @First Player player) {
        if (!event.getTargetBlock().getLocation().isPresent())
            return;
        Location<World> location = event.getTargetBlock().getLocation().get();
        if (!location.hasTileEntity())
            return;

        TileEntity tileEntity = location.getTileEntity().get();

        if (tileEntity.get(HISYSKeys.DEATH_CHEST_OWNER).isPresent()) {
            UUID owner = UUID.fromString(tileEntity.get(HISYSKeys.DEATH_CHEST_OWNER).get());
            if (player.getUniqueId().equals(owner)) {
                player.sendMessage(Text.of(TextColors.DARK_GREEN, "This is your DeathChest, break it to retrieve your items."));
            } else {
                if (!Sponge.getServiceManager().provide(UserStorageService.class).isPresent())
                    return;

                UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
                if (!service.get(owner).isPresent())
                    return;
                User user = service.get(owner).get();
                player.sendMessage(Text.of(
                        TextColors.RED, "This DeathChest is owned by ", TextColors.DARK_AQUA, user.getName(), TextColors.RED, " you may not retrieve the contents of another player's DeathChest!"
                ));
            }
            event.setCancelled(true);
        }
    }

    @Listener
    public void onBreak(ChangeBlockEvent.Break event, @First Player player) {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            if (transaction.getOriginal().getState().getType() == BlockTypes.CHEST) {
                BlockSnapshot snapshot = transaction.getOriginal();
                if (snapshot.get(HISYSKeys.DEATH_CHEST_OWNER).isPresent() && !snapshot.get(HISYSKeys.DEATH_CHEST_OWNER).get().equals(player.getUniqueId().toString())) {

                    UUID owner = UUID.fromString(snapshot.get(HISYSKeys.DEATH_CHEST_OWNER).get());
                    UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
                    if (!service.get(owner).isPresent())
                        return;
                    User user = service.get(owner).get();
                    player.sendMessage(Text.of(
                            TextColors.RED, "This DeathChest is owned by ", TextColors.DARK_AQUA, user.getName(), TextColors.RED, " you may not retrieve the contents of another player's DeathChest!"
                    ));
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
