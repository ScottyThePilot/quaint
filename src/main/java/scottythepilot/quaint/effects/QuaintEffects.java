package scottythepilot.quaint.effects;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import scottythepilot.quaint.QuaintConfig;
import scottythepilot.quaint.QuaintMod;
import scottythepilot.quaint.tags.QuaintEntityTypeTags;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public final class QuaintEffects {
  private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MOD_ID);

  public static final DeferredHolder<MobEffect, MobEffect> CAMOUFLAGED =
    MOB_EFFECTS.register("camouflaged", () -> new CamouflagedMobEffect(MobEffectCategory.NEUTRAL, 0xCCCCCC));
  public static final DeferredHolder<MobEffect, MobEffect> CLARITY =
    MOB_EFFECTS.register("clarity", () -> new ClarityMobEffect(MobEffectCategory.NEUTRAL, 0xFFFFFF, EffectCures.MILK));
  public static final DeferredHolder<MobEffect, MobEffect> DOOM =
    MOB_EFFECTS.register("doom", () -> new DoomMobEffect(MobEffectCategory.HARMFUL, 0x202020));

  public static void register(IEventBus modEventBus) {
    MOB_EFFECTS.register(modEventBus);
  }

  @Deprecated
  public static boolean shouldGiveCamouflagedEffect(GameRules gameRules) {
    return gameRules.getBoolean(QuaintMod.RULE_DO_GIVE_CAMOUFLAGED_ON_RESPAWN);
  }

  @Deprecated
  public static void giveCamouflagedEffect(Player player) {
    QuaintMod.LOGGER.debug("Giving camouflaged effect to player {}", player.getGameProfile().getName());
    player.addEffect(new MobEffectInstance(QuaintEffects.CAMOUFLAGED, QuaintConfig.Server.getCamouflagedDuration(), 0, true, true, true));
  }

  @Deprecated
  public static void clearCamouflagedEffect(Player player) {
    player.removeEffect(QuaintEffects.CAMOUFLAGED);
  }

  public static boolean canEntitySeeThroughCamouflage(LivingEntity livingEntity) {
    return livingEntity.getType().is(BuiltInRegistries.ENTITY_TYPE.getTag(QuaintEntityTypeTags.SEES_THROUGH_CAMOUFLAGE).orElseThrow());
  }
}
