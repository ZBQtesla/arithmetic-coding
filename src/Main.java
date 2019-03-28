import encoding.AdaptiveEncoder;
import encoding.StaticEncoder;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start static arithmetic coding.");
        File file = new File("data/raw/test.txt");
        StaticEncoder staticEncoder = new StaticEncoder();
        staticEncoder.encoding(file, "data/decoded/static_res_test.txt");

        System.out.println("Start adaptive arithmetic coding.");
        AdaptiveEncoder adaptiveEncoder = new AdaptiveEncoder();
        adaptiveEncoder.encoding(file, "data/decoded/adaptive_res_test.txt");
    }
}
