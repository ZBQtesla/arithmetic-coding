import encoding.StaticEncoder;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("data/raw/test.txt");
        StaticEncoder staticEncoder = new StaticEncoder();
        staticEncoder.encoding(file, "data/decoded/res_test.txt");
    }
}
