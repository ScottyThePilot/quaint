package scottythepilot.quaint.item_predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;

public class TrueItemPredicate implements ItemPredicate {
  public static final TrueItemPredicate INSTANCE = new TrueItemPredicate();
  public static final MapCodec<TrueItemPredicate> CODEC = MapCodec.unit(() -> INSTANCE);

  @Override
  public boolean test(ItemStack itemStack) {
    return true;
  }

  @Override
  public ItemPredicateType<? extends ItemPredicate> getType() {
    return QuaintItemPredicates.TRUE.get();
  }
}
