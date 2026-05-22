package scottythepilot.quaint;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import scottythepilot.quaint.commands.SmiteCommand;
import scottythepilot.quaint.effects.ClarityMobEffect;
import scottythepilot.quaint.effects.DoomMobEffect;
import scottythepilot.quaint.entities.DivineLightningBolt;
import scottythepilot.quaint.items.*;
import scottythepilot.quaint.items.BottleItem;

@Mod(QuaintMod.MOD_ID)
public class QuaintMod {
  public static final String MOD_ID = "quaint";

  public static final Logger LOGGER = LogUtils.getLogger();

  public static ResourceLocation at(String name) {
    return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
  }

  public static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registryKey, String name) {
    return ResourceKey.create(registryKey, QuaintMod.at(name));
  }

  public static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryKey, String name) {
    return TagKey.create(registryKey, QuaintMod.at(name));
  }

  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
  public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MOD_ID);
  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
  public static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, MOD_ID);
  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MOD_ID);
  public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MOD_ID);
  public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, MOD_ID);

  public static final FoodProperties FOOD_GOD_PARTICLE =
    (new FoodProperties.Builder())
      .nutrition(0).saturationModifier(0.0F).alwaysEdible()
      .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 3600, 0), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 6000, 1), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 9600, 3), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 9600, 3), 1.0F)
      .build();

  public static final DeferredItem<Item> ITEM_GOD_PARTICLE =
    ITEMS.registerSimpleItem("god_particle", (new Item.Properties()).food(FOOD_GOD_PARTICLE).fireResistant().rarity(Rarity.EPIC));

  public static final DeferredItem<Item> ITEM_HEART_OF_THE_SUN =
    ITEMS.registerSimpleItem("heart_of_the_sun", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> ITEM_HEART_OF_THE_RAIN =
    ITEMS.registerSimpleItem("heart_of_the_rain", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> ITEM_HEART_OF_THE_STORM =
    ITEMS.registerSimpleItem("heart_of_the_storm", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> ITEM_HEART_OF_THE_MOON =
    ITEMS.registerSimpleItem("heart_of_the_moon", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> ITEM_HEART_OF_THE_EARTH =
    ITEMS.registerSimpleItem("heart_of_the_earth", (new Item.Properties()).rarity(Rarity.UNCOMMON));
  public static final DeferredItem<Item> ITEM_HEART_OF_THE_SKIES =
    ITEMS.registerSimpleItem("heart_of_the_skies", (new Item.Properties()).rarity(Rarity.UNCOMMON));

  public static final int STAFF_DURABILITY = 32;

  public static final DeferredItem<StaffWeatherItem> ITEM_STAFF_OF_THE_SUN =
    ITEMS.register("staff_of_the_sun", () -> new StaffWeatherItem(Utils.WeatherType.CLEAR, (new Item.Properties()).durability(STAFF_DURABILITY).setNoRepair()));
  public static final DeferredItem<StaffWeatherItem> ITEM_STAFF_OF_THE_RAIN =
    ITEMS.register("staff_of_the_rain", () -> new StaffWeatherItem(Utils.WeatherType.RAINING, (new Item.Properties()).durability(STAFF_DURABILITY).setNoRepair()));
  public static final DeferredItem<StaffWeatherItem> ITEM_STAFF_OF_THE_STORM =
    ITEMS.register("staff_of_the_storm", () -> new StaffWeatherItem(Utils.WeatherType.THUNDERING, (new Item.Properties()).durability(STAFF_DURABILITY).setNoRepair()));

  public static final DeferredItem<MysteryTagItem> ITEM_MYSTERY_SAPLING =
    ITEMS.register("mystery_sapling", () -> new MysteryTagItem(ItemTags.SAPLINGS, new Item.Properties()));
  public static final DeferredItem<MysteryTagItem> ITEM_MYSTERY_SEEDS =
    ITEMS.register("mystery_seeds", () -> new MysteryTagItem(Tags.Items.SEEDS, new Item.Properties()));
  public static final DeferredItem<MysteryLootItem> ITEM_MYSTERY_BOX =
    ITEMS.register("mystery_box", () -> new MysteryLootItem(new Item.Properties()));

  public static final DeferredItem<HolyMackerelItem> ITEM_HOLY_MACKEREL =
    ITEMS.register("holy_mackerel", () -> new HolyMackerelItem(3, -2.4F, new Item.Properties()));

  public static final FoodProperties FOOD_DUBIOUS_DECOCTION =
    (new FoodProperties.Builder())
      .nutrition(0).saturationModifier(0.0F).alwaysEdible()
      .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 3600, 0), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 6000, 0), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.WITHER, 9600, 2), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 9600, 2), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 9600, 0), 1.0F)
      .build();

  public static final DeferredItem<Item> ITEM_DUBIOUS_DECOCTION =
    ITEMS.register("dubious_decoction", () -> new BottleItem(40, (new Item.Properties()).food(FOOD_DUBIOUS_DECOCTION).stacksTo(16)));

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceKey<LootTable>>> DATA_COMPONENT_LOOT_TABLE =
    DATA_COMPONENTS.registerComponentType("loot_table", (builder) -> {
      return builder
        .persistent(ResourceKey.codec(Registries.LOOT_TABLE))
        .networkSynchronized(ResourceKey.streamCodec(Registries.LOOT_TABLE));
    });

  public static final ResourceKey<DamageType> DAMAGE_TYPE_DOOM = QuaintMod.key(Registries.DAMAGE_TYPE, "doom");

  public static DamageSource damageSourceDoom(RegistryAccess registryAccess) {
    return QuaintMod.damageSource(registryAccess, DAMAGE_TYPE_DOOM);
  }

  public static DamageSource damageSource(RegistryAccess registryAccess, ResourceKey<DamageType> damageType) {
    return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType));
  }

  public static final DeferredHolder<EntityType<?>, EntityType<DivineLightningBolt>> ENTITY_TYPE_DIVINE_LIGHTNING_BOLT =
    ENTITY_TYPES.register("divine_lightning_bolt", () -> {
      return EntityType.Builder.of(DivineLightningBolt::new, MobCategory.MISC)
        .noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE)
        .build("divine_lightning_bolt");
    });

  public static final DeferredHolder<MobEffect, MobEffect> MOB_EFFECT_CAMOUFLAGED =
    MOB_EFFECTS.register("camouflaged", () -> new MobEffect(MobEffectCategory.NEUTRAL, 0xCCCCCC) {});
  public static final DeferredHolder<MobEffect, MobEffect> MOB_EFFECT_CLARITY =
    MOB_EFFECTS.register("clarity", () -> new ClarityMobEffect(MobEffectCategory.NEUTRAL, 0xFFFFFF) {});
  public static final DeferredHolder<MobEffect, MobEffect> MOB_EFFECT_DOOM =
    MOB_EFFECTS.register("doom", () -> new DoomMobEffect(MobEffectCategory.HARMFUL, 0x202020));

  public static final DeferredHolder<Potion, Potion> POTION_CLARITY =
    POTIONS.register("clarity", () -> new Potion("clarity", new MobEffectInstance(MOB_EFFECT_CLARITY)));
  public static final DeferredHolder<Potion, Potion> POTION_DOOM =
    POTIONS.register("doom", () -> new Potion("doom", new MobEffectInstance(MOB_EFFECT_DOOM, 48000)));

  public static TagKey<EntityType<?>> TAG_ENTITY_TYPE_SEES_THROUGH_CAMOUFLAGE =
    QuaintMod.tag(Registries.ENTITY_TYPE, "sees_through_camouflage");

  public static final GameRules.Key<GameRules.BooleanValue> RULE_KEEP_EXPERIENCE =
    GameRules.register("keepExperience", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
  public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_GIVE_CAMOUFLAGED_ON_RESPAWN =
    GameRules.register("doGiveCamouflagedOnRespawn", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB =
    CREATIVE_MODE_TABS.register("main", () -> {
      return CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.quaint.main"))
        .withTabsBefore(CreativeModeTabs.OP_BLOCKS)
        .icon(() -> ITEM_GOD_PARTICLE.get().getDefaultInstance())
        .displayItems(QuaintMod::generateDisplayItems)
        .build();
    });

  private static void generateDisplayItems(ItemDisplayParameters parameters, CreativeModeTab.Output output) {
    output.accept(ITEM_HEART_OF_THE_SUN.get());
    output.accept(ITEM_HEART_OF_THE_RAIN.get());
    output.accept(ITEM_HEART_OF_THE_STORM.get());
    output.accept(ITEM_HEART_OF_THE_MOON.get());
    output.accept(ITEM_HEART_OF_THE_EARTH.get());
    output.accept(ITEM_HEART_OF_THE_SKIES.get());

    output.accept(ITEM_STAFF_OF_THE_SUN.get());
    output.accept(ITEM_STAFF_OF_THE_RAIN.get());
    output.accept(ITEM_STAFF_OF_THE_STORM.get());

    output.accept(ITEM_HOLY_MACKEREL.get());

    output.accept(ITEM_MYSTERY_SAPLING.get());
    output.accept(ITEM_MYSTERY_SEEDS.get());
    output.accept(ITEM_MYSTERY_BOX.get());

    output.accept(ITEM_DUBIOUS_DECOCTION.get());

    output.accept(ITEM_GOD_PARTICLE.get());
  }

  public QuaintMod(IEventBus modEventBus, ModContainer modContainer) {
    modEventBus.addListener(this::onFMLCommonSetup);

    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    DATA_COMPONENTS.register(modEventBus);
    CREATIVE_MODE_TABS.register(modEventBus);
    DAMAGE_TYPES.register(modEventBus);
    ENTITY_TYPES.register(modEventBus);
    MOB_EFFECTS.register(modEventBus);
    POTIONS.register(modEventBus);

    QuaintConfig.Client.container.register(modContainer);
    QuaintConfig.Server.container.register(modContainer);
  }

  private void onFMLCommonSetup(FMLCommonSetupEvent event) {}

  public static boolean shouldKeepExperience(GameRules gameRules) {
    return gameRules.getBoolean(RULE_KEEP_EXPERIENCE) && !gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY);
  }

  public static boolean shouldGiveCamouflagedEffect(GameRules gameRules) {
    return gameRules.getBoolean(RULE_DO_GIVE_CAMOUFLAGED_ON_RESPAWN);
  }

  public static void giveCamouflagedEffect(Player player) {
    LOGGER.debug("Giving camouflaged effect to player {}", player.getGameProfile().getName());
    player.addEffect(new MobEffectInstance(MOB_EFFECT_CAMOUFLAGED, QuaintConfig.Server.getCamouflagedDuration(), 0, true, true, true));
  }

  public static boolean canEntitySeeThroughCamouflage(LivingEntity livingEntity) {
    return livingEntity.getType().is(BuiltInRegistries.ENTITY_TYPE.getTag(TAG_ENTITY_TYPE_SEES_THROUGH_CAMOUFLAGE).orElseThrow());
  }

  @EventBusSubscriber(modid = QuaintMod.MOD_ID, value = Dist.CLIENT)
  public static class ClientEvents {
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
      // This looks really awful but it works
      @SuppressWarnings({ "rawtypes", "unchecked" })
      final EntityRendererProvider<DivineLightningBolt> DIVINE_LIGHTNING_BOLT_ENTITY_RENDERER_PROVIDER =
        (EntityRendererProvider<DivineLightningBolt>)(EntityRendererProvider) LightningBoltRenderer::new;
      event.registerEntityRenderer(ENTITY_TYPE_DIVINE_LIGHTNING_BOLT.get(), DIVINE_LIGHTNING_BOLT_ENTITY_RENDERER_PROVIDER);
    }
  }

  @EventBusSubscriber(modid = QuaintMod.MOD_ID)
  public static class Events {
    // Modify visibility of entities with the sorrow effect
    @SubscribeEvent
    public static void onLivingVisibility(LivingEvent.LivingVisibilityEvent event) {
      if (event.getEntity().hasEffect(MOB_EFFECT_CAMOUFLAGED)) {
        event.modifyVisibility(0.0);
      }
    }

    // Prevent entities from targeting entities with the sorrow effect
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
      LivingEntity source = event.getEntity();
      LivingEntity target = event.getNewAboutToBeSetTarget();
      if (target == null) return;
      if (!target.hasEffect(MOB_EFFECT_CAMOUFLAGED)) return;
      if (canEntitySeeThroughCamouflage(source)) return;
      event.setCanceled(true);
    }

    // Remove sorrow effect when an entity with it attacks another entity
    @SubscribeEvent
    public static void onLivingAttackTarget(LivingDamageEvent.Pre event) {
      if (event.getSource().getEntity() instanceof LivingEntity attacker) {
        if (attacker.hasEffect(MOB_EFFECT_CAMOUFLAGED)) {
          attacker.removeEffect(MOB_EFFECT_CAMOUFLAGED);
        }
      }
    }

    // Give player back their levels on death if keep experience is enabled
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
      if (!event.isWasDeath()) return;
      Player player = event.getEntity();
      if (player.level().isClientSide()) return;

      int currentXp = event.getEntity().experienceLevel;
      int originalXp = event.getOriginal().experienceLevel;

      if (shouldKeepExperience(player.level().getGameRules())) {
        if (currentXp == 0) {
          LOGGER.debug("Giving player {} their levels back", player.getGameProfile().getName());
          player.giveExperienceLevels(originalXp);
        } else {
          LOGGER.warn("Expected respawning player experience to be zero, found {} experience", currentXp);
        }
      }
    }

    // Prevent player from dropping their levels on death if keep experience is enabled
    @SubscribeEvent
    public static void onPlayerDropExperience(LivingExperienceDropEvent event) {
      if (!(event.getEntity() instanceof Player player)) return;
      if (player.level().isClientSide()) return;

      if (shouldKeepExperience(player.level().getGameRules())) {
        LOGGER.debug("Preventing player {} from dropping levels", player.getGameProfile().getName());
        event.setCanceled(true);
      }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
      Player player = event.getEntity();
      if (player.level().isClientSide()) return;

      if (!player.getPersistentData().getBoolean(QuaintMod.MOD_ID + ".first_join_done")) {
        player.getPersistentData().putBoolean(QuaintMod.MOD_ID + ".first_join_done", true);
        if (shouldGiveCamouflagedEffect(player.level().getGameRules())) {
          giveCamouflagedEffect(player);
        }
      }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
      // Player is respawning after conquering the end, don't grant camouflaged effect
      if (event.isEndConquered()) return;
      Player player = event.getEntity();
      if (player.level().isClientSide()) return;

      if (shouldGiveCamouflagedEffect(player.level().getGameRules())) {
        giveCamouflagedEffect(player);
      }
    }

    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
      if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
        event.accept(ITEM_HEART_OF_THE_SUN.get());
        event.accept(ITEM_HEART_OF_THE_RAIN.get());
        event.accept(ITEM_HEART_OF_THE_STORM.get());
        event.accept(ITEM_HEART_OF_THE_MOON.get());
        event.accept(ITEM_HEART_OF_THE_EARTH.get());
        event.accept(ITEM_HEART_OF_THE_SKIES.get());
      }

      if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
        event.accept(ITEM_DUBIOUS_DECOCTION.get());
      }

      if (event.getTabKey() == CreativeModeTabs.COMBAT) {
        event.accept(ITEM_HOLY_MACKEREL.get());
      }

      if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
        event.accept(ITEM_STAFF_OF_THE_SUN.get());
        event.accept(ITEM_STAFF_OF_THE_RAIN.get());
        event.accept(ITEM_STAFF_OF_THE_STORM.get());
      }
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipesEvent(RegisterBrewingRecipesEvent event) {
      event.getBuilder().addStartMix(ITEM_DUBIOUS_DECOCTION.get(), POTION_DOOM);
      event.getBuilder().addStartMix(Items.MILK_BUCKET, POTION_CLARITY);
    }

    @SubscribeEvent
    public static void onRegisterCommandsEvent(RegisterCommandsEvent event) {
      SmiteCommand.register(event.getDispatcher());
    }
  }
}
