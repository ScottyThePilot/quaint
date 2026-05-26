package scottythepilot.quaint.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public final class QuaintLoot {
  private static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MOD_ID);
  private static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, MOD_ID);

  public static final DeferredHolder<LootItemConditionType, LootItemConditionType> CONDITION_ENSURE_UNIQUE =
    LOOT_CONDITION_TYPES.register("ensure_unique", () -> new LootItemConditionType(LootItemEnsureUniqueCondition.CODEC));
  public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<LootItemRegisterUniqueFunction>> FUNCTION_REGISTER_UNIQUE =
    LOOT_FUNCTION_TYPES.register("register_unique", () -> new LootItemFunctionType<>(LootItemRegisterUniqueFunction.CODEC));

  public static void register(IEventBus modEventBus) {
    LOOT_CONDITION_TYPES.register(modEventBus);
    LOOT_FUNCTION_TYPES.register(modEventBus);
  }
}
