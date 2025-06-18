package de.janschuri.lunaticdrops.config;

import de.janschuri.lunaticdrops.drops.Drop;
import de.janschuri.lunaticdrops.drops.DropBlockBreak;
import de.janschuri.lunaticdrops.loot.Loot;
import de.janschuri.lunaticdrops.utils.Logger;
import org.bukkit.Material;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DropConfigBlockBreak extends DropConfig {

    public DropConfigBlockBreak(Path path) {
        super(path);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public DropBlockBreak getDrop() {
        try {
            Drop drop = super.getDrop();
            Material block = getMaterial("block");

            return new DropBlockBreak(
                    drop,
                    block
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Material getMaterial(String key) {
        String material = getString(key);
        try {
            return Material.valueOf(material);
        } catch (IllegalArgumentException e) {
            Logger.errorLog("Invalid material: " + material);
            return null;
        }
    }
}
