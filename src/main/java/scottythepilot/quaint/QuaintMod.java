package scottythepilot.quaint;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import scottythepilot.quaint.blocks.QuaintBlocks;
import scottythepilot.quaint.commands.SmiteCommand;
import scottythepilot.quaint.data.QuaintData;
import scottythepilot.quaint.effects.QuaintEffects;
import scottythepilot.quaint.entities.QuaintEntities;
import scottythepilot.quaint.items.*;
import scottythepilot.quaint.items.alchemy.QuaintPotions;
import scottythepilot.quaint.loot.QuaintLoot;

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

  public static final GameRules.Key<GameRules.BooleanValue> RULE_KEEP_EXPERIENCE =
    GameRules.register("keepExperience", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
  public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_GIVE_CAMOUFLAGED_ON_RESPAWN =
    GameRules.register("doGiveCamouflagedOnRespawn", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));

  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB =
    CREATIVE_MODE_TABS.register("main", () -> {
      return CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.quaint.main"))
        .withTabsBefore(CreativeModeTabs.OP_BLOCKS)
        .icon(() -> QuaintItems.GOD_PARTICLE.get().getDefaultInstance())
        .displayItems((parameters, output) -> QuaintItems.generateTabItems(output, null))
        .build();
    });

  public QuaintMod(IEventBus modEventBus, ModContainer modContainer) {
    modEventBus.addListener(this::onFMLCommonSetup);

    QuaintBlocks.register(modEventBus);
    QuaintItems.register(modEventBus);
    QuaintData.register(modEventBus);
    QuaintEntities.register(modEventBus);
    QuaintEffects.register(modEventBus);
    QuaintPotions.register(modEventBus);
    QuaintLoot.register(modEventBus);
    QuaintData.register(modEventBus);

    CREATIVE_MODE_TABS.register(modEventBus);

    QuaintConfig.Client.container.register(modContainer);
    QuaintConfig.Server.container.register(modContainer);
  }

  private void onFMLCommonSetup(FMLCommonSetupEvent event) {}

  public static boolean shouldKeepExperience(GameRules gameRules) {
    return gameRules.getBoolean(QuaintMod.RULE_KEEP_EXPERIENCE) && !gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY);
  }

  public static class PlayerFirstJoinEvent extends PlayerEvent {
    public PlayerFirstJoinEvent(Player player) {
      super(player);
    }
  }

  @EventBusSubscriber(modid = QuaintMod.MOD_ID)
  public static final class Events {
    // Modify visibility of entities with the camouflage effect
    @SubscribeEvent
    public static void onLivingVisibility(LivingEvent.LivingVisibilityEvent event) {
      if (event.getEntity().hasEffect(QuaintEffects.CAMOUFLAGED)) {
        event.modifyVisibility(0.0);
      }
    }

    // Prevent entities from targeting entities with the camouflage effect
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
      LivingEntity source = event.getEntity();
      LivingEntity target = event.getNewAboutToBeSetTarget();
      if (target == null) return;
      if (!target.hasEffect(QuaintEffects.CAMOUFLAGED)) return;
      if (QuaintEffects.canEntitySeeThroughCamouflage(source)) return;
      event.setCanceled(true);
    }

    // Remove camouflage effect when an entity with it attacks another entity
    @SubscribeEvent
    public static void onLivingAttackTarget(LivingDamageEvent.Pre event) {
      if (event.getSource().getEntity() instanceof LivingEntity attacker) {
        attacker.removeEffect(QuaintEffects.CAMOUFLAGED);
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

      if (QuaintMod.shouldKeepExperience(player.level().getGameRules())) {
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
    public static void onPlayerFirstJoin(PlayerFirstJoinEvent event) {
      Player player = event.getEntity();
      LOGGER.info("Player {} joining for the first time", player.getGameProfile().getName());

      if (player.level().isClientSide()) return;
      if (player.level().getGameRules().getBoolean(QuaintMod.RULE_DO_GIVE_CAMOUFLAGED_ON_RESPAWN)) {
        QuaintMod.LOGGER.debug("Giving camouflaged effect to player on first login {}", player.getGameProfile().getName());
        player.addEffect(new MobEffectInstance(QuaintEffects.CAMOUFLAGED, QuaintConfig.Server.getCamouflagedDuration(), 0, true, true, true));
      }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
      Player player = event.getEntity();
      if (!player.getData(QuaintData.AttachmentTypes.FIRST_JOIN_DONE)) {
        player.setData(QuaintData.AttachmentTypes.FIRST_JOIN_DONE, true);
        NeoForge.EVENT_BUS.post(new PlayerFirstJoinEvent(player));
      }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
      // Player is respawning after conquering the end, don't grant camouflaged effect
      if (event.isEndConquered()) return;
      Player player = event.getEntity();
      if (player.level().isClientSide()) return;

      if (player.level().getGameRules().getBoolean(QuaintMod.RULE_DO_GIVE_CAMOUFLAGED_ON_RESPAWN)) {
        QuaintMod.LOGGER.debug("Giving camouflaged effect to player on respawn {}", player.getGameProfile().getName());
        player.addEffect(new MobEffectInstance(QuaintEffects.CAMOUFLAGED, QuaintConfig.Server.getCamouflagedDuration(), 0, true, true, true));
      }
    }

    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
      QuaintItems.generateTabItems(event, event.getTabKey());
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipesEvent(RegisterBrewingRecipesEvent event) {
      QuaintPotions.registerBrewingRecipesEvent(event);
    }

    @SubscribeEvent
    public static void onRegisterCommandsEvent(RegisterCommandsEvent event) {
      SmiteCommand.register(event.getDispatcher());
    }
  }
}
