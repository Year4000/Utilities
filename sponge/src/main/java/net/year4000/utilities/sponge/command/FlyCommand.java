/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.command;

import static net.year4000.utilities.sponge.Messages.CMD_ERROR_PLAYER;
import static net.year4000.utilities.sponge.Messages.CMD_FLY_MODE;
import static net.year4000.utilities.sponge.Messages.CMD_FLY_OFF;
import static net.year4000.utilities.sponge.Messages.CMD_FLY_ON;
import static org.spongepowered.api.text.format.TextColors.AQUA;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.RED;

import com.google.inject.Injector;
import net.year4000.utilities.sponge.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.manipulator.mutable.entity.FlyingAbilityData;
import org.spongepowered.api.data.manipulator.mutable.entity.FlyingData;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

public final class FlyCommand implements CommandExecutor {
    private static final String[] ALIAS = new String[] {"fly"};
    private static final CommandSpec COMMAND_SPEC = CommandSpec.builder()
        .description(Text.of("I believe I can fly"))
        .permission("utilities.command.fly")
        .executor(new FlyCommand())
        .build();

    /** Register this command with the manager */
    public static void register(Object object, Injector injector) {
        injector.injectMembers(COMMAND_SPEC.getExecutor());
        Sponge.getCommandManager().register(object, COMMAND_SPEC, ALIAS);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            throw new CommandException(Text.of(Messages.ERROR, RED, CMD_ERROR_PLAYER.get(src)));
        }

        final Player player = (Player) src;

        // Defaults, player can always have flying data no need for checks
        Value<Boolean> ability = player.getOrCreate(FlyingAbilityData.class).get().canFly().set(true);
        Value<Boolean> value = player.getOrCreate(FlyingData.class).get().flying().set(true);
        Text subTitle = Text.of(GREEN, CMD_FLY_ON.get(src));

        // Disable flying
        if (player.get(FlyingAbilityData.class).get().canFly().get()) {
            subTitle = Text.of(RED, CMD_FLY_OFF.get(src));
            value.set(false);
            ability.set(false);
        }

        // Send out updates
        player.sendTitle(Title.of(Text.of(AQUA, CMD_FLY_MODE.get(src)), subTitle));
        player.offer(ability);
        player.offer(value);

        return CommandResult.builder().affectedEntities(1).successCount(1).build();
    }
}
