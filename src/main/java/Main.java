import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static Client client;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        try {
            client = new Client(new URI("ws://" + getConfig().getString("api-url")));
            client.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("csocket")) {
            if (sender.hasPermission("csocket.admin")) {
                if (args.length != 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /csocket status|connect");
                    return true;
                }

                if (args[0].equalsIgnoreCase("status")) {
                    if (client.isOpen()) {
                        sender.sendMessage(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "Connected");
                    } else {
                        sender.sendMessage(ChatColor.GRAY + "Status: " + ChatColor.RED + "Disconnected");
                    }
                } else if (args[0].equalsIgnoreCase("connect")) {
                    if (!client.isOpen()) {
                        try {
                            client = new Client(new URI("ws://" + getConfig().getString("api-url")));
                            client.connect();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        sender.sendMessage(ChatColor.GREEN + "Connected to the websocket server!");
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "Already connected to websocket server!");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /csocket status|connect");
                    return true;
                }
            }
        }
        return true;
    }

    public static Main getInstance() {
        return plugin;
    }
}
