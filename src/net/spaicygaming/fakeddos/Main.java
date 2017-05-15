package net.spaicygaming.fakeddos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;


public class Main extends JavaPlugin implements Listener {

	String ver = "1.1";
	String projectlink= "http://bit.ly/FakeDDoS";
	String prefix = ChatColor.DARK_GRAY + " [" + ChatColor.AQUA + "Fake" + ChatColor.RED + "DDoS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;
	
	public void onEnable(){
		Server server = getServer();
		ConsoleCommandSender console = server.getConsoleSender();
		
	    console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	    console.sendMessage(ChatColor.AQUA + "           Fake" + ChatColor.RED + "DDoS");
	    console.sendMessage(ChatColor.AQUA + "          Version " + this.ver);
	    console.sendMessage(ChatColor.GOLD + " Project:" + ChatColor.RED + ChatColor.ITALIC + projectlink);
	    console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	    
	    Bukkit.getServer().getPluginManager().registerEvents(this, this);
	    saveDefaultConfig();
	    if (!getConfig().getString("ConfigVersion").equals("1.1")) {
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
						sender.sendMessage(prefix + getConfig().getString("NoArgsMessage"));	
					}		
					else {
							Player target = Bukkit.getServer().getPlayer(args[0]);
							if (target == null) {
								sender.sendMessage(prefix + getConfig().getString("TargetOffline").replaceAll("%TARGET", args[0]));
							}
					
							else {							
								if (target == sender) {
									sender.sendMessage(prefix + getConfig().getString("AutoDDoSMessage"));
								}							
								else{
									
									sender.sendMessage(prefix + getConfig().getString("SentDDoSMessage").replaceAll("%TARGET", target.getName().toString())); 
									
									CraftPlayer craftplayer = (CraftPlayer) target;
									PlayerConnection connection = craftplayer.getHandle().playerConnection;
																		
									IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.DARK_RED + ChatColor.BOLD + getConfig().getString("UnderAttackTitle") + "\"}");
									IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\":\"" + getConfig().getString("UnderAttackSubtitle").replaceAll("%SENDER", sender.getName().toString()) + "\"}");
									PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
									PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
									connection.sendPacket(title);
									connection.sendPacket(subtitle);									
									
									//target
									if (getConfig().getBoolean("Effects.Target.Sounds")) {										
										target.getWorld().playSound(target.getLocation(), Sound.GHAST_SCREAM, 1, 1);										
									}
									if (getConfig().getBoolean("Effects.Target.Particles")) {
										target.getWorld().playEffect(target.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
									}
									
									//sender
									if (getConfig().getBoolean("Effects.Sender.Sounds")) {
										((Entity) sender).getWorld().playSound(((Entity) sender).getLocation(), Sound.BLAZE_HIT, 1, 1);	
									}
									if (getConfig().getBoolean("Effects.Sender.Particles")) {
										((Entity) sender).getWorld().playEffect(((Entity) sender).getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
									}
								}
							}
					}			
				
				} else {
					sender.sendMessage(prefix + getConfig().getString("NoDDoSPermissions"));
				}
				
			} else {
				sender.sendMessage(prefix + getConfig().getString("OnlyPlayerCmd"));
			}
		}		
		
		if (alias.equalsIgnoreCase("fakeddos")){
			if (args.length == 0){
				sender.sendMessage("");
				sender.sendMessage(ChatColor.RED + "     --=-=" + ChatColor.GOLD  + " FakeDDoS " + ChatColor.GRAY + "v" + ver + ChatColor.RED + " =-=--");
				sender.sendMessage(ChatColor.AQUA + "   /ddos <player> " + ChatColor.GREEN + "->" + ChatColor.GRAY + " FakeDDoS a player.");
				sender.sendMessage(ChatColor.AQUA + "   /fakeddos info " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows Info.");
				sender.sendMessage(ChatColor.AQUA + "   /fakeddos reload " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Reloads the Config.");
				sender.sendMessage(ChatColor.RED + "          --=-=-=-=-=-=--");
				sender.sendMessage("");
			}
			if (args.length == 1){
				if (args[0].equalsIgnoreCase("reload")){
					if (sender.hasPermission("fakeddos.reload")){
						if (sender instanceof Player){
							reloadConfig();
							sender.sendMessage(prefix + "§cConfig Reloaded.");
							getLogger().info("Config Reloaded.");
						} else {
							getLogger().info("Config Reloaded.");
							reloadConfig();
						}
					
					} else {
						sender.sendMessage(prefix + getConfig().getString("NoReloadPermissions"));
					}
				}
				else if (args[0].equalsIgnoreCase("info")){
					sender.sendMessage(ChatColor.DARK_GREEN + "    --=-=-=-=-=-=-=-=-=--");
					sender.sendMessage(ChatColor.AQUA + "         Fake" + ChatColor.RED + "DDoS " + ChatColor.GRAY + "v" + ver);
					sender.sendMessage(ChatColor.GOLD + "    Project: " + ChatColor.DARK_RED + ChatColor.ITALIC + projectlink);
					sender.sendMessage(ChatColor.DARK_GREEN + "      --=-=-=-=-=-=-=--");
					sender.sendMessage("");					
				}
				else {
					if (sender instanceof Player){
						sender.sendMessage(prefix + getConfig().getString("WrongCommand"));
					}else{
						getLogger().info(getConfig().getString("WrongCommand"));
					}
				}
			}
			if (args.length > 1){
				if (sender instanceof Player){
					sender.sendMessage(prefix + getConfig().getString("TooManyArgs"));
				}else{
					getLogger().info(getConfig().getString("TooManyArgs"));
				}
			}
		}
	return false;
	}	
}
