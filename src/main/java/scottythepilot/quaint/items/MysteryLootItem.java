package scottythepilot.quaint.items;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import scottythepilot.quaint.QuaintConfig;
import scottythepilot.quaint.Utils;
import scottythepilot.quaint.data.QuaintData;

public class MysteryLootItem extends MysteryItem {
  public MysteryLootItem(Item.Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(
    @NotNull ItemStack stack,
    @NotNull TooltipContext context,
    @NotNull List<Component> tooltipComponents,
    @NotNull TooltipFlag tooltipFlag
  ) {
    ResourceKey<LootTable> lootTableKey = this.getLootTableKey(stack);
    if (lootTableKey == null) {
      tooltipComponents.add(Component.translatable(
        this.getDescriptionId() + ".loot_table.none"
      ));
    } else {
      tooltipComponents.add(Component.translatable(
        this.getDescriptionId() + ".loot_table",
        QuaintConfig.Client.displayLootTableKey(lootTableKey.location())
      ));
    }
  }

  @Override
  public @NotNull ItemStack getResultItemStack(Player player, ItemStack itemStack) {
    ResourceKey<LootTable> lootTableKey = this.getLootTableKey(itemStack);
    if (lootTableKey == null) throw new NullPointerException("Returned loot table key was null");
    final long lootTableSeed = player.getRandom().nextLong();
    return Utils.unpackLootTableSingleStack(player, lootTableKey, lootTableSeed);
  }

  public void setLootTableKey(ItemStack itemStack, ResourceKey<LootTable> lootTableKey) {
    if (lootTableKey == null) {
      itemStack.remove(QuaintData.DataComponentTypes.LOOT_TABLE);
    } else {
      itemStack.set(QuaintData.DataComponentTypes.LOOT_TABLE, lootTableKey);
    }
  }

  public @Nullable ResourceKey<LootTable> getLootTableKey(ItemStack itemStack) {
    return itemStack.get(QuaintData.DataComponentTypes.LOOT_TABLE);
  }
}
