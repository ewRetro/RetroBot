package com.retrogray.retrobot;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.command.commands.admin.*;
import com.retrogray.retrobot.command.commands.fun.*;
import com.retrogray.retrobot.command.commands.info.*;
import com.retrogray.retrobot.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new PasteCommand());
        addCommand(new HasteCommand());
        addCommand(new UptimeCommand());
        addCommand(new RepeatCommand());
        addCommand(new KickCommand());
        addCommand(new MemeCommand());
        addCommand(new JokeCommand());
        addCommand(new SetPrefixCommand());
        addCommand(new JoinCommand());
        addCommand(new KillCommand());
        addCommand(new EightBallCommand());
        addCommand(new YesNoCommand());
        addCommand(new SuggestCommand());
        addCommand(new SlotMachineCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new QueueCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new LeaveCommand());
        addCommand(new UserInfoCommand());
        addCommand(new HugCommand());
        addCommand(new MinecraftCommand());
        addCommand(new InfoCommand());
        addCommand(new DogCommand());
        addCommand(new CatCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new PlaylistCommand());
        addCommand(new ShuffleCommand());
        addCommand(new BirdCommand());
        addCommand(new SusCommand());
        addCommand(new SoundCommand());
        addCommand(new GuildInfoCommand());
        addCommand(new BanCommand());
        addCommand(new UnbanCommand());
        addCommand(new PurgeCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) throws IOException {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            //event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            try {
                cmd.handle(ctx);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


