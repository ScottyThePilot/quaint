package scottythepilot.quaint.loot;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import scottythepilot.quaint.data.QuaintData;

import java.util.*;
import java.util.function.Function;

public class UniqueLootData extends HashMap<String, Integer> {
  public static final Codec<UniqueLootData> CODEC =
    Codec.unboundedMap(Codec.STRING, Codec.INT).xmap(UniqueLootData::new, o -> o);
  public static final StreamCodec<ByteBuf, UniqueLootData> STREAM_CODEC =
    ByteBufCodecs.map(UniqueLootData::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.INT);

  public UniqueLootData() {
    super();
  }

  public UniqueLootData(int i) {
    super(i);
  }

  public UniqueLootData(Map<String, Integer> map) {
    super(map);
  }

  public int get(String identifier) {
    return this.getOrDefault(identifier, 0);
  }

  public int increment(String identifier) {
    return this.merge(identifier, 0, (i, quantity) -> quantity + 1);
  }

  public static UniqueLootData getAttachment(IAttachmentHolder attachmentHolder) {
    return attachmentHolder.getData(QuaintData.AttachmentTypes.UNIQUE_LOOT);
  }

  public void setAttachment(IAttachmentHolder attachmentHolder) {
    attachmentHolder.setData(QuaintData.AttachmentTypes.UNIQUE_LOOT, this);
  }

  public static void updateAttachment(IAttachmentHolder attachmentHolder, Function<UniqueLootData, UniqueLootData> function) {
    function.apply(UniqueLootData.getAttachment(attachmentHolder)).setAttachment(attachmentHolder);
  }

  public static void updateAttachmentPut(IAttachmentHolder attachmentHolder, String identifier, int quantity) {
    UniqueLootData.updateAttachment(attachmentHolder, (uniqueLootData) -> {
      uniqueLootData.put(identifier, quantity);
      return uniqueLootData;
    });
  }

  public static void updateAttachmentIncrement(IAttachmentHolder attachmentHolder, String identifier) {
    UniqueLootData.updateAttachment(attachmentHolder, (uniqueLootData) -> {
      uniqueLootData.increment(identifier);
      return uniqueLootData;
    });
  }
}
