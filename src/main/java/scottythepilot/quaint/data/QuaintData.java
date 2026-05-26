package scottythepilot.quaint.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import scottythepilot.quaint.loot.UniqueLootData;
import java.util.function.Supplier;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public final class QuaintData {
  public static final class DataComponentTypes {
    private static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MOD_ID);

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerDataComponentType(String name, Codec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {
      return DATA_COMPONENT_TYPES.registerComponentType(name, (builder) -> builder.persistent(codec).networkSynchronized(streamCodec));
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerDataComponentType(String name, Codec<T> codec) {
      return DATA_COMPONENT_TYPES.registerComponentType(name, (builder) -> builder.persistent(codec));
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceKey<LootTable>>> LOOT_TABLE =
      registerDataComponentType("loot_table", ResourceKey.codec(Registries.LOOT_TABLE), ResourceKey.streamCodec(Registries.LOOT_TABLE));
  }

  public static final class AttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> registerAttachmentType(String name, Supplier<T> defaultValue, Codec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {
      return ATTACHMENT_TYPES.register(name, () -> AttachmentType.builder(defaultValue).serialize(codec).sync(streamCodec).build());
    }

    private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> registerAttachmentType(String name, Supplier<T> defaultValue, Codec<T> codec) {
      return ATTACHMENT_TYPES.register(name, () -> AttachmentType.builder(defaultValue).serialize(codec).build());
    }

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> FIRST_JOIN_DONE =
      registerAttachmentType("first_join_done", () -> false, Codec.BOOL, ByteBufCodecs.BOOL);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<UniqueLootData>> UNIQUE_LOOT =
      registerAttachmentType("unique_loot", UniqueLootData::new, UniqueLootData.CODEC, UniqueLootData.STREAM_CODEC);
  }

  public static void register(IEventBus modEventBus) {
    DataComponentTypes.DATA_COMPONENT_TYPES.register(modEventBus);
    AttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
  }
}
