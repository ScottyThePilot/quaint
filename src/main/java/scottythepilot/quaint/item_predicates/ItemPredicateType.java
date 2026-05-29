package scottythepilot.quaint.item_predicates;

import com.mojang.serialization.MapCodec;

public record ItemPredicateType<T extends ItemPredicate>(MapCodec<T> codec) {}
