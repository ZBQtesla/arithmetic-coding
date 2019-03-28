import encoding.AdaptiveEncoder;
import encoding.StaticEncoder;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("data/raw/test.txt");
        StaticEncoder staticEncoder = new StaticEncoder();
        staticEncoder.encoding(file, "data/decoded/static_res_test.txt");

        AdaptiveEncoder adaptiveEncoder = new AdaptiveEncoder();
        adaptiveEncoder.encoding(file, "data/decoded/adaptive_res_test.txt");
    }
}
