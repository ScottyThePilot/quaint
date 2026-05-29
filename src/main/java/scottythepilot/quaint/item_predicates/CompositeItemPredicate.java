package scottythepilot.quaint.item_predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class CompositeItemPredicate implements ItemPredicate {
  protected final List<ItemPredicate> predicates;
  private final Predicate<ItemStack> composedPredicate;

  protected CompositeItemPredicate(List<ItemPredicate> predicates, Predicate<ItemStack> composedPredicate) {
    this.predicates = predicates;
    this.composedPredicate = composedPredicate;
  }

  public final boolean test(ItemStack itemStack) {
    return this.composedPredicate.test(itemStack);
  }

  public static <T extends CompositeItemPredicate> MapCodec<T> codec(Function<List<ItemPredicate>, T> factory) {
    return RecordCodecBuilder.mapCodec((inst) -> inst.group(ItemPredicate.CODEC.listOf().fieldOf("predicates").forGetter((t) -> t.predicates)).apply(inst, factory));
  }

  public static class AllOf extends CompositeItemPredicate {
    public static final MapCodec<AllOf> CODEC = CompositeItemPredicate.codec(AllOf::new);

    public AllOf(List<ItemPredicate> conditions) {
      super(conditions, Util.allOf(conditions));
    }

    @Override
    public ItemPredicateType<? extends ItemPredicate> getType() {
      return QuaintItemPredicates.ALL_OF.get();
    }
  }

  public static class AnyOf extends CompositeItemPredicate {
    public static final MapCodec<AnyOf> CODEC = CompositeItemPredicate.codec(AnyOf::new);

    public AnyOf(List<ItemPredicate> conditions) {
      super(conditions, Util.anyOf(conditions));
    }

    @Override
    public ItemPredicateType<? extends ItemPredicate> getType() {
      return QuaintItemPredicates.ANY_OF.get();
    }
  }
}
