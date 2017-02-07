/*
 * Copyright 2017 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import com.google.common.collect.Lists;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.sponge.hologram.Hologram;
import net.year4000.utilities.sponge.hologram.Holograms;
import net.year4000.utilities.sponge.protocol.Packets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * This plugin is used to test various things with Utilities.
 * The class will never be added to the production jar and must be
 * add to the classpath manually to access the testing tools.
 *
 * Hint: To enable this look in build.gradle for sponge
 */
@Plugin(id = "utilities-debug", name = "UtilitiesDebug", dependencies = {@Dependency(id ="utilities")})
public class UtilitiesDebug {
    private static final List<Hologram> activeHolograms = Lists.newArrayList();

    @Listener
    public void onInit(GameInitializationEvent event) {
        Packets packets = Sponge.getServiceManager().provide(Packets.class).orElseThrow(RuntimeException::new);
        Holograms holograms = Sponge.getServiceManager().provide(Holograms.class).orElseThrow(RuntimeException::new);

        // Gif Hologram
        cmd("gif", (src, args) -> {
            if (src instanceof Player) {
                Player player = (Player) src;
                InputStream gif = UtilitiesDebug.class.getResourceAsStream("/animation.gif");
                Hologram hologram = holograms.add(player.getLocation(), ImageIO.createImageInputStream(gif));
                activeHolograms.add(hologram);
            } else {
                src.sendMessage(Text.of("Must be in a context of a player"));
            }
        });
        // Png Hologram
        cmd("png", (src, args) -> {
            if (src instanceof Player) {
                Player player = (Player) src;
                InputStream gif = UtilitiesDebug.class.getResourceAsStream("/static.png");
                Hologram hologram = holograms.add(player.getLocation(), ImageIO.createImageInputStream(gif));
                activeHolograms.add(hologram);
            } else {
                src.sendMessage(Text.of("Must be in a context of a player"));
            }
        });
        // Destroy all holograms
        cmd("destroy", (src, args) -> activeHolograms.forEach(holograms::remove));
    }

    /** Interface for test command */
    private interface CommandConsumer {
        void cmd(CommandSource src, CommandContext args) throws Throwable;
    }

    /** Internal testing method */
    private void cmd(String cmd, final CommandConsumer consumer) {
        Sponge.getCommandManager().register(Utilities.get(), CommandSpec.builder().executor((src, args) -> {
            try {
                consumer.cmd(src, args);
                return CommandResult.success();
            } catch (Throwable throwable) {
                ErrorReporter.builder(throwable)
                    .add("Command: ", cmd)
                    .add("Source: ", src)
                    .add("Context: ", args)
                    .buildAndReport(System.err);
                return CommandResult.empty();
            }
        }).build(), cmd);
    }
}
