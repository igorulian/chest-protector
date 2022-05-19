package br.com.chestprotector.commands;

import br.com.chestprotector.Database;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListOfAccess implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0) {
                Block block = player.getTargetBlock(6);

                if(!block.getType().equals(Material.CHEST)){
                    player.sendMessage("§eDigite esse comando olhando para um baú");
                    return false;
                }

                double x = block.getX();
                double y = block.getY();
                double z = block.getZ();

                if(!Database.IsChestProtected(x,y,z)){
                    player.sendMessage("§cEste baú ainda não possui proteção");
                    return false;
                }

                Database.ListAccessPlayers(player,x,y,z);

            }else{
                player.sendMessage("/listar");
            }

        }
        return false;
    }
}
