package br.com.chestprotector;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.xml.crypto.Data;

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
        boolean isMainHand = (event.getHand() == EquipmentSlot.HAND);

        if((event.getAction() == Action.RIGHT_CLICK_BLOCK)){
            Block block = event.getClickedBlock();
            Material material = block.getType();

            if(material.equals(Material.CHEST)){
                double x = block.getX();
                double y = block.getY();
                double z = block.getZ();

                if(!Database.CanOpenChest(player, x, y, z)){
                    event.setCancelled(true);
                    player.sendMessage("§cVocê não possui acesso à esse baú");
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 10, 10);
                    return;
                }

                if(!(player.isSneaking() && isMainHand)) return;

                Material itemInHand = player.getInventory().getItemInMainHand().getType();

                if(itemInHand.equals(Material.DIAMOND)){
                    event.setCancelled(true);

                    if(Database.IsChestProtected(x,y,z)) {
                        player.sendMessage("§eEsse baú já está protegido");
                        return;
                    }

                    RemoveDiamondFromPlayer(player, 1);
                    Database.ProtectChest(player,x, y, z);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 10);

                }
            }
        }
    }

    @EventHandler
    public void onBreakChest(BlockBreakEvent event){
        Player player = event.getPlayer();

            Block block = event.getBlock();
            Material material = block.getType();

            if(material.equals(Material.CHEST)){
                double x = block.getX();
                double y = block.getY();
                double z = block.getZ();

                if(!Database.CanOpenChest(player, x, y, z)){
                    event.setCancelled(true);
                    player.sendMessage("§cVocê não possui acesso à esse baú");
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 10, 10);
                    return;
                }

                Database.RemoveChest(x,y,z);
                player.getWorld().playSound(block.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 10, 10);

            }
    }
}
