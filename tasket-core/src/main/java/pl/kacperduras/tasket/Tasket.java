package pl.kacperduras.tasket;

import org.apache.commons.lang3.Validate;
import pl.kacperduras.tasket.annotation.Task;

import java.lang.reflect.Method;
import java.util.*;

public abstract class Tasket {

    private final Map<Class<?>, List<TaskContainer>> tasksMap = new HashMap<>();

    public final Optional<TaskContainer> getTask(Class<?> parent, String id) {
        Validate.notNull(parent);
        Validate.notNull(id);

        List<TaskContainer> containers = tasksMap.get(parent);
        return containers.size() == 0 ? Optional.empty() : containers.stream()
                .filter(task -> task.getTaskId().equals(id)).findAny();
    }

    public final Optional<TaskContainer> getTask(String id) {
        Validate.notNull(id);

        for (Class<?> aClass : tasksMap.keySet()) {
            Optional<TaskContainer> optionalTask = getTask(aClass, id);
            if (optionalTask.isPresent()) {
                return optionalTask;
            }
        }

        return Optional.empty();
    }

    public void loadTasks(Object instance) {
        Validate.notNull(instance);

        for (Method method : instance.getClass().getMethods()) {
            Task task = method.getDeclaredAnnotation(Task.class);
            if (task == null) {
                continue;
            }

            String id = task.id();
            if (getTask(id).isPresent()) {
                throw new RuntimeException();
            }

            TaskContainer container = new TaskContainer(this, task, instance, method);
            tasksMap.computeIfAbsent(instance.getClass(), map -> new ArrayList<>()).add(container);

            if (task.timer().period() > 0) {
                container.start(true);
            }
        }
    }

    protected abstract Object start(Task annotation, Runnable runnable);

    protected abstract boolean stop(Object id);

    protected abstract boolean state(Object object);

}
