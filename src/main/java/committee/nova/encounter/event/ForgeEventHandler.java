package committee.nova.encounter.event;

import committee.nova.encounter.reload.EncounterReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;


@EventBusSubscriber
public class ForgeEventHandler {
    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {
        event.addListener(new EncounterReloadListener());
    }
}
