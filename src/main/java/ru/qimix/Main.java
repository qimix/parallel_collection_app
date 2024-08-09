package ru.qimix;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    static AtomicInteger resultCountA = new AtomicInteger();
    static AtomicInteger resultCountB = new AtomicInteger();
    static AtomicInteger resultCountC = new AtomicInteger();

    static AtomicReference<String> resultStrA = new AtomicReference<>();
    static AtomicReference<String> resultStrB = new AtomicReference<>();
    static AtomicReference<String> resultStrC = new AtomicReference<>();

    public static void main(String[] args) {
        System.out.println("Подсчёт для символов a,b,c, запущен...");
        new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < 10000; i++) {
                String line = generateText("abc", 100000 + random.nextInt(3));
                try {
                    queueA.put(line);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    queueB.put(line);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    queueC.put(line);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            int tmpCountA = 0;
            for (int i = 0; i < 10000; i++) {
                try {
                    String a = queueA.take();
                    String[] array = a.split("");
                    for(String s : array){
                        if(s.equals("a")) {
                            tmpCountA++;
                        }
                    }
                    if(resultCountA.get() < tmpCountA) {
                        resultCountA.set(tmpCountA);
                        resultStrA.set(a);
                        tmpCountA = 0;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Для - 'а' результат " + resultCountA.get());
        }).start();

        new Thread(() -> {
            int tmpCountB = 0;
            for (int i = 0; i < 10000; i++) {
                try {
                    String a = queueB.take();
                    String[] array = a.split("");
                    for(String s : array){
                        if(s.equals("b")) {
                            tmpCountB++;
                        }
                    }
                    if(resultCountB.get() < tmpCountB) {
                        resultCountB.set(tmpCountB);
                        resultStrB.set(a);
                        tmpCountB = 0;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Для - 'b' результат " + resultCountB.get());
        }).start();

        new Thread(() -> {
            int tmpCountC = 0;
            for (int i = 0; i < 10000; i++) {
                try {
                    String a = queueC.take();
                    String[] array = a.split("");
                    for(String s : array){
                        if(s.equals("c")) {
                            tmpCountC++;
                        }
                    }
                    if(resultCountC.get() < tmpCountC) {
                        resultCountC.set(tmpCountC);
                        resultStrC.set(a);
                        tmpCountC = 0;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Для - 'c' результат " + resultCountC.get());
        }).start();
    }

    public static String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
