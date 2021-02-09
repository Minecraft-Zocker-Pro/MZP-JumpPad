package minecraft.jumppad.zocker.pro.listener;

import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.jumppad.zocker.pro.event.PlayerJumpPadLandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJumpPadLandListener implements Listener {

	@EventHandler
	public void onPlayerJumpPadLand(PlayerJumpPadLandEvent e) {
		CompatibleSound.ITEM_SHIELD_BLOCK.play(e.getPlayer());
	}
}
