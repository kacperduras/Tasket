package pl.kacperduras.tasket;

import org.apache.commons.lang3.Validate;
import pl.kacperduras.tasket.annotation.Task;

import java.lang.reflect.Method;

public class TaskContainer {

    private final Tasket parent;

    private final String taskId;
    private final Task annotation;
    private final Method method;

    private Object id;

    public TaskContainer(Tasket parent, Task annotation, Method method) {
        Validate.notNull(parent);

        Validate.notNull(annotation);
        Validate.notNull(method);

        this.parent = parent;

        this.taskId = annotation.id();
        this.annotation = annotation;
        this.method = method;
    }

    public Tasket getParent() {
        return parent;
    }

    public String getTaskId() {
        return taskId;
    }

    public Task getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }

}
