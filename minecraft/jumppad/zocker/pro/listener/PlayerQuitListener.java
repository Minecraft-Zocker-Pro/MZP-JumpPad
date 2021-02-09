package minecraft.jumppad.zocker.pro.listener;

import minecraft.jumppad.zocker.pro.JumpPadManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent e) {
		new JumpPadManager().removeJumping(e.getPlayer());
	}
}
