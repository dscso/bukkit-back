package back;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("back")){
			Player player = (Player) sender;
			if (getConfig().getBoolean("enable-permissions") && !player.hasPermission("back.goback")) {
				player.sendMessage(getConfig().getString("no-permissions"));
				return true;
			}
			if ((getConfig().isSet("users." + player.getName() + ".death.world")) && (getConfig().isSet("users." + player.getName() + ".death.x")) && (getConfig().isSet("users." + player.getName() + ".death.y")) && (getConfig().isSet("users." + player.getName() + ".death.z"))) {
	            Location loc = new Location(getServer().getWorld(getConfig().getString("users." + player.getName() + ".death.world")), getConfig().getDouble("users." + player.getName() + ".death.x"), getConfig().getDouble("users." + player.getName() + ".death.y"), getConfig().getDouble("users." + player.getName() + ".death.z"));
	            player.teleport(loc);
	            player.sendMessage(getConfig().getString("backmessage"));
			    return true;
		    } else {
		    	player.sendMessage(getConfig().getString("error"));
		    	return true;
		    }
		}
		return false;
	}
	@EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        Location loc = player.getLocation();
        getConfig().set("users." + player.getName() + ".death.world", loc.getWorld().getName());
        getConfig().set("users." + player.getName() + ".death.x", Double.valueOf(loc.getX()));
        getConfig().set("users." + player.getName() + ".death.y", Double.valueOf(loc.getY()));
        getConfig().set("users." + player.getName() + ".death.z", Double.valueOf(loc.getZ()));
        saveConfig();
    }
}
