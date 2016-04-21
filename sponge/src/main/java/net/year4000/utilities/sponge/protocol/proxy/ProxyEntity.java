package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.Getter;
import net.year4000.utilities.reflection.Proxied;
import org.spongepowered.api.entity.Entity;

@Proxied("net.minecraft.entity.Entity")
public interface ProxyEntity {
    static ProxyEntity of(Entity entity) {
        return Gateways.proxy(ProxyEntity.class, entity);
    }

    @Getter("field_145783_c")
    int entityId();
}
