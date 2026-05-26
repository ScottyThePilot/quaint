package scottythepilot.quaint;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import scottythepilot.quaint.entities.QuaintEntities;

@Mod(value = QuaintMod.MOD_ID, dist = Dist.CLIENT)
public class QuaintModClient {
  public QuaintModClient(ModContainer container) {
    container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }

  @EventBusSubscriber(modid = QuaintMod.MOD_ID, value = Dist.CLIENT)
  public static final class Events {
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
      QuaintEntities.registerEntityRenderers(event);
    }
  }
}
