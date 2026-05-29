package scottythepilot.quaint.item_predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record NotItemPredicate(ItemPredicate predicate) implements ItemPredicate {
  public static final MapCodec<NotItemPredicate> CODEC = RecordCodecBuilder.mapCodec((inst) -> {
    return inst.group(ItemPredicate.CODEC.fieldOf("predicate").forGetter(NotItemPredicate::predicate)).apply(inst, NotItemPredicate::new);
  });

  @Override
  public boolean test(ItemStack itemStack) {
    return !this.predicate.test(itemStack);
  }

  @Override
  public ItemPredicateType<? extends ItemPredicate> getType() {
    return QuaintItemPredicates.NOT.get();
  }
}
