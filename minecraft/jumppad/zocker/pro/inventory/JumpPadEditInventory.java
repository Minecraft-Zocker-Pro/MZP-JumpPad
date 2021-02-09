package minecraft.jumppad.zocker.pro.inventory;

import minecraft.core.zocker.pro.Zocker;
import minecraft.core.zocker.pro.compatibility.CompatibleMaterial;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.core.zocker.pro.inventory.InventoryUpdateZocker;
import minecraft.core.zocker.pro.inventory.InventoryZocker;
import minecraft.core.zocker.pro.inventory.builder.InventoryEntryBuilder;
import minecraft.core.zocker.pro.inventory.util.ItemBuilder;
import minecraft.jumppad.zocker.pro.JumpPad;
import minecraft.jumppad.zocker.pro.JumpPadEditType;
import minecraft.jumppad.zocker.pro.JumpPadEffectType;
import minecraft.jumppad.zocker.pro.JumpPadManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class JumpPadEditInventory extends InventoryUpdateZocker {

	private final Zocker zocker;
	private final JumpPad jumpPad;

	private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
	private int selectedEffectType;

	public JumpPadEditInventory(Zocker zocker, JumpPad jumpPad) {
		this.zocker = zocker;
		this.jumpPad = jumpPad;
		this.decimalFormat.setRoundingMode(RoundingMode.CEILING);

		int count = 0;
		for (JumpPadEffectType value : JumpPadEffectType.values()) {
			if (value == jumpPad.getEffectType()) {
				this.selectedEffectType = count;
				return;
			}

			count++;
		}
	}

	@Override
	public String getTitle() {
		return "JumpPad Edit";
	}

	@Override
	public InventoryType getInventoryType() {
		return InventoryType.CHEST;
	}

	@Override
	public Integer getSize() {
		return 54;
	}

	@Override
	public void onClose(InventoryZocker inventoryZocker, Inventory inventory, Player player) {
		new JumpPadManager().update(jumpPad);
	}

	@Override
	public void setupInventory() {
		this.fillBorders();
		this.setUpdate(true);
		this.setUpdateOffset(1);
		this.setUpdateTimeUnit(TimeUnit.SECONDS);

		// region Delete
		this.setItem(new InventoryEntryBuilder()
			.setItem(new ItemBuilder(CompatibleMaterial.REDSTONE_BLOCK.getMaterial())
				.setName("§4§lDelete")
			).onAllClicks(inventoryClickEvent -> {
				Player player = zocker.getPlayer();

				new JumpPadManager().delete(this.jumpPad.getUuid().toString());
				player.closeInventory();

				CompatibleSound.playSuccessSound(player);
			})
			.setAsync(false)
			.setSlot(4)
			.build());
		// endregion

	}

	@Override
	public void onUpdate() {
		this.setItem(new InventoryEntryBuilder()
			.setItem(new ItemBuilder(CompatibleMaterial.BLAZE_POWDER.getMaterial()).setDisplayName("§6§lPower").setLore("§3" + decimalFormat.format(jumpPad.getPower())))
			.onLeftClick(inventoryClickEvent -> {
				if (inventoryClickEvent.isShiftClick()) {
					jumpPad.setPower(jumpPad.getPower() + 1);
				} else {
					jumpPad.setPower(jumpPad.getPower() + 0.1);
				}

				CompatibleSound.playChangedSound(zocker.getPlayer());
			})
			.onRightClick(inventoryClickEvent -> {
				if (jumpPad.getPower() <= 0) {
					jumpPad.setPower(0);
					CompatibleSound.playErrorSound(zocker.getPlayer());
					return;
				}

				if (inventoryClickEvent.isShiftClick()) {
					jumpPad.setPower(jumpPad.getPower() - 1);
				} else {
					jumpPad.setPower(jumpPad.getPower() - 0.1);
				}

				CompatibleSound.playChangedSound(zocker.getPlayer());
			})
			.setSlot(20)
			.build());

		// region Height
		this.setItem(new InventoryEntryBuilder()
			.setItem(new ItemBuilder(CompatibleMaterial.ENDER_EYE.getMaterial()).setDisplayName("§6§lHeight").setLore("§3" + decimalFormat.format(jumpPad.getHeight())))
			.onLeftClick(inventoryClickEvent -> {
				if (inventoryClickEvent.isShiftClick()) {
					jumpPad.setHeight(jumpPad.getHeight() + 1);
				} else {
					jumpPad.setHeight(jumpPad.getHeight() + 0.1);
				}

				CompatibleSound.playChangedSound(zocker.getPlayer());

			})
			.onRightClick(inventoryClickEvent -> {
				if (jumpPad.getHeight() <= 0) {
					jumpPad.setHeight(0);
					CompatibleSound.playErrorSound(zocker.getPlayer());
					return;
				}

				if (inventoryClickEvent.isShiftClick()) {
					jumpPad.setHeight(jumpPad.getHeight() - 1);
				} else {
					jumpPad.setHeight(jumpPad.getHeight() - 0.1);
				}

				CompatibleSound.playChangedSound(zocker.getPlayer());
			})
			.setSlot(21)
			.build());
		// endregion

		// region Effect
		this.setItem(new InventoryEntryBuilder()
			.setItem(new ItemBuilder(CompatibleMaterial.FIREWORK_STAR.getMaterial())
				.addItemFlag(ItemFlag.HIDE_DESTROYS)
				.setDisplayName("§6§lEffect")
				.setLore("§3" + jumpPad.getEffectType().name()))
			.onLeftClick(inventoryClickEvent -> {
				if (selectedEffectType + 1 >= JumpPadEffectType.values().length) {
					selectedEffectType = 0;
					jumpPad.setEffectType(JumpPadEffectType.values()[0]);
				} else {
					selectedEffectType++;
					jumpPad.setEffectType(JumpPadEffectType.values()[selectedEffectType]);
				}

				CompatibleSound.playChangedSound(zocker.getPlayer());
			})
			.setAsync(false)
			.setSlot(22)
			.build());
		// endregion

		// region Permission
		this.setItem(new InventoryEntryBuilder()
			.setItem(new ItemBuilder(CompatibleMaterial.REDSTONE.getMaterial())
				.setDisplayName("§6§lPermission")
				.setLore("§3" + jumpPad.getPermission()))
			.onLeftClick(inventoryClickEvent -> {
				new JumpPadAnvilEditInventory(zocker, jumpPad, JumpPadEditType.PERMISSION).open(zocker);
				CompatibleSound.playChangedSound(zocker.getPlayer());
			})
			.setAsync(false)
			.setSlot(23)
			.build());
		// endregion

		// region Sound
		this.setItem(new InventoryEntryBuilder()
			.setItem(new ItemBuilder(CompatibleMaterial.JUKEBOX.getMaterial())
				.setDisplayName("§6§lSound")
				.setLore("§3" + jumpPad.getSound().name()))
			.onLeftClick(inventoryClickEvent -> {
				new JumpPadAnvilEditInventory(zocker, jumpPad, JumpPadEditType.SOUND).open(zocker);
				CompatibleSound.playChangedSound(zocker.getPlayer());
			})
			.setAsync(false)
			.setSlot(24)
			.build());
		// endregion


		// region Particle
		this.setItem(new InventoryEntryBuilder()
			.setItem(new ItemBuilder(CompatibleMaterial.ORANGE_DYE.getMaterial())
				.setDisplayName("§6§lParticle")
				.setLore("§3" + jumpPad.getParticle().name()))
			.onLeftClick(inventoryClickEvent -> {
				new JumpPadAnvilEditInventory(zocker, jumpPad, JumpPadEditType.PARTICLE).open(zocker);
				CompatibleSound.playChangedSound(zocker.getPlayer());
			})
			.setAsync(false)
			.setSlot(31)
			.build());
		// endregion
	}
}
