package minecraft.jumppad.zocker.pro.command;

import minecraft.core.zocker.pro.command.SubCommand;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import minecraft.jumppad.zocker.pro.Main;
import org.bukkit.command.CommandSender;

import java.util.List;

public class JumpPadListCommand extends SubCommand {

	public JumpPadListCommand() {
		super("list");
	}

	@Override
	public String getUsage() {
		return Main.JUMPPAD_MESSAGE.getString("jumppad.prefix") + "§3Type §6/jumppad list";
	}

	@Override
	public String getPermission() {
		return "mzp.jumppad.command.list";
	}

	@Override
	public void onExecute(CommandSender commandSender, String[] strings) {
		int count = 1;
		for (JumpPad jumpPad : JumpPadManager.getJumpPadList()) {
			CompatibleMessage.sendMessage(commandSender, "§6" + count + ".§3 " + jumpPad.getUuid());
			count++;
		}
	}

	@Override
	public List<String> getCompletions(CommandSender commandSender, String[] strings) {
		return null;
	}
}
