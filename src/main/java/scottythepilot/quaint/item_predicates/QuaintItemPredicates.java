package scottythepilot.quaint.item_predicates;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import scottythepilot.quaint.QuaintMod;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public class QuaintItemPredicates {
  public static final ResourceKey<Registry<ItemPredicateType<?>>> ITEM_PREDICATES_REGISTRY_KEY = QuaintMod.keyRegistry("item_condition");
  public static final Registry<ItemPredicateType<?>> ITEM_PREDICATES_REGISTRY = new RegistryBuilder<>(ITEM_PREDICATES_REGISTRY_KEY)
    .sync(true).defaultKey(QuaintMod.at("false")).create();

  private static final DeferredRegister<ItemPredicateType<?>> ITEM_PREDICATE_TYPES = DeferredRegister.create(ITEM_PREDICATES_REGISTRY, MOD_ID);

  public static final DeferredHolder<ItemPredicateType<?>, ItemPredicateType<CompositeItemPredicate.AllOf>> ALL_OF =
    ITEM_PREDICATE_TYPES.register("all_of", () -> new ItemPredicateType<>(CompositeItemPredicate.AllOf.CODEC));
  public static final DeferredHolder<ItemPredicateType<?>, ItemPredicateType<CompositeItemPredicate.AnyOf>> ANY_OF =
    ITEM_PREDICATE_TYPES.register("any_of", () -> new ItemPredicateType<>(CompositeItemPredicate.AnyOf.CODEC));
  public static final DeferredHolder<ItemPredicateType<?>, ItemPredicateType<TrueItemPredicate>> TRUE =
    ITEM_PREDICATE_TYPES.register("true", () -> new ItemPredicateType<>(TrueItemPredicate.CODEC));
  public static final DeferredHolder<ItemPredicateType<?>, ItemPredicateType<FalseItemPredicate>> FALSE =
    ITEM_PREDICATE_TYPES.register("false", () -> new ItemPredicateType<>(FalseItemPredicate.CODEC));
  public static final DeferredHolder<ItemPredicateType<?>, ItemPredicateType<NotItemPredicate>> NOT =
    ITEM_PREDICATE_TYPES.register("not", () -> new ItemPredicateType<>(NotItemPredicate.CODEC));

  public static void register(IEventBus modEventBus) {
    ITEM_PREDICATE_TYPES.register(modEventBus);
  }

  public static void newRegistryEvent(NewRegistryEvent event) {
    event.register(ITEM_PREDICATES_REGISTRY);
  }
}
