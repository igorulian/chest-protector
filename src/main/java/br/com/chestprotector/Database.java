package br.com.chestprotector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Database {
    static Plugin plugin = Main.getPlugin(Main.class);

    public static void CreateFile(){
        File data = new File(plugin.getDataFolder(), File.separator + "/");
        File file = new File(data, File.separator + "chests.yml");
        FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                chestData.save(file);
            } catch (IOException exception) {
                Bukkit.getConsoleSender().sendMessage("Ocorreu um erro, ao criar arquivo db :|");
            }
        }

    }


    public static boolean IsChestProtected(double x, double y, double z){
        File data = new File(plugin.getDataFolder(), File.separator + "/");
        File file = new File(data, File.separator + "chests.yml");
        FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

        Object[] fields = chestData.getConfigurationSection("").getKeys(false).toArray();

        boolean isProtected = false;

        for (Object key : fields){
            double dx = chestData.getDouble("" + key + ".x");
            double dy = chestData.getDouble("" + key + ".y");
            double dz = chestData.getDouble("" + key + ".z");
            String owner = chestData.getString("" + key + ".owner");
            if((dx == x) && (dy == y) && (dz == z)){
                isProtected = true;
            }
        }
        return isProtected;
    }

    public static boolean CanOpenChest(Player player, double x, double y, double z){
        if(IsChestProtected(x,y,z)){
            File data = new File(plugin.getDataFolder(), File.separator + "/");
            File file = new File(data, File.separator + "chests.yml");
            FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

            Object[] fields = chestData.getConfigurationSection("").getKeys(false).toArray();

            String playerID =  player.getPlayer().getUniqueId().toString();

            boolean isTheOwner = false;
            boolean hasAccess = false;

            for (Object key : fields){
                double dx = chestData.getDouble("" + key + ".x");
                double dy = chestData.getDouble("" + key + ".y");
                double dz = chestData.getDouble("" + key + ".z");
                String ownerID = chestData.getString("" + key + ".owner");
                ArrayList<String> access = (ArrayList<String>) chestData.getList("" + key + ".access");

                if((dx == x) && (dy == y) && (dz == z)){
                    isTheOwner = playerID.equals(ownerID);

                    for (String accessId: access) {
                        if(accessId.equals(playerID)){
                            hasAccess = true;
                        }
                    }

                }
            }

            return isTheOwner || hasAccess;
        }else{
            return true;
        }
    }

    public static boolean AmITheChestOwner(Player player, double x, double y, double z){
        if(IsChestProtected(x,y,z)){
            File data = new File(plugin.getDataFolder(), File.separator + "/");
            File file = new File(data, File.separator + "chests.yml");
            FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

            Object[] fields = chestData.getConfigurationSection("").getKeys(false).toArray();

            String playerID =  player.getPlayer().getUniqueId().toString();

            boolean isTheOwner = false;

            for (Object key : fields){
                double dx = chestData.getDouble("" + key + ".x");
                double dy = chestData.getDouble("" + key + ".y");
                double dz = chestData.getDouble("" + key + ".z");
                String ownerID = chestData.getString("" + key + ".owner");

                if((dx == x) && (dy == y) && (dz == z)){
                    isTheOwner = playerID.equals(ownerID);
                }
            }

            return isTheOwner;
        }else{
            return false;
        }
    }



    public static void ProtectChest(Player player, double x, double y, double z){
        File data = new File(plugin.getDataFolder(), File.separator + "/");
        File file = new File(data, File.separator + "chests.yml");
        FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

        try {
            String key = "" + x + "" + y + "" + z;
            key = key.replace(".", "-");
            chestData.set(key + ".x", x);
            chestData.set(key + ".y", y);
            chestData.set(key + ".z", z);
            chestData.set(key + ".owner", player.getPlayer().getUniqueId().toString());
            ArrayList<String> access = new ArrayList<String>();
            chestData.set(key + ".access", access);
            chestData.save(file);
            player.sendMessage("§aBaú trancado com sucesso!");
            player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, new Location(player.getWorld(),x,y,z), 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GrantAccessToChest(Player player, double x, double y, double z){
        File data = new File(plugin.getDataFolder(), File.separator + "/");
        File file = new File(data, File.separator + "chests.yml");
        FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

        Object[] fields = chestData.getConfigurationSection("").getKeys(false).toArray();

        for (Object key : fields){
            double dx = chestData.getDouble("" + key + ".x");
            double dy = chestData.getDouble("" + key + ".y");
            double dz = chestData.getDouble("" + key + ".z");

            if((dx == x) && (dy == y) && (dz == z)) {
                ArrayList<String> access = (ArrayList<String>) chestData.getList("" + key + ".access");
                access.add(player.getPlayer().getUniqueId().toString());
                try{
                    chestData.set("" + key + ".access", access);
                    chestData.save(file);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void RemoveAccessToChest(Player player, double x, double y, double z){
        File data = new File(plugin.getDataFolder(), File.separator + "/");
        File file = new File(data, File.separator + "chests.yml");
        FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

        Object[] fields = chestData.getConfigurationSection("").getKeys(false).toArray();

        for (Object key : fields){
            double dx = chestData.getDouble("" + key + ".x");
            double dy = chestData.getDouble("" + key + ".y");
            double dz = chestData.getDouble("" + key + ".z");

            if((dx == x) && (dy == y) && (dz == z)) {
                ArrayList<String> access = (ArrayList<String>) chestData.getList("" + key + ".access");
                access.remove(player.getPlayer().getUniqueId().toString());
                try{
                    chestData.set("" + key + ".access", access);
                    chestData.save(file);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void ListAccessPlayers(Player player, double x, double y, double z){
        File data = new File(plugin.getDataFolder(), File.separator + "/");
        File file = new File(data, File.separator + "chests.yml");
        FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

        Object[] fields = chestData.getConfigurationSection("").getKeys(false).toArray();

        for (Object key : fields){
            double dx = chestData.getDouble("" + key + ".x");
            double dy = chestData.getDouble("" + key + ".y");
            double dz = chestData.getDouble("" + key + ".z");
            String owner = chestData.getString("" + key + ".owner");

            if((dx == x) && (dy == y) && (dz == z)) {
                player.sendMessage("§eJogadores com permissão neste baú: \n");
                player.sendMessage(" §6- " + getPlayerByUuid(owner).getName() + " (dono)");
                ArrayList<String> access = (ArrayList<String>) chestData.getList("" + key + ".access");
                for (String playerID : access) {
                    String playerName = getPlayerByUuid(playerID).getName();
                    player.sendMessage(" §e- " + playerName);
                }
            }
        }
    }
    public static Player getPlayerByUuid(String uuid) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(p.getUniqueId().toString().equals(uuid)) {
                return p;
            }
        }
        for(OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()){
            if(p.getUniqueId().toString().equals(uuid)) {
                return (Player) p;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void RemoveChest(double x, double y, double z){
        File data = new File(plugin.getDataFolder(), File.separator + "/");
        File file = new File(data, File.separator + "chests.yml");
        FileConfiguration chestData = YamlConfiguration.loadConfiguration(file);

        Object[] fields = chestData.getConfigurationSection("").getKeys(false).toArray();

        for (Object key : fields){
            double dx = chestData.getDouble("" + key + ".x");
            double dy = chestData.getDouble("" + key + ".y");
            double dz = chestData.getDouble("" + key + ".z");

            if((dx == x) && (dy == y) && (dz == z)) {
                try{
                    chestData.set("" + key, null);
                    chestData.save(file);
                }catch (IOException e){}
            }
        }
    }


}
