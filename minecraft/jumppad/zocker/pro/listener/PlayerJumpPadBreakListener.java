package minecraft.jumppad.zocker.pro.listener;

import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerJumpPadBreakListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerJumpPadBreak(BlockBreakEvent e) {
		if (e.getPlayer().hasPermission("mzp.jumppad.break")) {
			JumpPadManager jumpPadManager = new JumpPadManager();
			JumpPad jumpPad = jumpPadManager.get(e.getBlock().getLocation());
			if (jumpPad == null) return;

			jumpPadManager.delete(jumpPad.getUuid().toString());
			CompatibleSound.playSuccessSound(e.getPlayer());
		}
	}
}
