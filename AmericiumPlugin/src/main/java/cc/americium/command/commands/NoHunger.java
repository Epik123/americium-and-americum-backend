package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.ArrayList;
import java.util.UUID;

public class NoHunger implements Command, Listener {
    private final ArrayList<UUID> toggled = new ArrayList<>();

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"nohunger"};
    }

    @Override
    public String getDescription() {
        return "Prevents you from losing any hunger.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        UUID uuid = executor.getUniqueId();

        if (toggled.contains(uuid)) {
            toggled.remove(uuid);
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You will now lose hunger!");
            return;
        }

        toggled.add(uuid);
        sender.sendMessage(Colors.Prefix + Colors.Success + "You will now not lose hunger!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler
    private void hungerLoseEvent(FoodLevelChangeEvent e) {
        HumanEntity thing = e.getEntity();

        if (thing.getUniqueId() == null) {
            return;
        }

        if (this.toggled.contains(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
