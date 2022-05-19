package br.com.chestprotector;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveAccessCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 1) {
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

                if(!Database.AmITheChestOwner(player, x,y,z)){
                    player.sendMessage("§cVocê precisa ser o dono do baú para conceder permissões");
                    return false;
                }

                Player liberarPlayer = Bukkit.getPlayer(args[0]);

                if(liberarPlayer == null){
                    player.sendMessage("§cJogador inválido!");
                    return false;
                }

                if(Database.CanOpenChest(player,x,y,z)){
                    player.sendMessage("§eJogador ja possui permissão do baú");
                    return false;
                }

                Database.GrantAccessToChest(player,x,y,z);

            }else{
                player.sendMessage("/remover {player}");
            }

        }
        return false;
    }
}
