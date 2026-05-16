package scottythepilot.quaint.entities;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;

public class DivineLightningBolt extends LightningBolt {
  public DivineLightningBolt(EntityType<? extends DivineLightningBolt> entityType, Level level) {
    super(entityType, level);
  }

  @Override
  protected void spawnFire(int extraIgnitions) {}

  // This looks really awful but it works
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static final EntityRendererProvider<DivineLightningBolt> ENTITY_RENDERER_PROVIDER =
    (EntityRendererProvider<DivineLightningBolt>)(EntityRendererProvider)LightningBoltRenderer::new;
}
