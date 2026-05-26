package scottythepilot.quaint.items.alchemy;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import scottythepilot.quaint.effects.QuaintEffects;
import scottythepilot.quaint.items.QuaintItems;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public final class QuaintPotions {
  private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, MOD_ID);

  public static final DeferredHolder<Potion, Potion> CLARITY =
    POTIONS.register("clarity", () -> new Potion("clarity", new MobEffectInstance(QuaintEffects.CLARITY)));
  public static final DeferredHolder<Potion, Potion> DOOM =
    POTIONS.register("doom", () -> new Potion("doom", new MobEffectInstance(QuaintEffects.DOOM, 48000)));

  public static void register(IEventBus modEventBus) {
    POTIONS.register(modEventBus);
  }

  public static void registerBrewingRecipesEvent(RegisterBrewingRecipesEvent event) {
    event.getBuilder().addStartMix(QuaintItems.DUBIOUS_DECOCTION.get(), QuaintPotions.DOOM);
    event.getBuilder().addStartMix(Items.MILK_BUCKET, QuaintPotions.CLARITY);
  }
}
