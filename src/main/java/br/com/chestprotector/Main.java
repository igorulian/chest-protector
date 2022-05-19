package br.com.chestprotector;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new Database().CreateFile();
        registerEvents();
        Bukkit.getConsoleSender().sendMessage("Pl habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new ChestProtection(), this);

        getCommand("liberar").setExecutor(new GrantAccessCommnad());
        getCommand("remover").setExecutor(new RemoveAccessCommand());
    }

}
