package minecraft.jumppad.zocker.pro.listener;

import minecraft.jumppad.zocker.pro.JumpPadManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if (e.isCancelled()) return;

		JumpPadManager jumpPadManager = new JumpPadManager();
		if (jumpPadManager.isJumping(e.getPlayer())) {
			jumpPadManager.removeJumping(e.getPlayer());
		}
	}
}
