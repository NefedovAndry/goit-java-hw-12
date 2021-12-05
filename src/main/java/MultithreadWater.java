public class MultithreadWater {
    public static void main(String[] args) {
        WaterBuilding waterBuilding = new WaterBuilding();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    waterBuilding.putHydrogen();
                    waterBuilding.buildWater();
                    waterBuilding.releaseHydrogen();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Hydrogen").start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    waterBuilding.putOxygen();
                    waterBuilding.buildWater();
                    waterBuilding.releaseOxygen();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Oxygen").start();
        }

    }
}

class WaterBuilding {

    int countOfHydrogen = 0;
    final int maxCountOfHydrogen = 2;
    int countOfOxygen = 0;
    final int maxCountOfOxygen = 1;
    boolean buildWaterCompleted = false;
    boolean isNewProcess = false;

    private boolean isWater() {
        return countOfHydrogen == maxCountOfHydrogen && countOfOxygen == maxCountOfOxygen;
    }

    private boolean isClear() {
        return countOfHydrogen == 0 && countOfOxygen == 0;
    }

    public synchronized void putHydrogen() throws InterruptedException {
        if (countOfHydrogen == maxCountOfHydrogen) {
            System.out.println(" Blocked in put: " + Thread.currentThread().getName());
            while (!isNewProcess) {
                wait();
            }
        }
        if (countOfHydrogen == maxCountOfHydrogen) {
            isNewProcess = false;
        }
        buildWaterCompleted = false;
        countOfHydrogen++;
    }

    public synchronized void putOxygen() throws InterruptedException {
        if (countOfOxygen == maxCountOfOxygen) {
            System.out.println(" Blocked in put: " + Thread.currentThread().getName());
            while (!isNewProcess) {
                wait();
            }
        }
        if (countOfOxygen == maxCountOfOxygen) {
            isNewProcess = false;
        }
        buildWaterCompleted = false;
        countOfOxygen++;
    }

    public synchronized void buildWater() throws InterruptedException {
        if (isWater()) {
            buildWaterCompleted = true;
            notifyAll();
        } else {
            System.out.println(" Blocked in build: " + Thread.currentThread().getName());
            while (!buildWaterCompleted) {
                wait();
            }
        }
    }

    public synchronized void releaseHydrogen() {
        System.out.print("H");
        countOfHydrogen--;
        if (isClear()) {
            System.out.println(",");
            isNewProcess = true;
        }
        notifyAll();
    }

    public synchronized void releaseOxygen() {
        System.out.print("O");
        countOfOxygen--;
        if (isClear()) {
            System.out.println(",");
            isNewProcess = true;
        }
        notifyAll();
    }

}