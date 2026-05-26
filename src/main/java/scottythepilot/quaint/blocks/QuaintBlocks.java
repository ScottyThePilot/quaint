package scottythepilot.quaint.blocks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public final class QuaintBlocks {
  private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
  }
}
