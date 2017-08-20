package pl.kacperduras.tasket.bukkit;

import org.apache.commons.lang3.Validate;
import org.bukkit.plugin.Plugin;
import pl.kacperduras.tasket.Tasket;

public final class BukkitTasket extends Tasket {

    private final Plugin plugin;

    public BukkitTasket(Plugin plugin) {
        Validate.notNull(plugin);

        this.plugin = plugin;
    }

}
