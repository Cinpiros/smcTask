package fr.cinpiros.commands;

import fr.cinpiros.utils.DelayedTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase extends BukkitCommand implements CommandExecutor {
    private List<String> delayedPlayers = null;
    private int delay = 0;
    private final int minArguments;
    private final int maxArguments;
    private final boolean playerOnly;


    public CommandBase(String command) {
        this(command, 0);
    }

    public CommandBase(String command, boolean playerOnly) {
        this(command, 0, playerOnly);
    }

    public CommandBase(String command, int requiredArguments) {
        this(command, requiredArguments, requiredArguments);
    }

    public CommandBase(String command, int minArguments, int maxArguments) {
        this(command, minArguments, maxArguments, false);
    }

    public CommandBase(String command, int requiredArguments, boolean playerOnly) {
        this(command, requiredArguments, requiredArguments, playerOnly);
    }

    public CommandBase(String command, int minArguments, int maxArguments, boolean playerOnly) {
        super(command);
        this.minArguments = minArguments;
        this.maxArguments = maxArguments;
        this.playerOnly = playerOnly;

        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register(command, this);
        }
    }

    public CommandMap getCommandMap() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);

                return (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public CommandBase enableDelayStart(int delay) {
        this.delay = delay;
        this.delayedPlayers = new ArrayList<>();
        return this;
    }

    public void removeDelayFinish(Player player) {
        this.delayedPlayers.remove(player.getName());
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(getUsage());
    }

    public boolean execute(CommandSender sender, String alias, String [] args) {
        if (args.length < minArguments || (args.length > maxArguments && maxArguments != -1)) {
            sendUsage(sender);
            return true;
        }

        if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("Seulement un joueur peux utiliser cette commande");
            return true;
        }

        String permission = getPermission();
        if (permission != null && !(sender.hasPermission(permission))) {
            sender.sendMessage("&cVous n'avez pas la permission pour exécuter cette commande.");
            return true;
        }

        if (delayedPlayers != null && sender instanceof Player) {
            Player player = (Player) sender;

            if (delayedPlayers.contains(player.getName())) {
                player.sendMessage("&cMerci d'attendre avans d'utiliser cette commande à nouveau.");
                return true;
            }

            delayedPlayers.add(player.getName());
            DelayedTask task = new DelayedTask(() -> {
                delayedPlayers.remove(player.getName());
            }, 20L * delay);
        }

        if (!onCommand(sender, args)) {
            sendUsage(sender);
        }

        return true;

    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String [] args) {
        this.onCommand(sender, args);
        return true;
    }

    public abstract boolean onCommand(CommandSender sender, String [] args);

    public abstract String getUsage();

}
