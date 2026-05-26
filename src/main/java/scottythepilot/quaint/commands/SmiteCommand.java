package scottythepilot.quaint.commands;

import java.util.Collection;
import java.util.Locale;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import scottythepilot.quaint.QuaintMod;
import scottythepilot.quaint.entities.DivineLightningBolt;
import scottythepilot.quaint.entities.QuaintEntities;

public class SmiteCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(
      Commands.literal("smite")
        .requires((source) -> source.hasPermission(2))
        .executes((source) -> smite(source.getSource(), ImmutableList.of(source.getSource().getEntityOrException())))
        .then(
          Commands.argument("targets", EntityArgument.entities())
            .executes((source) -> smite(source.getSource(), EntityArgument.getEntities(source, "targets")))
        )
        .then(
          Commands.argument("location", Vec3Argument.vec3())
            .executes((source) -> smite(source.getSource(), Vec3Argument.getCoordinates(source, "location")))
        )
    );
  }

  private static int smite(CommandSourceStack source, Collection<? extends Entity> targets) {
    for (Entity target: targets) {
      smite(source, Vec3.atBottomCenterOf(target.blockPosition()));
    }

    if (targets.size() == 1) {
      Entity target = targets.iterator().next();

      source.sendSuccess(() -> {
        return Component.translatable(
          "commands.smite.success.single",
          target.getDisplayName()
        );
      }, true);
    } else {
      source.sendSuccess(() -> {
        return Component.translatable(
          "commands.smite.success.multiple",
          targets.size()
        );
      }, true);
    }

    return targets.size();
  }

  private static int smite(CommandSourceStack source, Coordinates location) {
    final Vec3 position = location.getPosition(source);
    smite(source, position);

    source.sendSuccess(() -> {
      return Component.translatable(
        "commands.smite.success.location",
        formatDouble(position.x),
        formatDouble(position.y),
        formatDouble(position.z)
      );
    }, true);

    return 1;
  }

  private static void smite(CommandSourceStack source, final Vec3 position) {
    DivineLightningBolt lightningBolt = QuaintEntities.DIVINE_LIGHTNING_BOLT.get().create(source.getLevel());
    if (lightningBolt == null) throw new NullPointerException("Newly created divine lightning bolt was null");
    lightningBolt.moveTo(position);
    source.getLevel().addFreshEntity(lightningBolt);
  }

  private static String formatDouble(double i) {
    return String.format(Locale.ROOT, "%f", i);
  }
}
