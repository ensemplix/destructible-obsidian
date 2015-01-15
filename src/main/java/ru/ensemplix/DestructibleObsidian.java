package ru.ensemplix;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class DestructibleObsidian extends JavaPlugin implements Listener {

    /**
     * Радиус в которм мы ищем блоки обсидана.
     */
    private static final int EXPLOSION_RADIUS = 3;

    /**
     * Сколько нужно взрывов обсидана что бы он сломался.
     */
    private static final int OBSIDIAN_DURABILITY = 10;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Location location = event.getLocation();
        World world = location.getWorld();

        for (int x = -EXPLOSION_RADIUS; x <= EXPLOSION_RADIUS; x++) {
            for (int y = -EXPLOSION_RADIUS; y <= EXPLOSION_RADIUS; y++) {
                for (int z = -EXPLOSION_RADIUS; z <= EXPLOSION_RADIUS; z++) {
                    Block block = world.getBlockAt(location.clone().add(x, y, z));

                    if (block.getType() == Material.OBSIDIAN) {
                        if (!block.hasMetadata("hits")) {
                            block.setMetadata("hits", new FixedMetadataValue(this, 1));
                        }

                        int hits = block.getMetadata("hits").get(0).asInt();

                        if (hits >= OBSIDIAN_DURABILITY) {
                            block.breakNaturally();
                            block.removeMetadata("hits", this);
                            block.getWorld().playSound(block.getLocation(), Sound.EXPLODE, 1, 1);
                        } else {
                            block.setMetadata("hits", new FixedMetadataValue(this, hits + 1));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if(block.getType() == Material.OBSIDIAN) {
            if(block.hasMetadata("hits")) {
                block.removeMetadata("hits", this);
            }
        }
    }

}
