package dev.xhyrom.timecontrol;

import net.fabricmc.api.ClientModInitializer;
import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientInitilizer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(TimeControlMod.CHANNEL_TIME_STATUS, (client, handler, buf, responseSender) -> {
            double timeRate = buf.readDouble();
            int timeStopperId = buf.readInt();
            client.execute(() -> ((ClientWorldAccessor) client.world).updateTimeStatus(timeRate, timeStopperId));
        });
    }
}
