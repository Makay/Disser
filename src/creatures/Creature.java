package creatures;

import javafx.util.Pair;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;

import java.util.Properties;

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

//    private final double fitness;

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
    }
//    public Creature(double a1, double b1, double c1, double alpha1, double beta1,
//                    double a2, double b2, double c2, double alpha2, double beta2) {
//        this.a1 = a1;
//        this.b1 = b1;
//        this.c1 = c1;
//        this.alpha1 = alpha1;
//        this.beta1 = beta1;
//        this.a2 = a2;
//        this.b2 = b2;
//        this.c2 = c2;
//        this.alpha2 = alpha2;
//        this.beta2 = beta2;
//    }

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
     * TODO
     * @return fitness of creature
     * @throws MatlabInvocationException
     * @throws MatlabConnectionException
     */
    public double calculateFitness() throws MatlabInvocationException, MatlabConnectionException {
        // connect by proxy
        // obtain points of amplitude function
        // 0) build derivative and limit its local extremums
        // 1) limit AF local extremums
        Pair<MatlabProxy, Properties> connection = matlab.MatlabOperations.openConnection();
        MatlabProxy proxy = connection.getKey();
        String path = connection.getValue().getProperty("filepath");

        proxy.eval("addpath('" + path + "')");

        proxy.feval("PSD", 0.1, 10000, 0, 0, 0.5, 1.e-15, a2, b2, c2, alpha2, beta2);

        proxy.eval("rmpath('" + path + "')");
        proxy.disconnect();
        return 0.0;
    }

    public double[] getCoefficients() {
        return new double[] {a2, b2, c2, alpha2, beta2};
    }
}
