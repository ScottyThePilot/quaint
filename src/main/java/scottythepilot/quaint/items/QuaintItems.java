package scottythepilot.quaint.items;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import scottythepilot.quaint.Utils;
import scottythepilot.quaint.items.alchemy.QuaintPotions;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public final class QuaintItems {
  private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

  public static final class Food {
    public static final FoodProperties GOD_PARTICLE =
      (new FoodProperties.Builder())
        .nutrition(0).saturationModifier(0.0F).alwaysEdible()
        .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 3600, 0), 1.0F)
        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 6000, 1), 1.0F)
        .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 9600, 3), 1.0F)
        .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 9600, 3), 1.0F)
        .build();

    public static final FoodProperties DUBIOUS_DECOCTION =
      (new FoodProperties.Builder())
        .nutrition(0).saturationModifier(0.0F).alwaysEdible()
        .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 3600, 0), 1.0F)
        .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 6000, 0), 1.0F)
        .effect(() -> new MobEffectInstance(MobEffects.WITHER, 9600, 2), 1.0F)
        .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 9600, 2), 1.0F)
        .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 9600, 0), 1.0F)
        .build();
  }

  public static final int STAFF_DURABILITY = 32;

  public static final DeferredItem<Item> GOD_PARTICLE =
    ITEMS.registerSimpleItem("god_particle", (new Item.Properties()).food(Food.GOD_PARTICLE).fireResistant().rarity(Rarity.EPIC));

  public static final DeferredItem<Item> HEART_OF_THE_SUN =
    ITEMS.registerSimpleItem("heart_of_the_sun", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> HEART_OF_THE_RAIN =
    ITEMS.registerSimpleItem("heart_of_the_rain", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> HEART_OF_THE_STORM =
    ITEMS.registerSimpleItem("heart_of_the_storm", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> HEART_OF_THE_MOON =
    ITEMS.registerSimpleItem("heart_of_the_moon", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> HEART_OF_THE_EARTH =
    ITEMS.registerSimpleItem("heart_of_the_earth", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> HEART_OF_THE_SKIES =
    ITEMS.registerSimpleItem("heart_of_the_skies", (new Item.Properties()).rarity(Rarity.UNCOMMON));

  public static final DeferredItem<StaffWeatherItem> STAFF_OF_THE_SUN =
    ITEMS.register("staff_of_the_sun", () -> new StaffWeatherItem(Utils.WeatherType.CLEAR, (new Item.Properties()).durability(STAFF_DURABILITY).setNoRepair()));
  public static final DeferredItem<StaffWeatherItem> STAFF_OF_THE_RAIN =
    ITEMS.register("staff_of_the_rain", () -> new StaffWeatherItem(Utils.WeatherType.RAINING, (new Item.Properties()).durability(STAFF_DURABILITY).setNoRepair()));
  public static final DeferredItem<StaffWeatherItem> STAFF_OF_THE_STORM =
    ITEMS.register("staff_of_the_storm", () -> new StaffWeatherItem(Utils.WeatherType.THUNDERING, (new Item.Properties()).durability(STAFF_DURABILITY).setNoRepair()));

  public static final DeferredItem<MysteryTagItem> MYSTERY_SAPLING =
    ITEMS.register("mystery_sapling", () -> new MysteryTagItem(ItemTags.SAPLINGS, new Item.Properties()));
  public static final DeferredItem<MysteryTagItem> MYSTERY_SEEDS =
    ITEMS.register("mystery_seeds", () -> new MysteryTagItem(Tags.Items.SEEDS, new Item.Properties()));
  public static final DeferredItem<MysteryLootItem> MYSTERY_BOX =
    ITEMS.register("mystery_box", () -> new MysteryLootItem(new Item.Properties()));

  public static final DeferredItem<HolyMackerelItem> HOLY_MACKEREL =
    ITEMS.register("holy_mackerel", () -> new HolyMackerelItem(3, -2.4F, new Item.Properties()));

  public static final DeferredItem<Item> DUBIOUS_DECOCTION =
    ITEMS.register("dubious_decoction", () -> new BottleItem(40, (new Item.Properties()).food(Food.DUBIOUS_DECOCTION).stacksTo(16)));

  public static void register(IEventBus modEventBus) {
    ITEMS.register(modEventBus);
  }

  private static final Item[] POTION_ITEMS = new Item[] {
    Items.POTION,
    Items.SPLASH_POTION,
    Items.LINGERING_POTION,
    Items.TIPPED_ARROW
  };

  public static void generateTabItems(CreativeModeTab.Output output, @Nullable ResourceKey<CreativeModeTab> tabKey) {
    if (tabKey == null || tabKey == CreativeModeTabs.INGREDIENTS) {
      output.accept(HEART_OF_THE_SUN.get());
      output.accept(HEART_OF_THE_RAIN.get());
      output.accept(HEART_OF_THE_STORM.get());
      output.accept(HEART_OF_THE_MOON.get());
      output.accept(HEART_OF_THE_EARTH.get());
      output.accept(HEART_OF_THE_SKIES.get());
    }

    if (tabKey == null || tabKey == CreativeModeTabs.TOOLS_AND_UTILITIES) {
      output.accept(STAFF_OF_THE_SUN.get());
      output.accept(STAFF_OF_THE_RAIN.get());
      output.accept(STAFF_OF_THE_STORM.get());
      output.accept(MYSTERY_SAPLING.get());
      output.accept(MYSTERY_SEEDS.get());
      output.accept(MYSTERY_BOX.get());
    }

    if (tabKey == null || tabKey == CreativeModeTabs.COMBAT) {
      output.accept(HOLY_MACKEREL.get());
    }

    if (tabKey == null || tabKey == CreativeModeTabs.FOOD_AND_DRINKS) {
      output.accept(DUBIOUS_DECOCTION.get());
    }

    if (tabKey == null) {
      for (final Item item: QuaintItems.POTION_ITEMS) {
        output.accept(PotionContents.createItemStack(item, QuaintPotions.CLARITY));
        output.accept(PotionContents.createItemStack(item, QuaintPotions.DOOM));
      }

      output.accept(GOD_PARTICLE.get());
    }
  }
}
