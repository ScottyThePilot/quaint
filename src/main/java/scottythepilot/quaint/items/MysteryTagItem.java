package scottythepilot.quaint.items;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MysteryTagItem extends MysteryItem {
  private final TagKey<Item> tagKey;

  public MysteryTagItem(final TagKey<Item> tagKey, Item.Properties properties) {
    super(properties);
    this.tagKey = tagKey;
  }

  @Override
  public @NotNull ItemStack getResultItemStack(Player player, ItemStack itemStackOriginal) {
    return BuiltInRegistries.ITEM.getTag(this.tagKey)
      .filter((named) -> !player.level().isClientSide())
      .flatMap((named) -> named.getRandomElement(player.getRandom()))
      .map(ItemStack::new).orElse(ItemStack.EMPTY);
  }
}
