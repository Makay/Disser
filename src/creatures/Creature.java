package creatures;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import numericOperations.NumericOperations;
import configOperations.Operations;

public class Creature {
    private final double a1;
    private final double b1;
    private final double c1;
    private final double alpha1;
    private final double beta1;
    private final double a2;
    private final double b2;
    private final double c2;
    private final double alpha2;
    private final double beta2;
    private final double fitness;
    private final int numberSteps = 1000;

    /**
     * Constructor for the current task, where parameters of first DE are defined
     * @param a2 - a2 coefficient
     * @param b2 - b2 coefficient
     * @param c2 - c2 coefficient
     * @param alpha2 - alpha2 coefficient
     * @param beta2 - beta2 coefficient
     */
    public Creature(double a2, double b2, double c2, double alpha2, double beta2) {
        this.a1 = 1.0;
        this.b1 = 1.0;
        this.c1 = 0.0;
        this.alpha1 = 0.0;
        this.beta1 = 1.0;
        this.a2 = a2;
        this.b2 = b2;
        this.c2 = c2;
        this.alpha2 = alpha2;
        this.beta2 = beta2;
        this.fitness = fitnessFunctionExtremum();
    }

    public Creature(double[] values) {
        if (values.length != 5) {
            throw new IllegalArgumentException("5 arguments!");
        }
        this.a1 = 1.0;
        this.b1 = 1.0;
        this.c1 = 0.0;
        this.alpha1 = 0.0;
        this.beta1 = 1.0;
        this.a2 = values[0];
        this.b2 = values[1];
        this.c2 = values[2];
        this.alpha2 = values[3];
        this.beta2 = values[4];
        this.fitness = fitnessFunctionExtremum();
    }

    public double getA2() {
        return a2;
    }

    public double getB2() {
        return b2;
    }

    public double getC2() {
        return c2;
    }

    public double getAlpha2() {
        return alpha2;
    }

    public double getBeta2() {
        return beta2;
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "Creature{" +
                "a1=" + a1 +
                ", b1=" + b1 +
                ", c1=" + c1 +
                ", alpha1=" + alpha1 +
                ", beta1=" + beta1 +
                ", a2=" + a2 +
                ", b2=" + b2 +
                ", c2=" + c2 +
                ", alpha2=" + alpha2 +
                ", beta2=" + beta2 +
                '}' + "\n";
    }

    /**
     * @return - 2nd five coefficients (of second equation)
     */
    public double[] getCoefficients() {
        return new double[] {a2, b2, c2, alpha2, beta2};
    }

    /**
     * TODO rewrite this function using some ODE solver (library)
     * @return - array of points (AF)
     * @throws MatlabInvocationException
     * @throws MatlabConnectionException
     */
    private double[][] PSD() throws MatlabInvocationException, MatlabConnectionException {
        MatlabProxy proxy = matlab.MatlabOperations.openConnection();
        String path = Operations.getProperties().getProperty("filepath");

        proxy.eval("addpath('" + path + "')");

        Object[] psdResult = proxy.returningFeval("PSD", 1, 0.1, 10000, 0, 0, 0.5, 1e-15, this.a2, this.b2, this.c2,
                this.alpha2, this.beta2);
        double[] result = (double[]) psdResult[0];
        int pointsNumber = result.length / 2;

        double[][] points = new double[pointsNumber][2];

        for (int i = 0; i < pointsNumber; i++) {
            points[i][0] = result[i];
            points[i][1] = result[i + pointsNumber];
        }

        proxy.eval("rmpath('" + path + "')");
        proxy.disconnect();
        points = NumericOperations.removeZeros(points);
        return points;
    }
    /**
     * Calculate inverse sum of extremums  of AF
     * The smaller sum of extremums the more creature fits
     * @return fitness of creature
     */
    private double fitnessFunctionExtremum() {
        // points of amplitude function
        double[][] afPoints = new double[0][];
        try {
            afPoints = PSD();
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        } catch (MatlabConnectionException e) {
            e.printStackTrace();
        }
        double extremumSum = 0.0;
        for (int i = 1; i < afPoints.length - 1; i++) {
            if (afPoints[i - 1][1] < afPoints[i][1] && afPoints[i][1] > afPoints[i + 1][1]) {
                extremumSum += afPoints[i][1];
            }
            if (afPoints[i - 1][1] > afPoints[i][1] && afPoints[i][1] < afPoints[i + 1][1]) {
                extremumSum += afPoints[i][1];
            }
        }
        return 1 / extremumSum;
    }

    /**
     * Calculates fitness of creature (of its AF)
     * The smaller sum of extremums the more creature fits
     * @return fitness (inverse sum of extremums)
     */
    private double fitnessFunctionExtremumGrid() {
        // points of amplitude function
        double[][] afPoints = new double[0][];
        try {
            afPoints = PSD();
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        } catch (MatlabConnectionException e) {
            e.printStackTrace();
        }
        double extremumSum = 0.0;

        double range = afPoints[afPoints.length - 1][0] - afPoints[0][0];
        double step = Math.min(range / numberSteps, afPoints[1][0] - afPoints[0][0]);
        long stepsNumber = Math.round(range / step);

        double value0 = NumericOperations.amplitudeFunctionValue(afPoints[0][0], afPoints);
        double value1 = NumericOperations.amplitudeFunctionValue(afPoints[0][0] + step, afPoints);
        double value2 = NumericOperations.amplitudeFunctionValue(afPoints[0][0] + step * 2, afPoints);

        for (int i = 3; i < stepsNumber - 1; i++) {
            if ((value0 < value1 && value1 > value2) || (value0 > value1 && value1 < value2)) {
                extremumSum += value1;
            }
            value0 = value1;
            value1 = value2;
            value2 = NumericOperations.amplitudeFunctionValue(afPoints[0][0] + step * i, afPoints);
        }

        return 1 / extremumSum;
    }

    /**
     * Calculate maximum of AF's derivative
     * The smaller max (the bigger 1 / max) the bigger probability of AF have more intersections with abscissa
     * @return - AF's derivative maximum value on given range
     */
    private double fitnessDerivativeExtremum() {
        // points of amplitude function
        double[][] afPoints = new double[0][];
        try {
            afPoints = PSD();
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        } catch (MatlabConnectionException e) {
            e.printStackTrace();
        }
        double range = afPoints[afPoints.length - 1][0] - afPoints[0][0];
        double step = Math.min(range / numberSteps, afPoints[1][0] - afPoints[0][0]);
        long stepsNumber = Math.round(range / step);
        double max = 0;
        for (int i = 0; i < stepsNumber; i++) {
            double curr = NumericOperations.amplitudeFunctionDerivative(afPoints[0][0] + i * step, afPoints);
            if (Math.abs(curr) > max) {
                max = Math.abs(curr);
            }
        }
        return 1 / max;
    }
}
