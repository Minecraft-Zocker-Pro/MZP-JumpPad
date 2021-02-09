package minecraft.jumppad.zocker.pro.command;

import minecraft.core.zocker.pro.command.SubCommand;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import minecraft.jumppad.zocker.pro.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class JumpPadDeleteCommand extends SubCommand {

	public JumpPadDeleteCommand() {
		super("delete", 1, 1);
	}

	@Override
	public String getUsage() {
		return Main.JUMPPAD_MESSAGE.getString("jumppad.prefix") + "§3Type §6/jumppad delete <id>";
	}

	@Override
	public String getPermission() {
		return "mzp.jumppad.command.delete";
	}

	@Override
	public void onExecute(CommandSender commandSender, String[] args) {
		if (args.length == 0) {
			CompatibleMessage.sendMessage(commandSender, getUsage());
			return;
		}
		
		JumpPadManager jumpPadManager = new JumpPadManager();

		try {
			if (jumpPadManager.delete(args[0]).get()) {

				CompatibleMessage.sendMessage(commandSender, Main.JUMPPAD_MESSAGE.getString("jumppad.prefix") + "You deleted the JumpPad (" + args[0] + ")");

				if (commandSender instanceof Player) {
					CompatibleSound.playSuccessSound((Player) commandSender);
				}
			} else {
				if (commandSender instanceof Player) {
					CompatibleSound.playErrorSound((Player) commandSender);
				}

				CompatibleMessage.sendMessage(commandSender, Main.JUMPPAD_MESSAGE.getString("jumppad.prefix") + "Failed to delete the JumpPad (" + args[0] + ")");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getCompletions(CommandSender commandSender, String[] strings) {
		return null;
	}
}
