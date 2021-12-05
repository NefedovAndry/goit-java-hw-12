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

    int countOfHydrogen;
    final int maxCountOfHydrogen = 2;
    int countOfOxygen;
    final int maxCountOfOxygen = 1;
    boolean buildWaterCompleted;
    boolean isNewProcess;

    private boolean isWater() {
        return countOfHydrogen == maxCountOfHydrogen && countOfOxygen == maxCountOfOxygen;
    }

    private boolean isClear() {
        return countOfHydrogen == 0 && countOfOxygen == 0;
    }

    public synchronized void buildWater() throws InterruptedException {
        if (isWater()) {
            buildWaterCompleted = true;
            notifyAll();
        } else {
            while (!buildWaterCompleted) {
                wait();
            }
        }
    }

    public synchronized void putHydrogen() throws InterruptedException {
        if (countOfHydrogen == maxCountOfHydrogen) {
            while (!isNewProcess) {
                wait();
            }
        }
        isNewProcess = false;
        countOfHydrogen++;
    }

    public synchronized void putOxygen() throws InterruptedException {
        if (countOfOxygen == maxCountOfOxygen) {
            while (!isNewProcess) {
                wait();
            }
        }
        isNewProcess = false;
        countOfOxygen++;
    }

    public synchronized void releaseHydrogen() {
        System.out.print("H");
        countOfHydrogen--;
        if (isClear()) {
            System.out.println(",");
            buildWaterCompleted = false;
            isNewProcess = true;
            notifyAll();
        }
    }

    public synchronized void releaseOxygen() {
        System.out.print("O");
        countOfOxygen--;
        if (isClear()) {
            System.out.println(",");
            buildWaterCompleted = false;
            isNewProcess = true;
            notifyAll();
        }
    }

}