package scottythepilot.quaint;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.apache.commons.lang3.IntegerRange;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class Utils {
  public static boolean roll(final double chance, RandomSource randomSource) {
    return randomSource.nextDouble() <= chance;
  }

  public static int sampleRange(final IntegerRange range, RandomSource randomSource) {
    return randomSource.nextInt(range.getMinimum(), range.getMaximum());
  }

  public enum WeatherType {
    CLEAR, RAINING, THUNDERING;

    public static WeatherType of(Level level) {
      if (level.isThundering()) {
        return WeatherType.THUNDERING;
      } else if (level.isRaining()) {
        return WeatherType.RAINING;
      } else {
        return WeatherType.CLEAR;
      }
    }

    public void apply(ServerLevel serverLevel, int length) {
      switch (this) {
        case WeatherType.CLEAR:
          Utils.setWeatherClear(serverLevel, length);
          break;
        case WeatherType.RAINING:
          Utils.setWeatherRaining(serverLevel, length);
          break;
        case WeatherType.THUNDERING:
          Utils.setWeatherThundering(serverLevel, length);
          break;
      }
    }
  }

  public static void setWeatherClear(ServerLevel level, int length) {
    QuaintMod.LOGGER.debug("Setting weather to clear");
    level.setWeatherParameters(length, 0, false, false);
  }

  public static void setWeatherRaining(ServerLevel level, int length) {
    QuaintMod.LOGGER.debug("Setting weather to rain");
    level.setWeatherParameters(0, length, true, false);
  }

  public static void setWeatherThundering(ServerLevel level, int length) {
    QuaintMod.LOGGER.debug("Setting weather to thunder");
    level.setWeatherParameters(0, length, true, true);
  }

  public static String prettifyResource(String string) {
    return Arrays.stream(string.replaceAll("[/:]", "_/_").split("[-_.]"))
      .map((s) -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
      .collect(Collectors.joining(" "));
  }

  public static ItemStack unpackLootTableSingleStack(@NotNull Player player, @Nullable ResourceKey<LootTable> lootTable, long lootTableSeed) {
    ResultItemContainer resultItemContainer = new ResultItemContainer(player, lootTable, lootTableSeed);
    resultItemContainer.unpackLootTable(player);
    return resultItemContainer.getItem();
  }

  public static class ResultItemContainer extends SimpleContainer implements RandomizableContainer {
    public final @NotNull Player player;
    private @Nullable ResourceKey<LootTable> lootTable;
    private long lootTableSeed;

    private ResultItemContainer(@NotNull Player player) {
      super(1);
      this.player = player;
    }

    public ResultItemContainer(@NotNull Player player, @Nullable ResourceKey<LootTable> lootTable, long lootTableSeed) {
      this(player);
      this.lootTable = lootTable;
      this.lootTableSeed = lootTableSeed;
    }

    public @NotNull ItemStack getItem() {
      return this.getItem(0);
    }

    public @NotNull ItemStack removeItem(int count) {
      return this.removeItem(0, count);
    }

    public void setItem(@NotNull ItemStack stack) {
      this.setItem(0, stack);
    }

    @Override
    public @NotNull BlockPos getBlockPos() {
      return this.player.blockPosition();
    }

    @Override
    public @NotNull Level getLevel() {
      return this.player.level();
    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
      return this.lootTable;
    }

    @Override
    public long getLootTableSeed() {
      return this.lootTableSeed;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
      this.lootTable = lootTable;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {
      this.lootTableSeed = lootTableSeed;
    }
  }
}
