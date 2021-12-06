/*
Нельзя использовать synchronized.
Нужно использовать методы находящиеся в пакете java.util.concurrent.

Задание 1

Есть два вида потоков, oxygen и hydrogen. Ваша задача сгруппировать потоки и составить из них молекулы воды.
Должен быть барьер, где каждый поток ждет, пока не будет составлена молекула воды.
Для потоков должны быть методы releaseHydrogen и releaseOxygen, которые позволяют им "пройти барьер"
и вывести в консоль Н или О.
Потоки обязательно проходят барьер только группами из 3 (2 гидрогена и 1 оксиген).
Так как после барьера идет вывод на консоль, убедитесь, что группа из 3 элементов была выведена до того,
как начнется вывод следующей группы (корректный вывод: НОН, ОНН, ННО, но не НООН - значит один атом
оксигена попал из другой группы).
Другими словами:
если поток оксигена "подходит к барьеру", но нет потоков гидрогена, он ждет 2 потока гидрогена
если поток гидрогена "подходит к барьеру" и нет других потоков, он ждет поток оксигена и еще один поток гидрогена.

Пример 1:
Input: "HOH"
Output: "HHO"
Explanation: "HOH" and "OHH" are also valid answers.
Пример 2:
Input: "OOHHHH"
Output: "HHOHHO"
Explanation: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" and "OHHOHH" are also valid answers
*/

import java.util.concurrent.*;

public class Water {
    public static void main(String[] args) {
        Semaphore semHydrogen = new Semaphore(2);
        Semaphore semOxygen = new Semaphore(1);
        CyclicBarrier cb = new CyclicBarrier(3, new Messenger());

        ExecutorService executorHydrogen = Executors.newFixedThreadPool(5);
        ExecutorService executorOxygen = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 6; i++) {
            executorHydrogen.execute(new MyThread(semHydrogen, cb, "H"));
        }
        for (int i = 0; i < 3; i++) {
            executorOxygen.execute(new MyThread(semOxygen, cb, "O"));
        }
        executorHydrogen.shutdown();
        executorOxygen.shutdown();
    }
}

class MyThread implements Runnable {
    Semaphore sem;
    CyclicBarrier cb;
    String name;

    public MyThread(Semaphore sem, CyclicBarrier cb, String name) {
        this.cb = cb;
        this.name = name;
        this.sem = sem;
        new Thread(this, name);
    }

    @Override
    public void run() {
        try {
            sem.acquire();
            cb.await(1, TimeUnit.SECONDS);
            System.out.print(name);
            sem.release();
        } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
            e.printStackTrace();
        }
    }


}

class Messenger implements Runnable {
    @Override
    public void run() {
        System.out.print(" ");
    }
}
