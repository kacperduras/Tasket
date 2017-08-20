package pl.kacperduras.tasket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.kacperduras.tasket.annotation.Task;
import pl.kacperduras.tasket.annotation.Timer;

import java.lang.reflect.Method;

@RunWith(MockitoJUnitRunner.class)
public class TaskContainerTests {

    @Mock
    private Tasket tasket;

    private TaskContainer container;

    @Before
    public void setup() throws NoSuchMethodException {
        Method method = Tasks.class.getMethod("method", String.class);
        Task annotation = method.getAnnotation(Task.class);

        container = new TaskContainer(tasket, annotation, new Tasks("Parent"), method);
    }

    @Test
    public void testInject() {
        container.start("Parent");
    }

    private class Tasks {

        private final String parent;

        public Tasks(String parent) {
            this.parent = parent;
        }

        @Task(id = "method", timer = @Timer(period = 20, later = true), async = true)
        public void method(String method) {
            Assert.assertEquals("Parent", parent);
            Assert.assertEquals("Tasket", method);
        }

    }

}
