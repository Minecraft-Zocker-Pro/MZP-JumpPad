package minecraft.jumppad.zocker.pro.inventory;

import minecraft.core.zocker.pro.Main;
import minecraft.core.zocker.pro.Zocker;
import minecraft.core.zocker.pro.compatibility.CompatibleMaterial;
import minecraft.core.zocker.pro.compatibility.CompatibleParticleHandler;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.core.zocker.pro.inventory.InventoryAnvilZocker;
import minecraft.core.zocker.pro.inventory.InventoryEntry;
import minecraft.core.zocker.pro.inventory.InventoryZocker;
import minecraft.core.zocker.pro.inventory.builder.InventoryEntryBuilder;
import minecraft.core.zocker.pro.inventory.util.ItemBuilder;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadEditType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class JumpPadAnvilEditInventory extends InventoryAnvilZocker {

	private final Zocker zocker;
	private final JumpPad jumpPad;
	private final JumpPadEditType type;

	public JumpPadAnvilEditInventory(Zocker zocker, JumpPad jumpPad, JumpPadEditType type) {
		this.zocker = zocker;
		this.jumpPad = jumpPad;
		this.type = type;
	}

	@Override
	public InventoryEntry getLeftInventoryEntry() {
		return new InventoryEntryBuilder()
			.setItem(CompatibleMaterial.PAPER.getItem())
			.setSlot(1)
			.build();
	}

	@Override
	public InventoryEntry getRightInventoryEntry() {
		return new InventoryEntryBuilder()
			.setItem(CompatibleMaterial.PAPER.getItem())
			.setSlot(2)
			.build();
	}

	@Override
	public InventoryEntry getResultInventoryEntry() {
		String displayName;
		switch (type) {
			case PERMISSION: {
				displayName = jumpPad.getPermission();
				break;
			}

			case PARTICLE: {
				displayName = jumpPad.getParticle().name();
				break;
			}

			case SOUND: {
				displayName = jumpPad.getSound().name();
				break;
			}

			default: {
				displayName = "";
			}
		}

		return new InventoryEntryBuilder()
			.setItem(new ItemBuilder(CompatibleMaterial.PAPER.getMaterial())
				.setDisplayName(displayName))
			.setSlot(0)
			.build();
	}

	@Override
	public void onResult(String output) {
		switch (type) {
			case PERMISSION: {
				jumpPad.setPermission(output);
				break;
			}

			case PARTICLE: {
				for (CompatibleParticleHandler.ParticleType value : CompatibleParticleHandler.ParticleType.values()) {
					if (value.name().equalsIgnoreCase(output)) {
						jumpPad.setParticle(value);

						break;
					}
				}

				break;
			}

			case SOUND: {
				for (CompatibleSound value : CompatibleSound.values()) {
					if (value.getSound().name().equalsIgnoreCase(output)) {
						jumpPad.setSound(value);
						break;
					}
				}

				break;
			}
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				zocker.getPlayer().closeInventory();
				new JumpPadEditInventory(zocker, jumpPad).open(zocker);
			}
		}.runTask(Main.getPlugin());
	}

	@Override
	public void onClose(InventoryZocker inventoryZocker, Inventory inventory, Player player) {
		new JumpPadEditInventory(zocker, jumpPad).open(zocker);
	}

	@Override
	public int getLevelCost() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "JumpPad Edit";
	}

	@Override
	public void setupInventory() {
	}
}
