package scottythepilot.quaint.entities;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public final class QuaintEntities {
  private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MOD_ID);

  public static final DeferredHolder<EntityType<?>, EntityType<DivineLightningBolt>> DIVINE_LIGHTNING_BOLT =
    ENTITY_TYPES.register("divine_lightning_bolt", () -> {
      return EntityType.Builder.of(DivineLightningBolt::new, MobCategory.MISC)
        .noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE)
        .build("divine_lightning_bolt");
    });

  public static void register(IEventBus modEventBus) {
    ENTITY_TYPES.register(modEventBus);
  }

  public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    // This looks really awful but it works
    @SuppressWarnings({ "rawtypes", "unchecked" })
    final EntityRendererProvider<DivineLightningBolt> DIVINE_LIGHTNING_BOLT_ENTITY_RENDERER_PROVIDER =
      (EntityRendererProvider<DivineLightningBolt>)(EntityRendererProvider) LightningBoltRenderer::new;
    event.registerEntityRenderer(DIVINE_LIGHTNING_BOLT.get(), DIVINE_LIGHTNING_BOLT_ENTITY_RENDERER_PROVIDER);
  }
}
