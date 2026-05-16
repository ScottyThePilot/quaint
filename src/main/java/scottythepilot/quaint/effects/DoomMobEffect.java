package scottythepilot.quaint.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import scottythepilot.quaint.QuaintMod;

public class DoomMobEffect extends MobEffect {
  public DoomMobEffect(MobEffectCategory category, int color) {
    super(category, color);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.isAlive()) {
      livingEntity.hurt(QuaintMod.damageSourceDoom(livingEntity.registryAccess()), Float.MAX_VALUE);
      return false;
    }

    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return duration == 1;
  }
}
