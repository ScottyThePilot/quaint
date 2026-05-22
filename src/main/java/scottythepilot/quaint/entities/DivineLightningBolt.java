package scottythepilot.quaint.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;

public class DivineLightningBolt extends LightningBolt {
  public DivineLightningBolt(EntityType<? extends DivineLightningBolt> entityType, Level level) {
    super(entityType, level);
  }

  @Override
  protected void spawnFire(int extraIgnitions) {}
}
