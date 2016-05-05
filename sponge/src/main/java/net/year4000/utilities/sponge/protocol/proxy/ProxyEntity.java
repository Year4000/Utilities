package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Proxied;
import org.spongepowered.api.entity.Entity;

@Proxied("net.minecraft.entity.Entity")
public interface ProxyEntity {
    static ProxyEntity of(Entity entity) {
        Conditions.nonNull(entity, "entity");
        return Gateways.proxy(ProxyEntity.class, entity);
    }

    /** Get the object that this proxy is using */
    Object $this();

    @Getter(signature = "I")
    int entityId();
}
