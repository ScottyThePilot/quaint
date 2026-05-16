package scottythepilot.quaint;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import org.apache.commons.lang3.IntegerRange;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.common.ModConfigSpec;
import java.util.function.Function;

public final class QuaintConfig {
  public record Container<I>(I instance, ModConfigSpec spec, ModConfig.Type type) {
    Container(Pair<I, ModConfigSpec> pair, ModConfig.Type type) {
      this(pair.getLeft(), pair.getRight(), type);
    }

    Container(Function<ModConfigSpec.Builder, I> constructor, ModConfig.Type type) {
      this(new ModConfigSpec.Builder().configure(constructor), type);
    }

    public void register(ModContainer modContainer) {
      modContainer.registerConfig(this.type, this.spec);
    }
  }

  public static class Client {
    static final Container<Client> container = new Container<>(Client::new, ModConfig.Type.CLIENT);

    private final ModConfigSpec.BooleanValue lootTableKeysPrettify;
    private final ModConfigSpec.BooleanValue lootTableKeysIncludeNamespace;

    private Client(ModConfigSpec.Builder builder) {
      builder
        .comment("Options for how loot table keys should be displayed in game")
        .translation("quaint.configuration.client.loot_table_keys")
        .push("loot_table_keys");
      this.lootTableKeysPrettify = builder
        .comment("Whether or not loot table key names should be prettified")
        .translation("quaint.configuration.client.loot_table_keys.prettify")
        .define("prettify", true);
      this.lootTableKeysIncludeNamespace = builder
        .comment("Whether or not loot table keys should include their namespace")
        .translation("quaint.configuration.client.loot_table_keys.include_namespace")
        .define("include_namespace", true);
      builder.pop();
    }

    private static Client getInstance() {
      return Client.container.instance;
    }

    public static boolean getLootTableKeysPrettify() {
      return Client.getInstance().lootTableKeysPrettify.get();
    }

    public static boolean getLootTableKeysIncludeNamespace() {
      return Client.getInstance().lootTableKeysIncludeNamespace.get();
    }

    public static String displayLootTableKey(ResourceLocation lootTableKey) {
      String name = Client.getLootTableKeysIncludeNamespace()
        ? lootTableKey.toString() : lootTableKey.getPath();

      return Client.getLootTableKeysPrettify()
        ? Utils.prettifyResource(name) : name;
    }
  }

  public static class Server {
    static final Container<Server> container = new Container<>(Server::new, ModConfig.Type.SERVER);

    private final ModConfigSpec.IntValue camouflagedDuration;
    private final ModConfigSpec.IntValue staffCooldown;
    private final ModConfigSpec.IntValue staffWeatherDurationMin;
    private final ModConfigSpec.IntValue staffWeatherDurationMax;

    private Server(ModConfigSpec.Builder builder) {
      this.camouflagedDuration = builder
        .comment("The duration (in seconds) of the camouflaged effect to be given to players when respawning, as long as the doGiveCamouflagedOnRespawn game rule is enabled")
        .translation("quaint.configuration.server.camouflaged_duration")
        .defineInRange("camouflaged_duration", 300, 0, Integer.MAX_VALUE);
      builder
        .comment("Options relating to the weather staves")
        .translation("quaint.configuration.server.staff_weather")
        .push("staff_weather");
      this.staffCooldown = builder
        .comment("The duration (in seconds) of the usage cooldown for weather staves")
        .translation("quaint.configuration.server.staff_weather.cooldown")
        .defineInRange("cooldown", 300, 0, Integer.MAX_VALUE);
      this.staffWeatherDurationMin = builder
        .comment("The minimum duration (in seconds) that weather from staves should last")
        .translation("quaint.configuration.server.staff_weather.duration_min")
        .defineInRange("duration_min", 300, 0, Integer.MAX_VALUE);
      this.staffWeatherDurationMax = builder
        .comment("The maximum duration (in seconds) that weather from staves should last")
        .translation("quaint.configuration.server.staff_weather.duration_max")
        .defineInRange("duration_max", 600, 0, Integer.MAX_VALUE);
      builder.pop();
    }

    private static Server getInstance() {
      return Server.container.instance;
    }

    public static int getCamouflagedDuration() {
      return Server.getInstance().camouflagedDuration.get() * 20;
    }

    public static int getStaffCooldown() {
      return Server.getInstance().staffCooldown.get() * 20;
    }

    public static IntegerRange getStaffWeatherDuration() {
      return IntegerRange.of(
        Server.getInstance().staffWeatherDurationMin.get() * 20,
        Server.getInstance().staffWeatherDurationMax.get() * 20
      );
    }

    public static int getStaffWeatherDurationRandom(RandomSource randomSource) {
      return Utils.sampleRange(Server.getStaffWeatherDuration(), randomSource);
    }
  }
}
