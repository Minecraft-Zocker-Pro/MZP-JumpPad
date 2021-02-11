package minecraft.jumppad.zocker.pro.listener;

import minecraft.core.zocker.pro.compatibility.CompatibleParticleHandler;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.core.zocker.pro.hook.HookManager;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadEffectType;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import minecraft.jumppad.zocker.pro.Main;
import minecraft.jumppad.zocker.pro.event.PlayerJumpPadEvent;
import minecraft.statistic.zocker.pro.StatisticZocker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJumpPadListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJumpPad(PlayerJumpPadEvent e) {
		if (e.isCancelled()) return;

		JumpPadManager jumpPadManager = new JumpPadManager();
		JumpPad jumpPad = e.getJumpPad();
		Player player = e.getPlayer();

		jumpPadManager.removeJumping(player);

		if (jumpPad.getPermission() != null && jumpPad.getPermission().length() > 0) {
			if (player.hasPermission(jumpPad.getPermission())) {
				CompatibleSound.playErrorSound(player);
				return;
			}
		}

		player.setVelocity(player.getLocation().getDirection().multiply(jumpPad.getPower()).setY(jumpPad.getHeight()));

		// Sound
		jumpPad.getSound().play(player.getWorld(), jumpPad.getLocation(), 2F, 2F);

		if (jumpPad.getEffectType() == JumpPadEffectType.EXPLODE) {
			CompatibleParticleHandler.spawnParticles(
				CompatibleParticleHandler.ParticleType.EXPLOSION_LARGE,
				jumpPad.getLocation(), 2);
		}

		if (!Main.JUMPPAD_CONFIG.getBool("jumppad.land.damage")) {
			jumpPadManager.addJumping(player);
			if (e.isAsynchronous()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						player.setAllowFlight(true);
					}
				}.runTask(Main.getPlugin());
			} else {
				player.setAllowFlight(true);
			}
		}

		if (new HookManager().isLoaded("MZP-Statistic")) {
			new StatisticZocker(player.getUniqueId()).add("JUMPPAD_USED");
		}
	}
}
