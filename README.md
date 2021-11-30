# goit-java-hw-12

Домашнее задание

нельзя использовать synchronized.
нужно использовать методы находящиеся в пакете java.util.concurrent.


Задание 1
Есть два вида потоков,  oxygen и hydrogen. Ваша задача сгруппировать потоки и составить из них молекулы воды. Должен быть барьер, где каждый поток ждет, пока не будет составлена молекула воды. Для потоков должны быть методы releaseHydrogen и releaseOxygen, которые позволяют им "пройти барьер" и вывести в консоль Н или О. Потоки обязательно проходят барьер только группами из 3 (2 гидрогена и 1 оксиген). Так как после барьера идет вывод на консоль, убедитесь, что группа из 3 элементов была выведена до того, как начнется вывод следующей группы (корректный вывод: НОН, ОНН, ННО, но не НООН - значит один атом оксигена попал из другой группы).
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


Задание 2 (дополнительно)
Создайте свою аннотацию Repeat с целочисленным параметром.
Расширьте класс ​ ThreadPoolExecutor​​ и переопределите метод ​execute следующим образом: если экземпляр Runnable имеет аннотацию Repeat, то его метод run выполняется несколько раз (количество задается параметром в Repeat).
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
