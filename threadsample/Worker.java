package threadsample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Worker implements Runnable {
    List<String> links;
    Random random = new Random();

    public Worker(List<String> links) {
        this.links = links;
    }

    @Override
    public void run() {
        for (String link : links) {
            try (InputStream in = new URL(link).openStream()) {
                int randInt = random.nextInt(100);
                String name = link.substring(link.length() - 10, link.length() - 4) + randInt;
                System.out.println("Processing " + link);
                Files.copy(in, Paths.get("src/threadsample/images/" + name + ".jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
