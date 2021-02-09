package minecraft.jumppad.zocker.pro.command;

import minecraft.core.zocker.pro.command.SubCommand;
import minecraft.core.zocker.pro.compatibility.CompatibleParticleHandler;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadEffectType;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import minecraft.jumppad.zocker.pro.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class JumpPadCreateCommand extends SubCommand {

	public JumpPadCreateCommand() {
		super("create");
	}

	@Override
	public String getUsage() {
		return Main.JUMPPAD_MESSAGE.getString("jumppad.prefix") + "§3Type §6/jumppad create";
	}

	@Override
	public String getPermission() {
		return "mzp.jumppad.command.create";
	}

	@Override
	public void onExecute(CommandSender sender, String[] strings) {
		if (!(sender instanceof Player)) return;

		Player player = (Player) sender;
		List<Block> blocks = player.getLineOfSight(null, 0);

		Location jumpPadLocation = null;
		for (Block block : blocks) {
			if (block.getType().name().contains("PRESSURE_PLATE") || block.getType().name().endsWith("_PLATE")) {
				jumpPadLocation = block.getLocation();
				break;
			}
		}

		if (jumpPadLocation == null) {
//			CompatibleMessage.sendMessage(player, Main.SKYPVP_MESSAGE.getString("jumppad.prefix") + Main.SKYPVP_MESSAGE.getString("skypvp.sign.create.failed"));
			CompatibleSound.playErrorSound(player);
			return;
		}

		JumpPadManager jumpPadManager = new JumpPadManager();
		JumpPad jumpPad = jumpPadManager.get(jumpPadLocation);

		if (jumpPad != null) {
			CompatibleSound.playErrorSound(player);
			return;
		}

		jumpPadManager.create(new JumpPad(
			UUID.randomUUID(),
			jumpPadLocation,
			1,
			1,
			"",
			CompatibleParticleHandler.ParticleType.FLAME,
			CompatibleSound.ENTITY_GENERIC_EXPLODE,
			JumpPadEffectType.NONE
		));

		CompatibleSound.playSuccessSound(player);
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args) {
		return null;
	}
}
