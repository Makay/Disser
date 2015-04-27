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
        List<Creature> summary2 = experiment.executeGeneticAlgorithmNoMLab();
        long timeSpent1 = System.nanoTime() - start;
        System.out.println((double)timeSpent1/1e9);
        System.out.println(summary2);


//        Creature[] creatures = new Creature[8];
//        for (int i = 0; i < 8; i++) {
//            creatures[i] = CreatureOperations.generateCreature(bounds);
//        }
//        Creature[] generation = CreatureOperationsNoMLab.formGenerationSequential(creatures, bounds);
//        for (int i = 0; i < 8; i++) {
//            System.out.println(generation[i]);
//        }
//        double result = NumericOperations.mutateMapSegmentNoRate(2.9, new Pair<Double, Double>(1.0, 3.0));
//        System.out.println(result);
     }
}
