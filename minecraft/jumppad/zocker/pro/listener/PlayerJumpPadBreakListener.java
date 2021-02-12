package minecraft.jumppad.zocker.pro.listener;

import minecraft.core.zocker.pro.compatibility.CompatibleMaterial;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.core.zocker.pro.compatibility.ServerVersion;
import minecraft.core.zocker.pro.nms.NmsManager;
import minecraft.core.zocker.pro.nms.api.nbt.NBTItem;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import minecraft.jumppad.zocker.pro.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJumpPadBreakListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerJumpPadBreak(BlockBreakEvent e) {
		if (e.getPlayer().hasPermission("mzp.jumppad.break")) {
			JumpPadManager jumpPadManager = new JumpPadManager();
			JumpPad jumpPad = jumpPadManager.get(e.getBlock().getLocation());
			if (jumpPad == null) return;

			Player player = e.getPlayer();

			jumpPadManager.delete(jumpPad.getUuid().toString());
			CompatibleSound.playSuccessSound(player);

			if (player.getGameMode() == GameMode.CREATIVE) return;

			ItemStack itemStack = CompatibleMaterial.getMaterial(e.getBlock().getType()).getItem();
			ItemMeta itemMeta = itemStack.getItemMeta();

			itemMeta.setDisplayName(Main.JUMPPAD_MESSAGE.getString("jumppad.item.display"));
			itemStack.setItemMeta(itemMeta);

			NBTItem nbtItem = NmsManager.getNbt().of(itemStack);
			nbtItem.set("jumppad_player", player.getName());
			ItemStack jumpPadItemStack = nbtItem.finish();

			player.getInventory().addItem(jumpPadItemStack);

			if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_12)) {
				e.setDropItems(false);
			} else {
				e.getBlock().getDrops().clear();
				e.getBlock().setType(CompatibleMaterial.AIR.getMaterial());
			}
		}
	}
}
