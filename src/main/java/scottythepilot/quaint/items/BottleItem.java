package scottythepilot.quaint.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BottleItem extends Item {
  private final int drinkDuration;

  public BottleItem(final int drinkDuration, Item.Properties properties) {
    super(properties.craftRemainder(Items.GLASS_BOTTLE));
    this.drinkDuration = drinkDuration;
  }

  public @NotNull ItemStack finishUsingItem(
    @NotNull ItemStack stack,
    @NotNull Level level,
    @NotNull LivingEntity livingEntity
  ) {
    super.finishUsingItem(stack, level, livingEntity);

    if (livingEntity instanceof ServerPlayer serverplayer) {
      CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
      serverplayer.awardStat(Stats.ITEM_USED.get(this));
    }

    if (stack.isEmpty()) {
      return new ItemStack(Items.GLASS_BOTTLE);
    } else {
      if (livingEntity instanceof Player player) {
        if (!player.hasInfiniteMaterials()) {
          ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
          if (!player.getInventory().add(itemstack)) {
            player.drop(itemstack, false);
          }
        }
      }

      return stack;
    }
  }

  public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
    return this.drinkDuration;
  }

  public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
    return UseAnim.DRINK;
  }

  public @NotNull SoundEvent getDrinkingSound() {
    return SoundEvents.GENERIC_DRINK;
  }

  public @NotNull SoundEvent getEatingSound() {
    return SoundEvents.GENERIC_DRINK;
  }

  public @NotNull InteractionResultHolder<ItemStack> use(
    @NotNull Level level,
    @NotNull Player player,
    @NotNull InteractionHand hand
  ) {
    return ItemUtils.startUsingInstantly(level, player, hand);
  }
}
