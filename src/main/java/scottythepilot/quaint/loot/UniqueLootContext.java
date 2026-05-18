package scottythepilot.quaint.loot;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Stream;

public enum UniqueLootContext implements StringRepresentable {
  LEVEL("level"), CHUNK("chunk"), ENTITY("entity");

  public static final Codec<UniqueLootContext> CODEC = StringRepresentable.fromEnum(UniqueLootContext::values);

  public final String name;

  UniqueLootContext(final String name) {
    this.name = name;
  }

  @Override
  public @NotNull String getSerializedName() {
    return this.name;
  }

  public IAttachmentHolder[] getSubjects(LootContext lootContext) {
    Entity entity = lootContext.getParam(LootContextParams.THIS_ENTITY);
    Level level = lootContext.getLevel();
    ChunkAccess chunk = level.getChunkAt(entity.blockPosition());
    return switch (this) {
      case LEVEL -> new IAttachmentHolder[] { level, chunk, entity };
      case CHUNK -> new IAttachmentHolder[] { chunk, entity };
      case ENTITY -> new IAttachmentHolder[] { entity };
    };
  }

  public IAttachmentHolder getSubject(LootContext lootContext) {
    Entity entity = lootContext.getParam(LootContextParams.THIS_ENTITY);
    Level level = lootContext.getLevel();
    ChunkAccess chunk = level.getChunkAt(entity.blockPosition());
    return switch (this) {
      case LEVEL -> level;
      case CHUNK -> chunk;
      case ENTITY -> entity;
    };
  }

  public int getQuantityFor(LootContext lootContext, String identifier) {
    return Stream.of(this.getSubjects(lootContext))
      .map(UniqueLootData::getAttachment)
      .map((uniqueLootData -> uniqueLootData.get(identifier)))
      .mapToInt(Integer::intValue)
      .sum();
  }

  public void setQuantityFor(LootContext lootContext, String identifier, int quantity) {
    if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");
    UniqueLootData.updateAttachmentPut(this.getSubject(lootContext), identifier, quantity);
  }

  public void clearQuantityFor(LootContext lootContext, String identifier) {
    for (IAttachmentHolder attachmentHolder: this.getSubjects(lootContext)) {
      UniqueLootData.updateAttachmentPut(attachmentHolder, identifier, 0);
    }
  }

  public void incrementQuantityFor(LootContext lootContext, String identifier) {
    UniqueLootData.updateAttachmentIncrement(this.getSubject(lootContext), identifier);
  }
}
