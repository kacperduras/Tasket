package pl.kacperduras.tasket.sponge;

import org.apache.commons.lang3.Validate;
import org.spongepowered.api.Game;
import org.spongepowered.api.scheduler.Task.Builder;
import pl.kacperduras.tasket.Tasket;
import pl.kacperduras.tasket.annotation.Task;
import pl.kacperduras.tasket.annotation.Timer;

public final class SpongeTasket extends Tasket {

    private final Object plugin;
    private final Game game;

    public SpongeTasket(Object plugin, Game game) {
        Validate.notNull(game);
        Validate.notNull(plugin);

        this.plugin = plugin;
        this.game = game;
    }

    @Override
    protected Object start(Task annotation, Runnable runnable) {
        Validate.notNull(annotation);
        Validate.notNull(runnable);

        String id = annotation.id();
        boolean async = annotation.async();
        Timer timer = annotation.timer();

        Builder builder = game.getScheduler().createTaskBuilder();
        if (async) {
            builder = builder.async();
        }

        if (timer.later()) {
            Validate.isTrue(timer.period() > 0);
            builder.delayTicks(timer.period());
        } else {
            if (timer.period() > 0) {
                builder.intervalTicks(timer.period());
            }
        }

        return builder.name(id)
                .execute(runnable)
                .submit(plugin)
                .getName();
    }

    @Override
    protected boolean stop(Object id) {
        Validate.notNull(id);
        Validate.isTrue(id instanceof String, "Id must be a String!");

        boolean state = state(id);
        if (!state) {
            return false;
        }

        String castedId = (String) id;
        game.getScheduler().getTasksByName(castedId)
                .forEach(org.spongepowered.api.scheduler.Task::cancel);

        return true;
    }

    @Override
    protected boolean state(Object object) {
        Validate.notNull(object);
        Validate.isTrue(object instanceof String, "Object must be a String!");

        String id = (String) object;
        return game.getScheduler().getTasksByName(id).size() > 0;
    }

}
