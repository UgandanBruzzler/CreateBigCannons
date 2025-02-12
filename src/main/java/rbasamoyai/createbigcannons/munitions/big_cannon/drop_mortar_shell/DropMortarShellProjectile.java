package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import javax.annotation.Nonnull;

import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.DropMortarProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.DropMortarProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class DropMortarShellProjectile extends FuzedBigCannonProjectile implements DropMortarProjectile {

	public DropMortarShellProjectile(EntityType<? extends DropMortarShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override public BlockState getRenderedBlockState() { return Blocks.AIR.defaultBlockState(); }

	@Override
	protected void detonate(Position position) {
		DropMortarShellProperties properties = this.getAllProperties();
		ShellExplosion entityDamage = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), properties.entityDamagingExplosivePower(), false, Level.ExplosionInteraction.NONE, true);
		CreateBigCannons.handleCustomExplosion(this.level(), entityDamage);
		ShellExplosion shellExplosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), properties.blockDamagingExplosivePower(), false,
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction(), false);
		CreateBigCannons.handleCustomExplosion(this.level(), shellExplosion);
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Nonnull
	@Override
	protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().bigCannonProperties();
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	@Nonnull
	@Override
	public DropMortarProjectileProperties getDropMortarProperties() {
		return this.getAllProperties();
	}

	protected DropMortarShellProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.DROP_MORTAR_SHELL.getPropertiesOf(this);
	}

}
