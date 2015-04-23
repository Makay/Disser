
import configOperations.Operations;
import javafx.util.Pair;
import matlabcontrol.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
//            List<Future<Pair<Creature, Creature>>> newCreatures = CreatureOperations.formGenerationParallelStart(initCreatures, bounds);
        SingleExperiment experiment = new SingleExperiment(bounds, initCreaturesNumber, iterationsNumber);
        List<Future<Pair<Creature, Creature>>> summary = experiment.execute();
        long timeSpent = System.nanoTime() - start;
        System.out.println(summary.size());
        System.out.println((double)timeSpent/1e9);
    }
}
