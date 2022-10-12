import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger fcounter = new AtomicInteger();
    static AtomicInteger scounter = new AtomicInteger();
    static AtomicInteger tcounter = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread th1 = buildThread(3, texts, fcounter);
        Thread th2 = buildThread(4, texts, scounter);
        Thread th3 = buildThread(5, texts, tcounter);

        th1.start();
        th2.start();
        th3.start();

        th1.join();
        th2.join();
        th3.join();

        System.out.println("Красивых слов с длиной 3: " + fcounter);
        System.out.println("Красивых слов с длиной 4: " + scounter);
        System.out.println("Красивых слов с длиной 5: " + tcounter);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread buildThread(int size, String[] texts, AtomicInteger counter) {
        return new Thread(() -> {
            for (String password : texts) {
                if (password.length() != size) continue;
                if (password.equals(new StringBuilder(password).reverse().toString())) {
                    counter.getAndAdd(1);
                } else if (password.equals(new StringBuilder().replace(0, size, password.charAt(0) + "").toString())) {
                    counter.getAndAdd(1);
                } else if (
                        password.lastIndexOf('a') < password.indexOf('b') &&
                                password.lastIndexOf('b') < password.indexOf('c') &&
                                password.lastIndexOf('a') < password.indexOf('c')
                ) {
                    counter.getAndAdd(1);
                }
            }
        });

    }
}
