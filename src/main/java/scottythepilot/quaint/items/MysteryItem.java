package scottythepilot.quaint.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import scottythepilot.quaint.QuaintMod;

public abstract class MysteryItem extends Item {
  public MysteryItem(Item.Properties properties) {
    super(properties);
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
    ItemStack itemStack = player.getItemInHand(interactionHand);

    final ItemStack returnItemStack;
    try {
      returnItemStack = this.getResultItemStack(player, itemStack);
    } catch (Exception exception) {
      QuaintMod.LOGGER.error("Error getting return stack", exception);
      return InteractionResultHolder.fail(itemStack);
    }

    if (player instanceof ServerPlayer serverplayer) {
      CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, itemStack);
      serverplayer.awardStat(Stats.ITEM_USED.get(this));
    }

    if (!player.hasInfiniteMaterials()) {
      itemStack.shrink(1);
    }

    if (itemStack.isEmpty()) {
      return InteractionResultHolder.success(returnItemStack);
    } else {
      player.addItem(returnItemStack);
      return InteractionResultHolder.success(itemStack);
    }
  }

  /*
   * Produce a result item stack upon the item being used.
   * Provides the player who used the item and the original item stack.
   */
  public abstract @NotNull ItemStack getResultItemStack(Player player, ItemStack itemStack);
}
