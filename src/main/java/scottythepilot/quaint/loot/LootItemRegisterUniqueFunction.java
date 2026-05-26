package scottythepilot.quaint.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Set;

public class LootItemRegisterUniqueFunction extends LootItemConditionalFunction {
  public static final MapCodec<LootItemRegisterUniqueFunction> CODEC =
    RecordCodecBuilder.mapCodec((inst) -> {
      return commonFields(inst).and(inst.group(
        Codec.STRING.fieldOf("identifier").forGetter(o -> o.identifier),
        UniqueLootContext.CODEC.optionalFieldOf("context", UniqueLootContext.LEVEL).forGetter(o -> o.context)
      )).apply(inst, LootItemRegisterUniqueFunction::new);
    });

  public final String identifier;
  public final UniqueLootContext context;

  protected LootItemRegisterUniqueFunction(
    final List<LootItemCondition> predicates,
    final String identifier,
    final UniqueLootContext context
  ) {
    super(predicates);
    this.context = context;
    this.identifier = identifier;
  }

  @Override
  public @NotNull LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
    return QuaintLoot.FUNCTION_REGISTER_UNIQUE.get();
  }

  @Override
  protected @NotNull ItemStack run(@NotNull ItemStack itemStack, @NotNull LootContext lootContext) {
    this.context.incrementQuantityFor(lootContext, this.identifier);
    return itemStack;
  }

  @Override
  public @NotNull Set<LootContextParam<?>> getReferencedContextParams() {
    return ImmutableSet.of(LootContextParams.THIS_ENTITY);
  }
}
