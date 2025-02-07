package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public interface ExtendsCannonMount {

	@Nullable CannonMountBlockEntity getCannonMount();

	MutableComponent noCannonPresent = Component.translatable(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.no_cannon_present");
	MutableComponent cannonYawComponent = Component.translatable(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.yaw");
	MutableComponent cannonPitchComponent = Component.translatable(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.pitch");
	MutableComponent bigCannonStrengthComponent = Component.translatable(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.cannon_strength");
	String bigCannonStrengthValueKey = CreateBigCannons.MOD_ID + ".goggles.cannon_mount.cannon_strength.value";
	MutableComponent autocannonRPMComponent = Component.translatable(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.autocannon_rate_of_fire");
	String autocannonRPMValueKey = CreateBigCannons.MOD_ID + ".goggles.cannon_mount.autocannon_rate_of_fire.value";

	static void addCannonInfoToTooltip(List<Component> tooltip, @Nullable PitchOrientedContraptionEntity mountedContraption) {
		if (mountedContraption != null && mountedContraption.getContraption() instanceof AbstractMountedCannonContraption cannon) {
			Direction dir = mountedContraption.getInitialOrientation();
			boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
			float pitch = flag ? mountedContraption.pitch : -mountedContraption.pitch;
			if (Math.abs(pitch) < 1e-1f) pitch = 0;

			String precision = CBCConfigs.CLIENT.cannonMountAngleGoggleTooltipPrecision.get().toString();
			float yaw;
			if (CBCConfigs.CLIENT.use180180RangeForYaw.get()) {
				yaw = Mth.wrapDegrees(mountedContraption.yaw);
			} else {
				yaw = Mth.positiveModulo(mountedContraption.yaw, 360);
			}
			String format = "%." + precision + "f\u00ba";
			Lang.builder().add(cannonYawComponent.copy().withStyle(ChatFormatting.GRAY)
					.append(Components.literal(String.format(format, yaw)).withStyle(ChatFormatting.WHITE)))
				.forGoggles(tooltip);
			Lang.builder().add(cannonPitchComponent.copy().withStyle(ChatFormatting.GRAY)
					.append(Components.literal(String.format(format, Mth.wrapDegrees(pitch))).withStyle(ChatFormatting.WHITE)))
				.forGoggles(tooltip);
			if (cannon instanceof MountedBigCannonContraption bigCannon) {
				Lang.builder().add(bigCannonStrengthComponent.copy().withStyle(ChatFormatting.GRAY)
					.append(Component.translatable(bigCannonStrengthValueKey, bigCannon.getMaxSafeCharges()).withStyle(ChatFormatting.WHITE)))
				.forGoggles(tooltip);
			} else if (cannon instanceof MountedAutocannonContraption autocannon) {
				Lang.builder().add(autocannonRPMComponent.copy().withStyle(ChatFormatting.GRAY)
					.append(Component.translatable(autocannonRPMValueKey, autocannon.getReferencedFireRate()).withStyle(ChatFormatting.WHITE)))
				.forGoggles(tooltip);
			}
		} else {
			Lang.builder().add(noCannonPresent.copy()).forGoggles(tooltip);
		}
	}

}
