package minecraft.jumppad.zocker.pro.command;

import minecraft.core.zocker.pro.command.Command;
import minecraft.core.zocker.pro.command.SubCommand;
import minecraft.core.zocker.pro.compatibility.CompatibleMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JumpPadCommand extends Command {

	private static final List<SubCommand> SUB_COMMAND_LIST = new ArrayList<>();

	public JumpPadCommand() {
		super("jumppad", "mzp.jumppad.command", new String[]{"jumpp"});

		SUB_COMMAND_LIST.add(new JumpPadCreateCommand());
		SUB_COMMAND_LIST.add(new JumpPadListCommand());
		SUB_COMMAND_LIST.add(new JumpPadDeleteCommand());
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				CompatibleMessage.sendMessage(sender, "§3/jumppad create");
				CompatibleMessage.sendMessage(sender, "§3/jumppad delete <id>");
				CompatibleMessage.sendMessage(sender, "§3/jumppad list");
			}

			return;
		}

		for (SubCommand subCommand : SUB_COMMAND_LIST) {
			if (subCommand.getName().equalsIgnoreCase(args[0])) {
				subCommand.execute(sender, args);
			}
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		List<String> completions = new ArrayList<>();

		if (args.length == 0) {
			SUB_COMMAND_LIST.forEach(subCommand -> completions.add(subCommand.getName()));
		} else if (args.length == 1) {
			SUB_COMMAND_LIST.stream().filter(subCommand -> subCommand.getName().toLowerCase().startsWith(args[0].toLowerCase()))
				.forEach(subCommand -> completions.add(subCommand.getName()));
		} else {
			SubCommand command = findSubCommand(args[0]);

			if (command != null) {
				return command.getCompletions(sender, args);
			}
		}

		return completions;
	}

	private SubCommand findSubCommand(String name) {
		return SUB_COMMAND_LIST.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
}
