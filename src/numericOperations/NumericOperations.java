package numericOperations;

import javafx.util.Pair;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ruslan Mokaev on 27.04.2015.
 * mokaev.ruslan@gmail.com
 */
public final class NumericOperations {
    private static String getBinaryRepresentation(long value) {
        return String.format("%64s", Long.toBinaryString(value)).replace(' ', '0');
    }

    private static Pair<Long, Long> recombinationLong(long value0, long value1) {
        String bits0 = getBinaryRepresentation(value0);
        String bits1 = getBinaryRepresentation(value1);

        Pair<String, String> binaryForm =  recombinationString(bits0, bits1);

        long resultValue0 = parseLong(binaryForm.getKey());
        long resultValue1 = parseLong(binaryForm.getValue());

        return new Pair<Long, Long>(resultValue0, resultValue1);
    }

    private static Pair<String, String> recombinationString(String bits0, String bits1) {
        int breakPoint = (int) (Math.random() * bits0.length());

        String result0 = bits0.substring(0, breakPoint) + bits1.substring(breakPoint, bits1.length());
        String result1 = bits1.substring(0, breakPoint) + bits0.substring(breakPoint, bits0.length());

        return new Pair<String, String>(result0, result1);
    }

    private static long parseLong(String value) {
        return new BigInteger(value, 2).longValue();
    }

    public static Pair<Double, Double> recombinationRange(double value0, double value1, Pair<Double, Double> parameterBounds) {
        double minValue = parameterBounds.getKey();
        double maxValue = parameterBounds.getValue();
        double range = maxValue - minValue;
        int degree = 40;

        double aCurr = value0 - minValue;
        double bCurr = value1 - minValue;

        long aTemp = Math.round(Math.pow(2, degree) * aCurr / range);
        long bTemp = Math.round(Math.pow(2, degree) * bCurr / range);

        Pair<Long, Long> tempResult = recombinationLong(aTemp, bTemp);

        double d0 = range * (tempResult.getKey()) / Math.pow(2, degree) + minValue;
        double d1 = range * (tempResult.getValue()) / Math.pow(2, degree) + minValue;

        return new Pair<Double, Double>(d0, d1);
    }

    /**
     * Mutation of long value. Each bit toggled with pr. == rate
     * @param value - value to mutate
     * @param rate - mutation rate
     * @return - mutated value
     */
    private static long mutateWithRate(long value, double rate) {
        int bitNum = Long.toBinaryString(value).length();
        long result = value;
        for (int i = 0; i < bitNum; i++) {
            if (Math.random() <= rate) {
                result ^= (1 << i);
            }
        }
        return result;
    }

    /**
     * Mutation of long value. Mutates about 10% of bits.
     * Not interested in whole 64 bits
     * @param value - value to mutate
     * @return - mutated value
     */
    private static long mutateNoRate(long value) {
        String valueString = Long.toBinaryString(value);
        int bitNum = valueString.length();
        char[] valueCharArray = valueString.toCharArray();

        Integer[] array = new Integer[bitNum];
        for (int i = 0; i < bitNum; i++) {
            array[i] = i;
        }
        List<Integer> list = Arrays.asList(array);
        Collections.shuffle(list);

        int bitsNumberToFlip = bitNum / 10;
        for (int i = 0; i < bitsNumberToFlip; i++) {
            char bit = valueCharArray[list.get(i)];
            if (bit == '0') {
                valueCharArray[list.get(i)] = '1';
            }
            else {
                valueCharArray[list.get(i)] = '0';
            }
        }
        return parseLong(new String(valueCharArray));
    }

    /**
     * Mutate value of parameter with respect of its bounds. Using mutation rate.
     * @param value - value to mutate
     * @param parameterBounds - min and max values of parameter
     * @param rate - mutation rate
     * @return - mutated value with respect of its bounds
     */
    public static double mutateMapSegmentWithRate(double value, Pair<Double, Double> parameterBounds, double rate) {
        double minValue = parameterBounds.getKey();
        double maxValue = parameterBounds.getValue();
        double range = maxValue - minValue;
        int degree = 50;

        double valueNormed = value - minValue;
        long valueToBigLong = Math.round(Math.pow(2, degree) * valueNormed / range);

        long mutated = mutateWithRate(valueToBigLong, rate);

        return range * (mutated / Math.pow(2, degree)) + minValue;
    }

    /**
     * Mutate value of parameter with respect of its bounds. Don't use mutation rate.
     * @param value - value to mutate
     * @param parameterBounds - min and max values of parameter
     * @return - mutated value with respect of its bounds
     */
    public static double mutateMapSegmentNoRate(double value, Pair<Double, Double> parameterBounds) {
        double minValue = parameterBounds.getKey();
        double maxValue = parameterBounds.getValue();
        double range = maxValue - minValue;
        int degree = 50;

        double valueNormed = value - minValue;
        long valueToBigLong = Math.round(Math.pow(2, degree) * valueNormed / range);

        long mutated = mutateNoRate(valueToBigLong);

        return range * (mutated / Math.pow(2, degree)) + minValue;
    }
}
