package pl.kacperduras.tasket.bukkit;

import org.apache.commons.lang3.Validate;
import org.bukkit.plugin.Plugin;
import pl.kacperduras.tasket.Tasket;
import pl.kacperduras.tasket.annotation.Task;
import pl.kacperduras.tasket.annotation.Timer;

public final class BukkitTasket extends Tasket {

    private final Plugin plugin;

    public BukkitTasket(Plugin plugin) {
        Validate.notNull(plugin);

        this.plugin = plugin;
    }

    @Override
    protected Object start(Task annotation, Runnable runnable) {
        Validate.notNull(annotation);
        Validate.notNull(runnable);

        boolean async = annotation.async();
        Timer timer = annotation.timer();

        if (async) {
            if (timer.later()) {
                Validate.isTrue(timer.period() > 0);
                return plugin.getServer().getScheduler().runTaskLaterAsynchronously(
                        plugin, runnable, timer.period()).getTaskId();
            } else {
                if (timer.period() > 0) {
                    return plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                            plugin, runnable, timer.period(), timer.period()).getTaskId();
                } else {
                    return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable).getTaskId();
                }
            }
        } else {
            if (timer.later()) {
                Validate.isTrue(timer.period() > 0);
                return plugin.getServer().getScheduler().runTaskLaterAsynchronously(
                        plugin, runnable, timer.period()).getTaskId();
            } else {
                if (timer.period() > 0) {
                    return plugin.getServer().getScheduler().runTaskTimer(
                            plugin, runnable, timer.period(), timer.period()).getTaskId();
                } else {
                    return plugin.getServer().getScheduler().runTask(plugin, runnable).getTaskId();
                }
            }
        }
    }

    @Override
    protected boolean stop(Object id) {
        Validate.notNull(id);
        Validate.isTrue(id instanceof Integer, "Id must be a Integer!");

        boolean state = state(id);
        if (!state) {
            return false;
        }

        Integer castedId = (Integer) id;
        plugin.getServer().getScheduler().cancelTask(castedId);

        return true;
    }

    @Override
    protected boolean state(Object object) {
        Validate.notNull(object);
        Validate.isTrue(object instanceof Integer, "Object must be a Integer!");

        Integer id = (Integer) object;
        return plugin.getServer().getScheduler().isCurrentlyRunning(id);
    }

}
