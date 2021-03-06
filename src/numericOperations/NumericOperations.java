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
    private static final double threshold = 1e-15;
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

// --Commented out by Inspection START (04.05.2015 21:07):
//    /**
//     * This one is wrong
//     * Mutation of long value. Each bit toggled with pr. == rate
//     * @param value - value to mutate
//     * @param rate - mutation rate
//     * @return - mutated value
//     */
//    private static long mutateWithRate(long value, double rate) {
//        int bitNum = Long.toBinaryString(value).length();
//        long result = value;
//        for (int i = 0; i < bitNum; i++) {
//            if (Math.random() <= rate) {
//                result ^= (1 << i);
//            }
//        }
//        return result;
//    }
// --Commented out by Inspection STOP (04.05.2015 21:07)

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

// --Commented out by Inspection START (04.05.2015 21:07):
//    /**
//     * Mutate value of parameter with respect of its bounds. Using mutation rate.
//     * @param value - value to mutate
//     * @param parameterBounds - min and max values of parameter
//     * @param rate - mutation rate
//     * @return - mutated value with respect of its bounds
//     */
//    public static double mutateMapSegmentWithRate(double value, Pair<Double, Double> parameterBounds, double rate) {
//        double minValue = parameterBounds.getKey();
//        double maxValue = parameterBounds.getValue();
//        double range = maxValue - minValue;
//        int degree = 50;
//
//        double valueNormed = value - minValue;
//        long valueToBigLong = Math.round(Math.pow(2, degree) * valueNormed / range);
//
//        long mutated = mutateWithRate(valueToBigLong, rate);
//
//        return range * (mutated / Math.pow(2, degree)) + minValue;
//    }
// --Commented out by Inspection STOP (04.05.2015 21:07)

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

    /**
     * Checks if value is zero with e15 precision
     * @param value - value to be checked
     * @return - true if value ~ zero, false otherwise
     */
    private static boolean isZero(double value){
        return value >= -threshold && value <= threshold;
    }

    /**
     * removes redundant zero points from array
     * @param array - input array
     * @return - result array without 0-0 pairs
     */
    public static double[][] removeZeros(double[][] array) {
        int resultSize = 0;
        for (int i = 0; i < array.length; i++) {
            if (isZero(array[i][0]) && isZero(array[i][1])) {
                resultSize = i;
                break;
            }
        }
        double[][] resultArray = new double[resultSize][2];
        for (int i = 0; i < resultSize; i++) {
            resultArray[i][0] = array[i][0];
            resultArray[i][1] = array[i][1];
        }
        return resultArray;
    }

    /**
     * Calculates value of interpolated amplitude function in given value using Lagrange
     * @param xCoord - given value
     * @param points - points of AF
     * @return - value in point
     */
    public static double amplitudeFunctionValue(double xCoord, double[][] points) {
        double yCoord = 0;
        for (int i = 0; i < points.length; i++) {
            double curr = 1;
            for (int j = 0; j < points.length; j++) {
                if (j != i) {
                    curr *= (xCoord - points[j][0]) / (points[i][0] - points[j][0]);
                }
            }
            yCoord += curr * points[i][1];
        }
        return yCoord;
    }

    /**
     * Calculates value of interpolated derivative of amplitude function in given value using Lagrange
     * @param xCoord - given value
     * @param points - points of AF
     * @return - value in point
     */
    public static double amplitudeFunctionDerivative(double xCoord, double[][] points) {
        double yCoord = 0;
        for (int i = 0; i < points.length; i++) {
            double deriv = 0;
            for (int p = 0; p < points.length; p++) {
                if (i != p) {
                    double curr = 1;
                    for (int k = 0; k < points.length; k++) {
                        if (k != p && k != i) {
                            curr *= (xCoord - points[k][0]) / (points[i][0] - points[k][0]);
                        }
                    }
                    curr /= (points[i][0] - points[p][0]);
                    deriv += curr;
                }
            }
            yCoord += deriv * points[i][1];
        }
        return yCoord;
    }
}
