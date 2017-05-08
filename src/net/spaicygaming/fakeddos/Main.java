package net.spaicygaming.fakeddos;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;


public class Main extends JavaPlugin implements Listener {

	String ver = "1.0";
	
	public void onEnable(){
		Server server = getServer();
		ConsoleCommandSender console = server.getConsoleSender();
		
	    console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	    console.sendMessage(ChatColor.AQUA + "           Fake" + ChatColor.RED + "DDoS");
	    console.sendMessage(ChatColor.AQUA + "          Version " + this.ver);
	 //console.sendMessage(ChatColor.GOLD + " Project:" + ChatColor.RED + ChatColor.ITALIC + "http://bit.ly/");
	    console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	    
	    Bukkit.getServer().getPluginManager().registerEvents(this, this);
	    saveDefaultConfig();
	    if (!getConfig().getString("ConfigVersion").equals("1.0")) {
	        console.sendMessage("[FakeDDoS] " + ChatColor.RED + "OUTDATED CONFIG FILE DETECTED, PLEASE DELETE THE OLD ONE!");
	    }
	}	
	
	public void onDisable(){
		getLogger().info("FakeDDoS has been disabled");
	}
	

	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args){
		
		if (alias.equalsIgnoreCase("ddos")) {
			
			if (sender instanceof Player) {
				
				if (sender.hasPermission("fakeddos.use")) {
			
					if (args.length == 0) {
						sender.sendMessage(ChatColor.DARK_GRAY + " [" + ChatColor.AQUA + "Fake" + ChatColor.RED + "DDoS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + getConfig().getString("NoArgsMessage"));	
					}		
					else {
							Player target = Bukkit.getServer().getPlayer(args[0]);
							if (target == null) {
								sender.sendMessage(ChatColor.DARK_GRAY + " [" + ChatColor.AQUA + "Fake" + ChatColor.RED + "DDoS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + getConfig().getString("TargetOffline").replaceAll("%TARGET", args[0]));
							}
					
							else {							
								if (target == sender) {
									sender.sendMessage(ChatColor.DARK_GRAY + " [" + ChatColor.AQUA + "Fake" + ChatColor.RED + "DDoS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + getConfig().getString("AutoDDoSMessage"));
								}							
								else{
									
									sender.sendMessage(ChatColor.DARK_GRAY + " [" + ChatColor.AQUA + "Fake" + ChatColor.RED + "DDoS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + getConfig().getString("SentDDoSMessage").replaceAll("%TARGET", target.getName().toString())); 
									
									CraftPlayer craftplayer = (CraftPlayer) target;
									PlayerConnection connection = craftplayer.getHandle().playerConnection;
																		
									IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.DARK_RED + ChatColor.BOLD + getConfig().getString("UnderAttacTitle") + "\"}");
									IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\":\"" + getConfig().getString("UnderAttacSubtitle").replaceAll("%SENDER", sender.getName().toString()) + "\"}");
									PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
									PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
									connection.sendPacket(title);
									connection.sendPacket(subtitle);									
									
									if (getConfig().getBoolean("PlaySounds-Target")) {										
										target.getWorld().playSound(target.getLocation(), Sound.GHAST_SCREAM, 1, 1);										
									}
									
									if (getConfig().getBoolean("PlaySounds-Sender")) {
										((CraftPlayer) sender).getWorld().playSound(((CraftPlayer) sender).getLocation(), Sound.BLAZE_HIT, 1, 1);	
									}
								}
							}
					}			
				
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + " [" + ChatColor.AQUA + "Fake" + ChatColor.RED + "DDoS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + getConfig().getString("NoPermissionMessage"));
				}
				
			} else {
				sender.sendMessage(ChatColor.DARK_GRAY + " [" + ChatColor.AQUA + "Fake" + ChatColor.RED + "DDoS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + getConfig().getString("OnlyPlayerCmd"));
			}
		}		
		return false;
	}
	
}
