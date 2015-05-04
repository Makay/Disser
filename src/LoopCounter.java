import configOperations.Operations;
import creatures.Creature;
import creatures.SingleExperiment;
import javafx.util.Pair;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import java.util.List;
import java.util.concurrent.ExecutionException;

class LoopCounter {
    public static void main(String[] args) throws MatlabConnectionException, MatlabInvocationException, ExecutionException, InterruptedException {
        @SuppressWarnings("unchecked") Pair<Double, Double>[] bounds = new Pair[5];
        bounds[0] = new Pair<Double, Double>(-15.0, -5.0);
        bounds[1] = new Pair<Double, Double>(1.0, 3.0);
        bounds[2] = new Pair<Double, Double>(0.0, 1.0);
        bounds[3] = new Pair<Double, Double>(-100.0, -50.0);
        bounds[4] = new Pair<Double, Double>(0.0, 0.5);

        int initCreaturesNumber = Operations.getInitCreaturesNumber();
        int iterationsNumber = Operations.getIterationsNumber();

        long start = System.nanoTime();
        SingleExperiment experiment = new SingleExperiment(bounds, initCreaturesNumber, iterationsNumber);
        List<Creature> summary = experiment.executeGeneticAlgorithmNoMLab();
        long timeSpent = System.nanoTime() - start;
        System.out.println((double)timeSpent/1e9);
        System.out.println(summary);
    }
}
