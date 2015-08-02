package me.kippy.tnt;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Core extends JavaPlugin implements Listener {
	private Logger logger = getLogger();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR) {
			if(p.getItemInHand().getType() == Material.TNT) {
				TNTPrimed prime = p.getWorld().spawn(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ()), TNTPrimed.class);
				prime.setVelocity(p.getLocation().getDirection().multiply(getConfig().getDouble("Velocity")));
				prime.setFuseTicks(48);
				if(!(p.getGameMode() == GameMode.CREATIVE)) {
					if(p.getItemInHand().getAmount() == 1) {
						p.setItemInHand(new ItemStack(Material.AIR));
					}else{
						p.getInventory().getItemInHand().setAmount(p.getInventory().getItemInHand().getAmount() - 1);
					}
				}
			}
		}
	}
	@EventHandler
	public void onExplosion(EntityExplodeEvent e) {
		if(e.getEntityType() == EntityType.PRIMED_TNT) {
			TNTPrimed tnt = (TNTPrimed) e.getEntity();
			e.setCancelled(true);
			tnt.getWorld().createExplosion(tnt.getLocation(), 4.0F);
		}
	}
	
	@Override
	public void onDisable() {
		this.logger.info("TNT Throw has been disabled!");
	}
	
	@Override
	public void onEnable() {
		this.logger.info("TNT Throw has been enabled!");
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		saveDefaultConfig();
		reloadConfig();
	}

}
