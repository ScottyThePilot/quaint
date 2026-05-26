package scottythepilot.quaint.damage_sources;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import scottythepilot.quaint.QuaintMod;

public final class QuaintDamageSources {
  public static final ResourceKey<DamageType> DAMAGE_TYPE_DOOM = QuaintMod.key(Registries.DAMAGE_TYPE, "doom");

  public static DamageSource damageSource(RegistryAccess registryAccess, ResourceKey<DamageType> damageType) {
    return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType));
  }

  public static DamageSource damageSourceDoom(RegistryAccess registryAccess) {
    return QuaintDamageSources.damageSource(registryAccess, DAMAGE_TYPE_DOOM);
  }
}
