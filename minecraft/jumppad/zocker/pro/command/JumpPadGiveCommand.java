package minecraft.jumppad.zocker.pro.command;

import minecraft.core.zocker.pro.command.SubCommand;
import minecraft.core.zocker.pro.compatibility.CompatibleMaterial;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.core.zocker.pro.nms.NmsManager;
import minecraft.core.zocker.pro.nms.api.nbt.NBTItem;
import minecraft.jumppad.zocker.pro.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class JumpPadGiveCommand extends SubCommand {

	public JumpPadGiveCommand() {
		super("give", 1, 3);
	}

	@Override
	public String getUsage() {
		return Main.JUMPPAD_MESSAGE.getString("jumppad.prefix") + "§3Type §6/jumppad give <player> <type> <amount>";
	}

	@Override
	public String getPermission() {
		return "mzp.jumppad.command.give";
	}

	@Override
	public void onExecute(CommandSender commandSender, String[] args) {
		if (args.length == 0) {
			CompatibleMessage.sendMessage(commandSender, getUsage());
			return;
		}

		String playerName = args[0];

		Player player = Bukkit.getPlayer(playerName);
		if (player == null || !player.isOnline()) {
			CompatibleMessage.sendMessage(
				commandSender,
				Main.JUMPPAD_MESSAGE.getString("jumppad.prefix") + Main.JUMPPAD_MESSAGE.getString("jumppad.player.offline")
					.replace("%player%", playerName));
			return;
		}

		if (args.length == 1) {
			giveJumpPad(player, CompatibleMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.getMaterial(), 1);
			return;
		}

		String materialName = args[1];

		if (!materialName.contains("PRESSURE_PLATE") || !materialName.endsWith("_PLATE")) {
			materialName = CompatibleMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.getMaterial().name();
		}

		CompatibleMaterial compatibleMaterial = CompatibleMaterial.getMaterial(materialName);
		if (compatibleMaterial == null) {
			materialName = CompatibleMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.getMaterial().name();
		}

		if (args.length == 2) {
			giveJumpPad(player, CompatibleMaterial.getMaterial(materialName).getMaterial(), 1);
			return;
		}

		if (args.length == 3) {
			giveJumpPad(player, CompatibleMaterial.getMaterial(materialName).getMaterial(), Integer.parseInt(args[2]));
		}
	}

	private void giveJumpPad(Player player, Material material, int amount) {
		ItemStack itemStack = CompatibleMaterial.getMaterial(material).getItem();
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(Main.JUMPPAD_MESSAGE.getString("jumppad.item.display"));
		itemStack.setItemMeta(itemMeta);

		NBTItem nbtItem = NmsManager.getNbt().of(itemStack);
		nbtItem.set("jumppad_player", player.getName());
		ItemStack jumpPadItemStack = nbtItem.finish();

		for (int i = 0; i < amount; i++) {
			player.getInventory().addItem(jumpPadItemStack);
		}

		CompatibleSound.ENTITY_ITEM_PICKUP.play(player);
	}

	@Override
	public List<String> getCompletions(CommandSender commandSender, String[] strings) {
		return null;
	}
}
