package scottythepilot.quaint.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class HolyMackerelItem extends SwordItem {
  public HolyMackerelItem(int attackDamage, float attackSpeed, Item.Properties properties) {
    super(TIER, properties.attributes(SwordItem.createAttributes(TIER, attackDamage, attackSpeed)));
  }

  public static final Tier TIER = new SimpleTier(
    BlockTags.INCORRECT_FOR_STONE_TOOL,
    100,
    Tiers.STONE.getSpeed(),
    Tiers.STONE.getAttackDamageBonus(),
    Tiers.DIAMOND.getEnchantmentValue(),
    () -> Ingredient.of(ItemTags.FISHES)
  );
}
