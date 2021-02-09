package minecraft.jumppad.zocker.pro.event;

import minecraft.jumppad.zocker.pro.JumpPad;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJumpPadEvent extends Event implements Cancellable {

	private static final HandlerList HANDLER_LIST = new HandlerList();

	private final Player player;
	private final JumpPad jumpPad;
	private boolean isCancelled;

	public PlayerJumpPadEvent(Player player, JumpPad jumpPad) {
		super(true);
		this.player = player;
		this.jumpPad = jumpPad;
	}

	public Player getPlayer() {
		return player;
	}

	public JumpPad getJumpPad() {
		return jumpPad;
	}

	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.isCancelled = b;
	}
}
