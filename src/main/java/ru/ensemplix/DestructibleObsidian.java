package ru.ensemplix;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class DestructibleObsidian extends JavaPlugin implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        for(Block block : event.blockList()) {
            if(block.getType() == Material.OBSIDIAN) {
                if (block.hasMetadata("hits")) {
                    block.setMetadata("hits", new FixedMetadataValue(this, 1));
                }

                int hits = block.getMetadata("hits").get(0).asInt();

                if(hits >= 8) {
                    block.setType(Material.AIR);
                    block.removeMetadata("hits", this);
                    block.getWorld().playSound(block.getLocation(), Sound.EXPLODE, 1, 1);
                } else {
                    block.setMetadata("hits", new FixedMetadataValue(this, hits + 1));
                }
            }
        }
    }

}
