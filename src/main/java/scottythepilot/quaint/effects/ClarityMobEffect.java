package scottythepilot.quaint.effects;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.EffectCures;

public class ClarityMobEffect extends InstantenousMobEffect {
  public ClarityMobEffect(MobEffectCategory category, int color) {
    super(category, color);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (!livingEntity.level().isClientSide()) {
      livingEntity.removeEffectsCuredBy(EffectCures.MILK);
    }

    return true;
  }
}
