package scottythepilot.quaint.item_predicates;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;

public interface ItemPredicate extends Predicate<ItemStack> {
  Codec<ItemPredicate> CODEC = QuaintItemPredicates.ITEM_PREDICATES_REGISTRY.byNameCodec()
    .dispatch(ItemPredicate::getType, ItemPredicateType::codec);

  ItemPredicateType<? extends ItemPredicate> getType();
}
