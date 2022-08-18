package threadsample;


import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainThread {
    public static List<String> getLinks() {
        List<String> list = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/threadsample/images.txt"))) {
            String link;
            while ((link = reader.readLine()) != null) {
                list.add(link);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public static void writeResultToFile(int threadCount, long result) {
        String dir = "src/threadsample/";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dir + "results.txt", true))) {
            writer.write("Thread count: " + threadCount + ", " + "Result: " + result + " ms\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void deleteFiles(String directoryName) {
        try {
            Files.newDirectoryStream(Paths.get("src/threadsample/" + directoryName)).forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        deleteFiles("images");

        Scanner scan = new Scanner(System.in);
        List<String> links = getLinks();

        List<Thread> allThreads = new LinkedList<>();

        System.out.print("Enter thread count: ");
        int threadCount = scan.nextInt();
        int low = 0;

        int linkPerThread = 0;

        if (links.size() % threadCount == 0) {
            linkPerThread = links.size() / threadCount;
        } else {
            linkPerThread = links.size() / (threadCount - 1);
        }

        int tracker = 0;
        System.out.println(linkPerThread);
        for (int i = 0; i < threadCount; i++) {
            List<String> subList = null;

            if (tracker + linkPerThread > links.size()) {
                subList = links.subList(tracker, links.size());
            } else {
                subList = links.subList(tracker, tracker + linkPerThread);
            }
            tracker += linkPerThread;

            Thread t = new Thread(new Worker(subList));
            allThreads.add(t);
        }
        long start = System.currentTimeMillis();
        for (Thread t : allThreads) {
            t.start();
        }

        for (Thread t : allThreads) {
            t.join();
        }
        long end = System.currentTimeMillis();


        long result = end - start;
        System.out.println("Thread count: " + threadCount + ", " + "Result " + result + " ms");
        writeResultToFile(threadCount, result);
    }
}

