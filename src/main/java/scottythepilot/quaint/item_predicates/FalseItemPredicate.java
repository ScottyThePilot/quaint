package scottythepilot.quaint.item_predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;

public class FalseItemPredicate implements ItemPredicate {
  public static final FalseItemPredicate INSTANCE = new FalseItemPredicate();
  public static final MapCodec<FalseItemPredicate> CODEC = MapCodec.unit(() -> INSTANCE);

  @Override
  public boolean test(ItemStack itemStack) {
    return false;
  }

  @Override
  public ItemPredicateType<? extends ItemPredicate> getType() {
    return QuaintItemPredicates.FALSE.get();
  }
}
