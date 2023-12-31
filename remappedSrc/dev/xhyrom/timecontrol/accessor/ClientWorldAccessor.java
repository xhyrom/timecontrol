package dev.xhyrom.timecontrol.accessor;

public interface ClientWorldAccessor {
    void updateTimeStatus(double var1, int var3);

    double getTimeRate();

    int getTimeStopperId();
}

