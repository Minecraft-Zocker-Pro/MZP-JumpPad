package minecraft.jumppad.zocker.pro;

import minecraft.core.zocker.pro.Zocker;
import minecraft.core.zocker.pro.compatibility.CompatibleParticleHandler;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.core.zocker.pro.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class JumpPadManager {

	private static final HashMap<UUID, Long> JUMP_PAD_PLAYERS = new HashMap<>();
	private static final List<JumpPad> JUMP_PAD_LIST = new ArrayList<>();

	public static void loadAll() {
		ResultSet result;
		String[] columns = new String[]{"jumppad_uuid", "location_world", "location_x", "location_y", "location_z", "power", "height", "permission", "particle", "sound", "effect"};

		if (StorageManager.isMySQL()) {
			assert StorageManager.getMySQLDatabase() != null : "Select jumppads failed.";
			result = StorageManager.getMySQLDatabase().select(Main.JUMPPAD_DATABASE_TABLE, columns);
		} else {
			assert StorageManager.getSQLiteDatabase() != null : "Select jumppads failed.";
			result = StorageManager.getSQLiteDatabase().select(Main.JUMPPAD_DATABASE_TABLE, columns);
		}

		try {
			JumpPadManager jumpPadManager = new JumpPadManager();

			while (result.next()) {
				jumpPadManager.add(new JumpPad(
					UUID.fromString(result.getString("jumppad_uuid")),
					new Location(
						Bukkit.getWorld(result.getString("location_world")),
						result.getDouble("location_x"),
						result.getDouble("location_y"),
						result.getDouble("location_z")),
					result.getDouble("power"),
					result.getDouble("height"),
					result.getString("permission"),
					CompatibleParticleHandler.ParticleType.valueOf(result.getString("particle")),
					CompatibleSound.valueOf(result.getString("sound")),
					JumpPadEffectType.valueOf(result.getString("effect"))));
			}

			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public JumpPad get(Location location) {
		for (JumpPad jumpPad : JUMP_PAD_LIST) {

			if (jumpPad.getLocation().getWorld().getName().equalsIgnoreCase(location.getWorld().getName())) {
				if (jumpPad.getLocation().getX() == location.getX() && jumpPad.getLocation().getY() == location.getY() && jumpPad.getLocation().getZ() == location.getZ()) {
					return jumpPad;
				}
			}
		}

		return null;
	}

	public boolean add(JumpPad jumpPad) {
		if (jumpPad == null) return false;
		if (JUMP_PAD_LIST.contains(jumpPad)) return false;

		JUMP_PAD_LIST.add(jumpPad);
		return true;
	}

	public CompletableFuture<Boolean> update(JumpPad jumpPad) {
		if (jumpPad == null) return null;
		if (!JUMP_PAD_LIST.contains(jumpPad)) return null;

		return new Zocker("dummy").set(
			Main.JUMPPAD_DATABASE_TABLE,
			new String[]{"jumppad_uuid", "location_world", "location_x", "location_y", "location_z", "power", "height", "permission", "particle", "sound", "effect"},
			new Object[]{jumpPad.getUuid(), jumpPad.getLocation().getWorld().getName(), jumpPad.getLocation().getX(), jumpPad.getLocation().getY(), jumpPad.getLocation().getZ(), jumpPad.getPower(), jumpPad.getHeight(), jumpPad.getPermission(), jumpPad.getParticle(), jumpPad.getSound().name(),
				jumpPad.getEffectType().name()},
			new String[]{"jumppad_uuid"},
			new Object[]{jumpPad.getUuid()});
	}

	public void addJumping(Player player) {
		JUMP_PAD_PLAYERS.putIfAbsent(player.getUniqueId(), System.currentTimeMillis() / 1000);
	}

	public boolean isJumping(Player player) {
		return JUMP_PAD_PLAYERS.containsKey(player.getUniqueId());
	}

	public long isJumpingSince(Player player) {
		if (JUMP_PAD_PLAYERS.get(player.getUniqueId()) != null) {
			return (System.currentTimeMillis() / 1000) - JUMP_PAD_PLAYERS.get(player.getUniqueId());
		}

		addJumping(player);
		return 0;
	}

	public void removeJumping(Player player) {
		JUMP_PAD_PLAYERS.remove(player.getUniqueId());
	}

	public CompletableFuture<Boolean> create(JumpPad jumpPad) {
		if (jumpPad == null) return null;
		if (JUMP_PAD_LIST.contains(jumpPad)) return null;

		JUMP_PAD_LIST.add(jumpPad);

		return new Zocker("dummy").insert(
			Main.JUMPPAD_DATABASE_TABLE,
			new String[]{"jumppad_uuid", "location_world", "location_x", "location_y", "location_z", "power", "height", "permission", "particle", "sound", "effect"},
			new Object[]{jumpPad.getUuid(), jumpPad.getLocation().getWorld().getName(), jumpPad.getLocation().getX(), jumpPad.getLocation().getY(), jumpPad.getLocation().getZ(), jumpPad.getPower(), jumpPad.getHeight(), jumpPad.getPermission(), jumpPad.getParticle(), jumpPad.getSound().name(),
				jumpPad.getEffectType().name()},
			new String[]{"jumppad_uuid"},
			new Object[]{jumpPad.getUuid()});
	}

	public CompletableFuture<Boolean> delete(String jumpPadUUID) {
		if (jumpPadUUID == null) return null;

		for (JumpPad jumpPad : JUMP_PAD_LIST) {
			if (jumpPad.getUuid().toString().equalsIgnoreCase(jumpPadUUID)) {
				JUMP_PAD_LIST.remove(jumpPad);
				break;
			}
		}

		return new Zocker("dummy").delete(Main.JUMPPAD_DATABASE_TABLE,
			new String[]{"jumppad_uuid"},
			new Object[]{jumpPadUUID});
	}

	public static List<JumpPad> getJumpPadList() {
		return JUMP_PAD_LIST;
	}
}
