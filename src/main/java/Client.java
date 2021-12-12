import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class Client extends WebSocketClient {

    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connection opened!");
        send("Hello World!");

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if (isClosed()) {
                    if (counter >= 2) {
                        cancel();
                        return;
                    }
                    counter++;
                    return;
                }

                send("ping");
            }
        }.runTaskTimer(Main.getInstance(),0, 15 * 20);
        Main.getInstance().getServer().broadcastMessage(ChatColor.GREEN + "Connected to WebSocket!");
    }

    @Override
    public void onMessage(String s) {
        if (s.startsWith("cmd:")) {
            new BukkitRunnable() {
                final String cmd = s.replace("cmd:", "");
                @Override
                public void run() {
                    Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(),
                            cmd);
                }
            }.runTask(Main.getInstance());
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
