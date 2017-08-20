package pl.kacperduras.tasket;

import org.apache.commons.lang3.Validate;
import pl.kacperduras.tasket.annotation.Task;

import java.lang.reflect.Method;

public class TaskContainer {

    private final Tasket parent;

    private final String taskId;
    private final Task annotation;
    private final Object instance;
    private final Method method;

    private Object id;

    public TaskContainer(Tasket parent, Task annotation, Object instance, Method method) {
        Validate.notNull(parent);

        Validate.notNull(annotation);
        Validate.notNull(instance);
        Validate.notNull(method);

        this.parent = parent;

        this.taskId = annotation.id();
        this.annotation = annotation;
        this.instance = instance;
        this.method = method;
    }

    public boolean start(boolean force, Object... objects) {
        Runnable runnable = () -> {
            try {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                method.invoke(instance, objects);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };

        if (id != null) {
            boolean state = parent.state(id);
            if (state && force) {
                parent.stop(id);
            } else {
                return false;
            }
        }

        id = parent.start(annotation, runnable);
        if (id != null) {
            return false;
        }

        return true;
    }

    public boolean start(Object... objects) {
        return start(false, objects);
    }

    public boolean stop() {
        if (id != null) {
            boolean state = parent.state(id);
            if (state) {
                parent.stop(id);
                return true;
            }
        }

        return false;
    }

    public String getTaskId() {
        return taskId;
    }

    public Task getAnnotation() {
        return annotation;
    }

}
