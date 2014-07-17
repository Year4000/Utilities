package net.year4000.ducktape.bungee.module;

import net.md_5.bungee.api.plugin.Listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleListeners {
    public Class<? extends Listener>[] value();
}
