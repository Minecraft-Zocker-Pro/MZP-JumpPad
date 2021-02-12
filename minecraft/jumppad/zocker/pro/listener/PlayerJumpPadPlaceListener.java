package minecraft.jumppad.zocker.pro.listener;

import minecraft.core.zocker.pro.compatibility.CompatibleParticleHandler;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.core.zocker.pro.nms.NmsManager;
import minecraft.core.zocker.pro.nms.api.nbt.NBTItem;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadEffectType;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class PlayerJumpPadPlaceListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJumpPadPlace(BlockPlaceEvent e) {
		if (e.isCancelled()) return;
		if (e.getPlayer().hasPermission("mzp.jumppad.place")) {
			NBTItem nbtItem = NmsManager.getNbt().of(e.getItemInHand());
			if (nbtItem.has("jumppad_player")) {
				JumpPadManager jumpPadManager = new JumpPadManager();

				jumpPadManager.create(new JumpPad(
					UUID.randomUUID(),
					e.getBlockPlaced().getLocation(),
					1,
					1,
					"",
					CompatibleParticleHandler.ParticleType.FLAME,
					CompatibleSound.ENTITY_GENERIC_EXPLODE,
					JumpPadEffectType.NONE
				));

				CompatibleSound.playSuccessSound(e.getPlayer());
			}
		}
	}
}
