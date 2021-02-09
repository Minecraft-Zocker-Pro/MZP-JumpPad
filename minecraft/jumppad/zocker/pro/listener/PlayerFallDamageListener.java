package minecraft.jumppad.zocker.pro.listener;

import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import minecraft.jumppad.zocker.pro.event.PlayerJumpPadLandEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.CompletableFuture;

public class PlayerFallDamageListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerFallDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
				Player player = (Player) e.getEntity();
				JumpPadManager jumpPadManager = new JumpPadManager();
				if (jumpPadManager.isJumping(player)) {
					e.setCancelled(true);
					player.setAllowFlight(false);
					jumpPadManager.removeJumping(player);

					if (PlayerJumpPadLandEvent.getHandlerList().getRegisteredListeners().length > 0) {
						CompletableFuture.runAsync(() -> {
							PlayerJumpPadLandEvent playerJumpPadLandEvent = new PlayerJumpPadLandEvent(player);
							Bukkit.getPluginManager().callEvent(playerJumpPadLandEvent);
						});
					}
				}
			}
		}
	}
}
