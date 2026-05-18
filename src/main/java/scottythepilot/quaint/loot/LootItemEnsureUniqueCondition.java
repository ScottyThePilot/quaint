package scottythepilot.quaint.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;
import scottythepilot.quaint.QuaintMod;
import java.util.Set;

public record LootItemEnsureUniqueCondition(String identifier, UniqueLootContext context, int limit) implements LootItemCondition {
  public static MapCodec<LootItemEnsureUniqueCondition> CODEC =
    RecordCodecBuilder.mapCodec((inst) -> {
      return inst.group(
        Codec.STRING.fieldOf("identifier").forGetter(LootItemEnsureUniqueCondition::identifier),
        UniqueLootContext.CODEC.optionalFieldOf("context", UniqueLootContext.LEVEL).forGetter(LootItemEnsureUniqueCondition::context),
        Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("limit", 1).forGetter(LootItemEnsureUniqueCondition::limit)
      ).apply(inst, LootItemEnsureUniqueCondition::new);
    });

  @Override
  public @NotNull LootItemConditionType getType() {
    return QuaintMod.LOOT_CONDITION_ENSURE_UNIQUE.get();
  }

  @Override
  public boolean test(@NotNull LootContext lootContext) {
    return this.context.getQuantityFor(lootContext, this.identifier) < this.limit;
  }

  @Override
  public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
    return ImmutableSet.of(LootContextParams.THIS_ENTITY);
  }
}
