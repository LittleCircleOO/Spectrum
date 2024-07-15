package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = LightmapTextureManager.class, priority = 9999)

public class LightmapTextureManagerMixin {
	
	@ModifyReturnValue(method = "getDarkness", at = @At("RETURN"))
	private float spectrum$getDarkness(float original) {
		if (isInDim()) {
			if (SpectrumCommon.CONFIG.ExtraDarkDimension) {
				return Math.max(0.24F, original);
			}
			else {
				return Math.max(0.12F, original);
			}

		}
		return original;
	}
	
	@ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
	private float spectrum$decreaseGamma(float original) {
		if (isInDim()) {
			if (SpectrumCommon.CONFIG.ExtraDarkDimension) {
				return -1.5F;
			}
			else {
				return original - 0.5F;
			}
		}
		return original;
	}

	@ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z", ordinal = 0))
	private boolean spectrum$disableNightVision(boolean original) {
		if (isInDim() && SpectrumCommon.CONFIG.ExtraDarkDimension) {
			return false;
		}
		return original;
	}
	
	@Unique
	private static boolean isInDim() {
		MinecraftClient client = MinecraftClient.getInstance();
		return SpectrumDimensions.DIMENSION_KEY == client.world.getRegistryKey();
	}

}
