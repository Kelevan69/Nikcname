import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NicknameGenerator {
    private static final AtomicInteger countLength3 = new AtomicInteger(0);
    private static final AtomicInteger countLength4 = new AtomicInteger(0);
    private static final AtomicInteger countLength5 = new AtomicInteger(0);

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Проверка на палиндром
    private static boolean isPalindrome(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    // Проверка на одинаковые буквы
    private static boolean isSameLetters(String text) {
        char firstChar = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    // Проверка на возрастание букв
    private static boolean isAscending(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindromeThread = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread sameLettersThread = new Thread(() -> {
            for (String text : texts) {
                if (isSameLetters(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread ascendingThread = new Thread(() -> {
            for (String text : texts) {
                if (isAscending(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        palindromeThread.start();
        sameLettersThread.start();
        ascendingThread.start();

        palindromeThread.join();
        sameLettersThread.join();
        ascendingThread.join();

        // Вывод результатов
        System.out.println("Красивых слов с длиной 3: " + countLength3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + countLength4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + countLength5.get() + " шт");
    }

    // Увеличение счетчика в зависимости от длины текста
    private static void incrementCounter(int length) {
        switch (length) {
            case 3 -> countLength3.incrementAndGet();
            case 4 -> countLength4.incrementAndGet();
            case 5 -> countLength5.incrementAndGet();
        }
    }
}
