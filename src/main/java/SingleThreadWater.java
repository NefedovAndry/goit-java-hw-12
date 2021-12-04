public class SingleThreadWater {
    int mainCount = 0;
    int countH = 0;
    int countO = 0;

    public void buildWater() {
        mainCount = 5;
        while (mainCount != 0) {
            while (countH != 2 || countO != 1){
                putH();
                putO();
            }
            int index = countH;
            for (int i = 0; i < index; i++) {
                printH();
                countH--;
            }
            index = countO;
            for (int i = 0; i < index; i++) {
                printO();
                countO--;
            }
            if (countH == 0 && countO == 0){
                mainCount--;
            } else {
                System.out.println("Something went wrong");
                break;
            }
        }
    }

    public void putH() {
        if (countH != 2) {
            countH++;
        }
    }

    public void putO() {
        if (countO != 1){
            countO++;
        }
    }

    public void printH() {
        System.out.print("H");
    }

    public void printO() {
        System.out.println("O");
    }
}

class Test {
    public static void main(String[] args) {
        SingleThreadWater water = new SingleThreadWater();
        water.buildWater();
    }
}