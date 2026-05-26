package scottythepilot.quaint.effects;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.EffectCure;

public class ClarityMobEffect extends InstantenousMobEffect {
  protected final EffectCure cure;

  public ClarityMobEffect(MobEffectCategory category, int color, EffectCure cure) {
    super(category, color);
    this.cure = cure;
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (!livingEntity.level().isClientSide()) {
      livingEntity.removeEffectsCuredBy(this.cure);
    }

    return true;
  }
}
