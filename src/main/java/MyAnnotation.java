/*Задание 2 (дополнительно)

Создайте свою аннотацию Repeat с целочисленным параметром.
Расширьте класс ThreadPoolExecutor и переопределите метод execute следующим образом:
если экземпляр Runnable имеет аннотацию Repeat, то его метод run выполняется несколько раз
(количество задается параметром в Repeat).
То есть, написав такой класс:

@Repeat(3)
class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("Hello!");
    }
}

и использовав его:

public static void main(String[] strings) {
        CustomThreadPoolExecutor customThreadPoolExecutor =
                        new CustomThreadPoolExecutor(10);
        customThreadPoolExecutor.execute(new MyRunnable());
}

Мы должны увидеть:

Hello!
Hello!
Hello!
*/

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Retention(RetentionPolicy.RUNTIME)
@interface Repeat {
    int value();
}

@Repeat(3)
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello!");
    }
}

class CustomThreadPoolExecutor extends ThreadPoolExecutor {
    private static final long DEFAULT_KEEPALIVE_MILLIS = 10L;

    public CustomThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, Integer.MAX_VALUE,
                DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    @Override
    public void execute(Runnable command) {
        Repeat anno = command.getClass().getAnnotation(Repeat.class);
        for (int i = 0; i < anno.value(); i++) {
            super.execute(command);
        }
    }
}

class Main {
    public static void main(String[] strings) {
        CustomThreadPoolExecutor customThreadPoolExecutor =
                new CustomThreadPoolExecutor(10);
        customThreadPoolExecutor.execute(new MyRunnable());
        customThreadPoolExecutor.shutdown();
    }
}