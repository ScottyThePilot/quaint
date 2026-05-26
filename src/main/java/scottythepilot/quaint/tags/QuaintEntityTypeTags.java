package scottythepilot.quaint.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import scottythepilot.quaint.QuaintMod;

public final class QuaintEntityTypeTags {
  private static TagKey<EntityType<?>> tag(String name) {
    return QuaintMod.tag(Registries.ENTITY_TYPE, name);
  }

  public static final TagKey<EntityType<?>> SEES_THROUGH_CAMOUFLAGE = tag("sees_through_camouflage");
}
