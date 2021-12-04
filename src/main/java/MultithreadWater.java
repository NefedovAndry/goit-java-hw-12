public class MultithreadWater {
    public static void main(String[] args) {

    }
}

class WaterBuilding {

    int countOfHydrogen;
    final int maxCountOfHydrogen = 2;
    int countOfOxygen;
    final int maxCountOfOxygen = 1;
    boolean done;

    public synchronized void buildWater() throws InterruptedException {
        if (countOfHydrogen != 2 && countOfOxygen != 1) {
            wait();
        }
    }

    public synchronized void putHydrogen() throws InterruptedException {
        if (countOfHydrogen != maxCountOfHydrogen) {
            countOfHydrogen++;
        } else {
            wait();
        }
        notifyAll();
    }

    public synchronized void putOxygen() throws InterruptedException {
        if (countOfOxygen != maxCountOfOxygen) {
            countOfOxygen++;
        } else {
            wait();
        }
        notifyAll();
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