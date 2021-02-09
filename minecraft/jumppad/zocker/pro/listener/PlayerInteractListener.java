package minecraft.jumppad.zocker.pro.listener;

import minecraft.core.zocker.pro.Zocker;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import minecraft.jumppad.zocker.pro.Main;
import minecraft.jumppad.zocker.pro.event.PlayerJumpPadEvent;
import minecraft.jumppad.zocker.pro.inventory.JumpPadEditInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerInteractListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) return;

		if (e.getAction() == Action.PHYSICAL) {
			JumpPad jumpPad = new JumpPadManager().get(e.getClickedBlock().getLocation());
			if (jumpPad == null) return;

			// we need to call the event later, because bukkit is modifying the velocity 
			new BukkitRunnable() {
				@Override
				public void run() {
					PlayerJumpPadEvent jumpPadTriggerEvent = new PlayerJumpPadEvent(e.getPlayer(), jumpPad);
					Bukkit.getPluginManager().callEvent(jumpPadTriggerEvent);
				}
			}.runTaskLaterAsynchronously(Main.getPlugin(), 5L); // 1L = 50ms

			return;
		}

		// Edit menu
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getPlayer().isSneaking()) {
				Player player = e.getPlayer();
				if (!player.hasPermission("mzp.jumppad.edit")) return;

				JumpPad jumpPad = new JumpPadManager().get(e.getClickedBlock().getLocation());
				if (jumpPad == null) return;

				Zocker zocker = Zocker.getZocker(player.getUniqueId());
				if (zocker != null) {
					new JumpPadEditInventory(zocker, jumpPad).open(zocker);
				}
			}
		}
	}
}
