package fr.neatmonster.nocheatplus;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.checks.moving.MovingConfig;
import fr.neatmonster.nocheatplus.checks.moving.MovingData;
import fr.neatmonster.nocheatplus.players.DataManager;
import fr.neatmonster.nocheatplus.players.IPlayerData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.bukkit.Bukkit.getServer;


public class PlayerSpeed implements Listener {
    HashMap<Player, Location> last_location = new HashMap<Player, Location>();
    HashMap<Player, Long> last_check = new HashMap<Player, Long>();
    public static HashMap<Player, Double> speed = new HashMap<Player, Double>();
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();


        player.getScheduler().runAtFixedRate(getServer().getPluginManager().getPlugin("NoCheatPlus"), scheduledTask -> {
            if(last_location.containsKey(player) && player.getLocation().getWorld() == last_location.get(player).getWorld()){
                if(player.getLocation() != last_location.get(player)){
                    float delta_time = (float) (System.currentTimeMillis() - last_check.get(player))/1000;
                    speed.put(player, (double) player.getLocation().distance(last_location.get(player))/delta_time);
                    // getServer().getLogger().info("Speed: " + speed.get(player));
                }
            }
            last_check.put(player, System.currentTimeMillis());
            last_location.put(player, player.getLocation());
        }, null, 1L, 10L);

    }
    @EventHandler
    public void onJoinSpeed_Exp(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.getScheduler().runAtFixedRate(getServer().getPluginManager().getPlugin("NoCheatPlus"), scheduledTask -> {
            // PlayerSpeed / Fly exploit
            //
            final IPlayerData pData = DataManager.getPlayerData(player);
            final MovingData data_mov = pData.getGenericInstance(MovingData.class);
            Material m = player.getLocation().getBlock().getType();
            if(m == Material.NETHER_PORTAL || m== Material.END_PORTAL) {
                data_mov.resetSetBack();
            }
        }, null, 1L, 1L);

    }
}
