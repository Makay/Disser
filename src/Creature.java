/**
 * Created by 777 on 07.04.2015.
 */
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
     * @param a2
     * @param b2
     * @param c2
     * @param alpha2
     * @param beta2
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
                '}';
    }
}
