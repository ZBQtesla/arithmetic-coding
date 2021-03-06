package encoding;

import java.io.*;

/**
 * Static arithmetic coding.
 *
 * @author Baiqiang Zhang
 */
public class StaticEncoder {
    private static final int CHARACTER_NUMBER = 256;
    // character's corresponding cumulative frequency
    private double[] asciiFrequency = new double[CHARACTER_NUMBER];

    /**
     * Count the cumulative frequency of each character in a given file.
     *
     * @param file textual file only contains ASCII Characters
     */
    private void countFrequency(File file) {
        // the number of occurrences per character
        long[] asciiOccur = new long[CHARACTER_NUMBER];

        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            int achar;
            while ((achar = reader.read()) != -1) {
                if (achar >= CHARACTER_NUMBER) {
                    System.out.println("This file contains invalid character " + (char) achar + ", we have skipped it");
                } else {
                    asciiOccur[achar]++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // total character number
        long sum = 0;
        for (long ao : asciiOccur) {
            sum += ao;
        }
        if (sum == 0) {
            System.out.println("No valid character.");
            return;
        }

        // calculate cumulative frequency
        double acc = 0;
        for (int i = 0; i < asciiFrequency.length; i++) {
            asciiFrequency[i] = acc + ((double) asciiOccur[i]) / sum;
        }
    }

    /**
     * Encode a file with arithmetic coding algorithm.
     *
     * @param rawFile         the file to be encoded
     * @param encodedFilePath the storage path of the file encoded
     */
    public void encoding(File rawFile, String encodedFilePath) {
        System.out.println("Encoding...");
        countFrequency(rawFile);

        StringBuilder stringBuffer = new StringBuilder();
        // initial boundary value
        double low = 0.0;
        double high = 1.0;

        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(rawFile));
            int achar;
            while ((achar = reader.read()) != -1) {
                double interval = high - low;
                // left and right determine the interval corresponding to current character
                double left, right;
                left = (achar == 0) ? 0 : asciiFrequency[achar - 1];
                right = asciiFrequency[achar];
                // update boundary value
                low = low + left * interval;
                high = low + right * interval;

                // continuously extract the new code
                while ((low * 2 >= 1 && high * 2 >= 1) || (low * 2 < 1 && high * 2 < 1)) {
                    if ((low * 2 >= 1 && high * 2 >= 1)) {
                        stringBuffer.append(1);
                        low = low * 2 - 1;
                        high = high * 2 - 1;
                    } else {
                        stringBuffer.append(0);
                        low = low * 2;
                        high = high * 2;
                    }
                }
            }

            // the last symbol make corresponding decimal h satisfy: low <= h <= high
            stringBuffer.append(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write the compression code to a file
        System.out.println("Encode successfully, write encoded file to " + encodedFilePath);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(encodedFilePath)));
            bufferedWriter.write(stringBuffer.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[] getAsciiFrequency() {
        return asciiFrequency;
    }
}
