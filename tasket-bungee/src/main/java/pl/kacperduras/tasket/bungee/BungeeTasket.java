package pl.kacperduras.tasket.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import org.apache.commons.lang3.Validate;
import pl.kacperduras.tasket.Tasket;
import pl.kacperduras.tasket.annotation.Task;
import pl.kacperduras.tasket.annotation.Timer;

import java.util.concurrent.TimeUnit;

public final class BungeeTasket extends Tasket {

    private final Plugin plugin;

    public BungeeTasket(Plugin plugin) {
        Validate.notNull(plugin);

        this.plugin = plugin;
    }

    @Override
    protected Object start(Task annotation, Runnable runnable) {
        Validate.notNull(annotation);
        Validate.notNull(runnable);

        Timer timer = annotation.timer();

        if (timer.later()) {
            Validate.isTrue(timer.period() > 0);
            return plugin.getProxy().getScheduler().schedule(
                    plugin, runnable, timer.period(), TimeUnit.MILLISECONDS).getId();
        } else {
            if (timer.period() > 0) {
                return plugin.getProxy().getScheduler().schedule(
                        plugin, runnable, timer.period(), timer.period(), TimeUnit.MILLISECONDS).getId();
            } else {
                plugin.getProxy().getScheduler().runAsync(plugin, runnable);
            }
        }

        return null;
    }

    @Override
    protected boolean stop(Object id) {
        Validate.notNull(id);
        Validate.isTrue(id instanceof Integer, "Id must be a Integer!");

        Integer castedId = (Integer) id;
        plugin.getProxy().getScheduler().cancel(castedId);

        return true;
    }

    @Override
    protected boolean state(Object object) {
        return true;
    }

}
