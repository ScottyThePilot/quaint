package scottythepilot.quaint.commands;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import static scottythepilot.quaint.QuaintMod.MOD_ID;

public final class QuaintCommands {
  private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES =
    DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, MOD_ID);

  public static void register(IEventBus modEventBus) {
    COMMAND_ARGUMENT_TYPES.register(modEventBus);
  }

  public static void registerCommandsEvent(RegisterCommandsEvent event) {
    SmiteCommand.register(event.getDispatcher());
  }
}
