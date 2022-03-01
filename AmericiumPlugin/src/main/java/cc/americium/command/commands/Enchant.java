package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Utilities;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Enchant implements Command {
    private final HashMap<String, Enchantment> enchantMap = new HashMap<>();

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"enchant"};
    }

    @Override
    public String getDescription() {
        return "Enchant held item with certain enchantment.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length < 2) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "enchant <enchant> <level>");
            return;
        }

        ItemStack item = Utilities.getHeldItem(executor);

        if (item.getType() == Material.AIR) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "You are not holding anything. fucktard.");
            return;
        }

        Enchantment enchant;

        if (Utilities.isNewApi()) {
            enchant = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(args[0].toLowerCase()));
        } else {
            try {
                enchant = enchantMap.get(args[0].toLowerCase());
            } catch (Exception e) {
                sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
                return;
            }
        }

        if (enchant == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That enchant was not found! Here are all valid enchants.");
            ArrayList<String> enchantsArray = new ArrayList<>();
            if (Utilities.isNewApi()) {
                for (Enchantment e : Enchantment.values()) {
                    enchantsArray.add(e.getKey().getKey());
                }
            } else {
                enchantsArray.addAll(enchantMap.keySet());
            }

            sender.sendMessage(ChatColor.RED + String.join(ChatColor.GRAY + ", " + ChatColor.RED, enchantsArray));
            return;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + e.getMessage());
            return;
        }

        item.addUnsafeEnchantment(enchant, level);
        sender.sendMessage(Colors.Prefix + Colors.Success + "Successfully applied enchant " + args[0] + " level " + level + " to your " + Utilities.prettyMaterial(item.getType()) + "!");
    }

    @Override
    public void onEnable(Z_ z) {
        enchantMap.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantMap.put("fire_protection", Enchantment.PROTECTION_FIRE);
        enchantMap.put("feather_falling", Enchantment.PROTECTION_FALL);
        enchantMap.put("blast_protection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantMap.put("projectile_protection", Enchantment.PROTECTION_PROJECTILE);
        enchantMap.put("respiration", Enchantment.OXYGEN);
        enchantMap.put("aqua_affinity", Enchantment.WATER_WORKER);
        enchantMap.put("thorns", Enchantment.THORNS);
        enchantMap.put("depth_strider", Enchantment.DEPTH_STRIDER);
        enchantMap.put("sharpness", Enchantment.DAMAGE_ALL);
        enchantMap.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchantMap.put("bane_of_arthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchantMap.put("knockback", Enchantment.KNOCKBACK);
        enchantMap.put("fire_aspect", Enchantment.FIRE_ASPECT);
        enchantMap.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchantMap.put("efficiency", Enchantment.DIG_SPEED);
        enchantMap.put("silk_touch", Enchantment.SILK_TOUCH);
        enchantMap.put("unbreaking", Enchantment.DURABILITY);
        enchantMap.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchantMap.put("power", Enchantment.ARROW_DAMAGE);
        enchantMap.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchantMap.put("flame", Enchantment.ARROW_FIRE);
        enchantMap.put("infinity", Enchantment.ARROW_INFINITE);
        enchantMap.put("luck_of_the_sea", Enchantment.LUCK);
        enchantMap.put("lure", Enchantment.LURE);
    }
}
