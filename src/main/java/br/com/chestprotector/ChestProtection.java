package br.com.chestprotector;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChestProtection implements Listener {

    public void RemoveDiamondFromPlayer(Player player, int ammount){
        ItemStack diamonds = player.getInventory().getItemInMainHand();
        int diamondsAmmount = diamonds.getAmount();

        if(diamondsAmmount > 1){
            player.getInventory().setItemInMainHand(new ItemStack(Material.DIAMOND, diamondsAmmount-ammount));
        }else{
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onClickChest(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            Material material = block.getType();

            if(material.equals(Material.CHEST)){
                double x = block.getX();
                double y = block.getY();
                double z = block.getZ();

                if(!new Database().CanOpenChest(player, x, y, z)){
                    event.setCancelled(true);
                    player.sendMessage("§cVocê não possui acesso à esse baú");
                    return;
                }

                if(player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND)){

                    RemoveDiamondFromPlayer(player, 1);

                    new Database().ProtectChest(player,x, y, z);
                }
            }
        }
    }
}
