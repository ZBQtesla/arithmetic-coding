package encoding;

import java.io.*;
import java.util.Arrays;

/**
 * Adaptive arithmetic coding.
 *
 * @author Baiqiang Zhang
 */
public class AdaptiveEncoder {
    private static final int CHARACTER_NUMBER = 256;
    private long[] asciiOccur;
    private long charNum = 0;

    public AdaptiveEncoder() {
        asciiOccur = new long[CHARACTER_NUMBER];
        // initially, all character are given the same probability
        Arrays.fill(asciiOccur, 1);
    }

    public void encoding(File rawFile, String encodedFilePath) {
        System.out.println("Encoding...");
        StringBuilder stringBuilder = new StringBuilder();
        double low = 0.0;
        double high = 1.0;
        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(rawFile));
            int achar;
            while ((achar = reader.read()) != -1) {
                double interval = high - low;
                low = low + (getCumProb(achar) - ((double) asciiOccur[achar] / (charNum + CHARACTER_NUMBER))) * interval;
                high = low + getCumProb(achar) * interval;

                // continuously extract the new code
                while ((low * 2 >= 1 && high * 2 >= 1) || (low * 2 < 1 && high * 2 < 1)) {
                    if ((low * 2 >= 1 && high * 2 >= 1)) {
                        stringBuilder.append(1);
                        low = low * 2 - 1;
                        high = high * 2 - 1;
                    } else {
                        stringBuilder.append(0);
                        low = low * 2;
                        high = high * 2;
                    }
                }

                // the last symbol make corresponding decimal h satisfy: low <= h <= high
                stringBuilder.append(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write the compression code to a file
        System.out.println("Encode successfully, write encoded file to " + encodedFilePath);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(encodedFilePath)));
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get the cumulative frequency of a specified char.
     * @param achar a char
     * @return the cumulative frequency of this char
     */
    private double getCumProb(int achar) {
        long sum = 0;
        for (int i = 0; i < achar; i++) {
            sum +=asciiOccur[i];
        }
        return (double)sum/(double)(charNum+CHARACTER_NUMBER);
    }

}
