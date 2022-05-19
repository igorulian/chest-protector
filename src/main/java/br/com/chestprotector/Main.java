package br.com.chestprotector;

import br.com.chestprotector.commands.GrantAccess;
import br.com.chestprotector.commands.ListOfAccess;
import br.com.chestprotector.commands.RemoveAccess;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new Database().CreateFile();
        registerEvents();
        Bukkit.getConsoleSender().sendMessage("§eChestProtector Enabled");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§eChestProtector Disabled");
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new ChestProtection(), this);

        getCommand("liberar").setExecutor(new GrantAccess());
        getCommand("remover").setExecutor(new RemoveAccess());
        getCommand("listar").setExecutor(new ListOfAccess());
    }

}
