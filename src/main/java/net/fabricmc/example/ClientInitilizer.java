package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.example.accessor.ClientWorldAccessor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientInitilizer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ExampleMod.CHANNEL_TIME_STATUS, (client, handler, buf, responseSender) -> {
            double timeRate = buf.readDouble();
            int timeStopperId = buf.readInt();
            client.execute(() -> ((ClientWorldAccessor) client.world).updateTimeStatus(timeRate, timeStopperId));
        });
    }
}
