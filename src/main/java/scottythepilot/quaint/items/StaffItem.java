package scottythepilot.quaint.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import scottythepilot.quaint.QuaintConfig;

public abstract class StaffItem extends Item {
  public StaffItem(Item.Properties properties) {
    super(properties);
  }

  @Override
  public boolean isEnchantable(@NotNull ItemStack itemStack) {
    return false;
  }

  @Override
  public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
    return 20;
  }

  @Override
  public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
    return UseAnim.BOW;
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
    ItemStack itemStack = player.getItemInHand(usedHand);
    player.startUsingItem(usedHand);
    return InteractionResultHolder.success(itemStack);
  }

  @Override
  public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
    if (livingEntity instanceof Player player) {
      if (this.onStaffUse(player)) {
        if (player.getAbilities().instabuild) {
          player.getCooldowns().addCooldown(this, 20);
        } else {
          player.getCooldowns().addCooldown(this, QuaintConfig.Server.getStaffCooldown());
          stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
        }
      } else {
        player.getCooldowns().addCooldown(this, 20);
      }
    }

    return stack;
  }

  /*
   * Perform an action upon the staff being used.
   * Returns true if the action was successful, and false otherwise.
   */
  protected abstract boolean onStaffUse(Player player);
}
