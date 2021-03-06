package de.dytanic.cloudnet.ext.bridge.nukkit.event;

import cn.nukkit.event.HandlerList;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;

public final class NukkitCloudServiceConnectNetworkEvent extends NukkitCloudNetEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ServiceInfoSnapshot serviceInfoSnapshot;

    public NukkitCloudServiceConnectNetworkEvent(ServiceInfoSnapshot serviceInfoSnapshot) {
        this.serviceInfoSnapshot = serviceInfoSnapshot;
    }

    public static HandlerList getHandlers() {
        return NukkitCloudServiceConnectNetworkEvent.handlers;
    }

    public ServiceInfoSnapshot getServiceInfoSnapshot() {
        return this.serviceInfoSnapshot;
    }
}