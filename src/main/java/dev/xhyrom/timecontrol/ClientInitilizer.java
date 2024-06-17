package dev.xhyrom.timecontrol;

import net.fabricmc.api.ClientModInitializer;
import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientInitilizer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(TimeControlTimePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                double timeRate = payload.timeRate();
                int timeStopperId = payload.timeStopperId();
                ((ClientWorldAccessor) context.client().world).updateTimeStatus(timeRate, timeStopperId);
            });
        });
    }
}
