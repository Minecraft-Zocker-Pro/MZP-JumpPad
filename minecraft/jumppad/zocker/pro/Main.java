package minecraft.jumppad.zocker.pro;

import minecraft.core.zocker.pro.CorePlugin;
import minecraft.core.zocker.pro.compatibility.CompatibleMaterial;
import minecraft.core.zocker.pro.config.Config;
import minecraft.core.zocker.pro.hook.HookManager;
import minecraft.core.zocker.pro.storage.StorageManager;
import minecraft.jumppad.zocker.pro.command.JumpPadCommand;
import minecraft.jumppad.zocker.pro.listener.*;
import minecraft.statistic.zocker.pro.StatisticManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends CorePlugin {

	private static CorePlugin PLUGIN;
	public static Config JUMPPAD_CONFIG, JUMPPAD_MESSAGE;
	public static String JUMPPAD_DATABASE_TABLE;

	@Override
	public void onEnable() {
		if (!Bukkit.getPluginManager().isPluginEnabled("MZP-Core")) {
			System.out.println("Cant load MZP-JumpPad. Please install MZP-Core.");
			return;
		}

		super.onEnable();
		super.setDisplayItem(CompatibleMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.getMaterial());
		super.setPluginName("MZP-JumpPad");
		this.setHelpCommand("jumppad");

		PLUGIN = this;

		this.buildConfig();
		this.verifyDatabase();
		this.registerCommand();
		this.registerListener();

		HookManager hookManager = new HookManager();
		hookManager.load("MZP-Statistic");

		if (hookManager.isLoaded("MZP-Statistic")) {
			StatisticManager.register("JUMPPAD_USED");
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				JumpPadManager.loadAll();
			}
		}.runTaskLater(this, 20L);
	}

	@Override
	public void registerCommand() {
		getCommand("jumppad").setExecutor(new JumpPadCommand());
	}

	@Override
	public void registerListener() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new PlayerInteractListener(), this);
		pluginManager.registerEvents(new PlayerJumpPadListener(), this);
		pluginManager.registerEvents(new PlayerQuitListener(), this);
		pluginManager.registerEvents(new PlayerJumpPadBreakListener(), this);
		pluginManager.registerEvents(new PlayerTeleportListener(), this);

		if (!JUMPPAD_CONFIG.getBool("jumppad.land.damage")) {
			pluginManager.registerEvents(new PlayerFallDamageListener(), this);
		}

		if (Main.JUMPPAD_CONFIG.getBool("jumppad.land.sound")) {
			pluginManager.registerEvents(new PlayerJumpPadLandListener(), this);
		}

		pluginManager.registerEvents(new PlayerJumpPadPlaceListener(), this);
	}

	@Override
	public void buildConfig() {
		// Config
		JUMPPAD_CONFIG = new Config("jumppad.yml", this.getPluginName());

		// Global or Per Server
		JUMPPAD_CONFIG.set("jumppad.global", true, "0.0.1");
		JUMPPAD_CONFIG.set("jumppad.land.damage", false, "0.0.1");
		JUMPPAD_CONFIG.set("jumppad.land.sound", true, "0.0.1");

		JUMPPAD_CONFIG.setVersion("0.0.1", true);

		// Message
		JUMPPAD_MESSAGE = new Config("message.yml", this.getPluginName());
		JUMPPAD_MESSAGE.set("jumppad.prefix", "&6&l[JumpPad]&3 ", "0.0.1");
		JUMPPAD_MESSAGE.set("jumppad.player.offline", "&6%player%&3 is not online.", "0.0.3");
		JUMPPAD_MESSAGE.set("jumppad.item.display", "&6&lJumpPad", "0.0.3");

		JUMPPAD_MESSAGE.setVersion("0.0.3", true);
	}

	@Override
	public void reload() {
		JUMPPAD_CONFIG.reload();
		JUMPPAD_MESSAGE.reload();
	}

	private void verifyDatabase() {
		if (JUMPPAD_CONFIG.getBool("jumppad.global")) {
			JUMPPAD_DATABASE_TABLE = "server_jumppad";
		} else {
			JUMPPAD_DATABASE_TABLE = "server_jumppad_" + StorageManager.getServerName();
		}

		String createUpgradeTable = "CREATE TABLE IF NOT EXISTS `" + JUMPPAD_DATABASE_TABLE + "` (`jumppad_uuid` varchar(36) NOT NULL, `location_world` varchar(36) NOT NULL, `location_x` double NOT NULL, `location_y` double NOT NULL, `location_z` double NOT NULL, `power` double DEFAULT 2, `height` " +
			"double DEFAULT 2, `permission` varchar(64) DEFAULT NULL, `particle` varchar(64) DEFAULT 'FLAME', `sound` varchar(64) DEFAULT 'ENTITY_GENERIC_EXPLODE', `effect` varchar(32) DEFAULT 'NONE')";

		if (StorageManager.isMySQL()) {
			assert StorageManager.getMySQLDatabase() != null : "Create table failed.";
			StorageManager.getMySQLDatabase().createTable(createUpgradeTable);
			return;
		}

		assert StorageManager.getSQLiteDatabase() != null : "Create table failed.";
		StorageManager.getSQLiteDatabase().createTable(createUpgradeTable);
	}

	public static CorePlugin getPlugin() {
		return PLUGIN;
	}
}
