package scottythepilot.quaint.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import scottythepilot.quaint.QuaintConfig;
import scottythepilot.quaint.Utils;

public class StaffWeatherItem extends StaffItem {
  private final Utils.WeatherType weatherType;

  public StaffWeatherItem(final Utils.WeatherType weatherType, Item.Properties properties) {
    super(properties);
    this.weatherType = weatherType;
  }

  @Override
  protected boolean onStaffUse(Player player) {
    if (Utils.WeatherType.of(player.level()) == this.weatherType) {
      player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".failure.same_weather"), true);
      return false;
    }

    if (!Utils.isLevelWeatherSupported(player.level())) {
      player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".failure.wrong_dimension"), true);
      return false;
    }

    if (player.level() instanceof ServerLevel serverLevel) {
      int duration = QuaintConfig.Server.getStaffWeatherDurationRandom(player.getRandom());
      this.weatherType.apply(serverLevel, duration);
    }

    player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".success"), true);
    return true;
  }
}
