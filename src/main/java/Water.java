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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Water {
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lockHydrogen = new ReentrantLock();
        ReentrantLock lockOxygen = new ReentrantLock();
        Condition notFullHydrogen = lockHydrogen.newCondition();
        Condition notFullOxygen = lockOxygen.newCondition();

        ExecutorService executorHydrogen = Executors.newSingleThreadExecutor();
        ExecutorService executorOxygen = Executors.newSingleThreadExecutor();
//        executorHydrogen.execute();
//        executorOxygen.execute();
        executorHydrogen.shutdown();
        executorOxygen.shutdown();

    }
}

class WaterMaker {
    int countOfHydrogen;
    final int maxCountOfHydrogen = 2;
    int countOfOxygen;
    final int maxCountOfOxygen = 1;
    boolean done;

    public void buildWater() {
        done = false;
        if (countOfHydrogen != 2 && countOfOxygen != 1) {
            while (countOfHydrogen == 2 && countOfOxygen == 1) {
//                await();
            }
        }
        releaseHydrogen();
        releaseOxygen();
    }

    public void putHydrogen() {
        while (!done) {
            if (countOfHydrogen != maxCountOfHydrogen) {
                countOfHydrogen++;
            } else {
//                await();
            }
//            signalAll();
        }
    }

    public void putOxygen() {
        while (!done) {
            if (countOfOxygen != maxCountOfOxygen) {
                countOfOxygen++;
            } else {
//                await();
            }
//            signalAll();
        }
    }

    public void releaseHydrogen() {
        System.out.print("H");
        countOfHydrogen--;
    }

    public void releaseOxygen() {
        System.out.println("O");
        countOfOxygen--;
    }

}

/*class BoundedBuffer<E> {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    public void put(E x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            E x = (E) items[takeptr];
            if (++takeptr == items.length) takeptr = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}*/

/*class Driver { // ...
    void main() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; ++i) // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();

        doSomethingElse();            // don't let run yet
        startSignal.countDown();      // let all threads proceed
        doSomethingElse();
        doneSignal.await();           // wait for all to finish
    }
}

class Worker implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }
    public void run() {
        try {
            startSignal.await();
            doWork();
            doneSignal.countDown();
        } catch (InterruptedException ex) {} // return;
    }

    void doWork() { ... }
}*/

/*
class Driver2 { // ...
    void main() throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(N);
        Executor e = ...

        for (int i = 0; i < N; ++i) // create and start threads
            e.execute(new WorkerRunnable(doneSignal, i));

        doneSignal.await();           // wait for all to finish
    }
}

class WorkerRunnable implements Runnable {
    private final CountDownLatch doneSignal;
    private final int i;
    WorkerRunnable(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }
    public void run() {
        try {
            doWork(i);
            doneSignal.countDown();
        } catch (InterruptedException ex) {} // return;
    }

    void doWork() { ... }
}*/

/*class Solver {
   final int N;
   final float[][] data;
   final CyclicBarrier barrier;

   class Worker implements Runnable {
     int myRow;
     Worker(int row) { myRow = row; }
     public void run() {
       while (!done()) {
         processRow(myRow);

         try {
           barrier.await();
         } catch (InterruptedException ex) {
           return;
         } catch (BrokenBarrierException ex) {
           return;
         }
       }
     }
   }

   public Solver(float[][] matrix) {
     data = matrix;
     N = matrix.length;
     Runnable barrierAction = () -> mergeRows(...);
     barrier = new CyclicBarrier(N, barrierAction);

     List<Thread> threads = new ArrayList<>(N);
     for (int i = 0; i < N; i++) {
       Thread thread = new Thread(new Worker(i));
       threads.add(thread);
       thread.start();
     }

     // wait until done
     for (Thread thread : threads)
       thread.join();
   }
 }*/