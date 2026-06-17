package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.sign;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignExecutor implements CommandExecutor {
  @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(MessageManager.get("general-invalid-executor"));
            return true;
        }
        Player player = (Player) commandSender;
        if (args.length < 1) {
            return false;
        }
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage(MessageManager.get("sign-invalid-item"));
        }
        String text = String.join(" ",args);
        Pattern pattern = Pattern.compile("&([0-9a-frlomnk])");
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll("§$1");


        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();


        Date rawDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String date = sdf.format(rawDate);


        String playerName = player.getPlayerListName();


        List<Component> itemLore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            itemLore = itemMeta.lore();
        }
        itemLore.add(Component.text(""));
        itemLore.add(LegacyComponentSerializer.legacySection().deserialize(text));
        itemLore.add(LegacyComponentSerializer.legacySection().deserialize("§7Signiert von§a " + playerName + "§7 am §a" + date + "§7. "));
        itemMeta.lore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return true;
    }
}
