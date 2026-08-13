package com.example.pillsforillness;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandPfi extends CommandBase {

    @Override
    public String getName() {
        return "pfi";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.pfi.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentTranslation("commands.pfi.players_only"));
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;

        IPlayerTimer timer = player.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
        if (timer == null) {
            sender.sendMessage(new TextComponentTranslation("commands.pfi.error_no_data"));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(new TextComponentTranslation("commands.pfi.usage"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length < 2) {
                    sender.sendMessage(new TextComponentTranslation("commands.pfi.add.usage"));
                    return;
                }
                try {
                    long addTicks = Long.parseLong(args[1]);
                    long newTimer = Math.max(0, timer.getTimer() + addTicks);
                    timer.setTimer(newTimer);
                    long sec = newTimer / 20;
                    sender.sendMessage(new TextComponentTranslation("commands.pfi.add.success", addTicks, newTimer, (sec / 60), (sec % 60)));
                } catch (NumberFormatException e) {
                    sender.sendMessage(new TextComponentTranslation("commands.pfi.invalid_number", args[1]));
                }
                break;

            case "current":
                long ticks = timer.getTimer();
                long seconds = ticks / 20;
                long minutes = seconds / 60;
                sender.sendMessage(new TextComponentTranslation("commands.pfi.current.success", ticks, minutes, (seconds % 60)));
                break;

            case "reset":
                timer.setTimer(0);
                timer.setLastPillTime(0);
                sender.sendMessage(new TextComponentTranslation("commands.pfi.reset.success"));
                break;

            case "info":
                for (int i = 1; i <= 6; i++) {
                    sender.sendMessage(new TextComponentTranslation("commands.pfi.info.stage" + i));
                }
                sender.sendMessage(new TextComponentTranslation("commands.pfi.info.death"));
                sender.sendMessage(new TextComponentTranslation("commands.pfi.info.overdose_header"));
                for (int i = 1; i <= 4; i++) {
                    sender.sendMessage(new TextComponentTranslation("commands.pfi.info.overdose" + i));
                }
                break;

            default:
                sender.sendMessage(new TextComponentTranslation("commands.pfi.unknown_subcommand"));
                break;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "add", "current", "reset", "info");
        }
        return Collections.emptyList();
    }
}
