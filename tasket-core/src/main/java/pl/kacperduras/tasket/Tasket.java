package pl.kacperduras.tasket;

import pl.kacperduras.tasket.annotation.Task;

public abstract class Tasket {

    protected abstract Object start(Task annotation, Runnable runnable);

    protected abstract boolean stop(Object id);

    protected abstract boolean state(Object object);

}
