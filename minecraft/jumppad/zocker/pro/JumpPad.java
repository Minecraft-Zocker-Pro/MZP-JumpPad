package minecraft.jumppad.zocker.pro;

import minecraft.core.zocker.pro.compatibility.CompatibleParticleHandler;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import org.bukkit.Location;

import java.util.UUID;

public class JumpPad {

	private final UUID uuid;
	private final Location location;
	private double power;
	private double height;
	private String permission;
	private CompatibleParticleHandler.ParticleType particle;
	private CompatibleSound sound;
	private JumpPadEffectType effectType;

	public JumpPad(UUID uuid, Location location, double power, double height, String permission, CompatibleParticleHandler.ParticleType particle, CompatibleSound sound) {
		this.uuid = uuid;
		this.location = location;
		this.power = power;
		this.height = height;
		this.permission = permission;
		this.particle = particle;
		this.sound = sound;
		this.effectType = JumpPadEffectType.NONE;
	}
	
	public JumpPad(UUID uuid, Location location, double power, double height, String permission, CompatibleParticleHandler.ParticleType particle, CompatibleSound sound, JumpPadEffectType effectType) {
		this.uuid = uuid;
		this.location = location;
		this.power = power;
		this.height = height;
		this.permission = permission;
		this.particle = particle;
		this.sound = sound;
		this.effectType = effectType;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Location getLocation() {
		return location;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public JumpPadEffectType getEffectType() {
		return effectType;
	}

	public void setEffectType(JumpPadEffectType effectType) {
		this.effectType = effectType;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

	public CompatibleParticleHandler.ParticleType getParticle() {
		return particle;
	}

	public void setParticle(CompatibleParticleHandler.ParticleType particle) {
		this.particle = particle;
	}

	public CompatibleSound getSound() {
		return sound;
	}

	public void setSound(CompatibleSound sound) {
		this.sound = sound;
	}
}
