package creatures;

import javafx.util.Pair;
import numericOperations.NumericOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Ruslan Mokaev on 27.04.2015.
 * mokaev.ruslan@gmail.com
 */
final class CreatureOperationsNoMLab {
    // --Commented out by Inspection (04.05.2015 21:06):private static final double mutationRate = 0.1;

    /**
     * Making a pair of creatures out of four random ones
     * @param creature0 - 1st random creature
     * @param creature1 - 2nd random creature
     * @param creature2 - 3rd random creature
     * @param creature3 - 4th random creature
     * @param bounds - bounds of parameters values
     * @return - pair of creatures to put in new generation
     */
    static Pair<Creature, Creature> fight(Creature creature0, Creature creature1,
                                          Creature creature2, Creature creature3,
                                          Pair<Double, Double>[] bounds) {
        List<Creature> creatures = Arrays.asList(creature0, creature1, creature2, creature3);
        Collections.shuffle(creatures);

        Creature parent0 = battleOfTwo(creatures.get(0), creatures.get(1));
        Creature parent1 = battleOfTwo(creatures.get(2), creatures.get(3));

        return makeChildrenNoMLab(parent0, parent1, bounds);
    }

    /**
     * @param creature0 - 1st input creature
     * @param creature1 - 2nd input creature
     * @return "better" creature (more likely its children would have more limit circle than other creatures' children)
     */
    private static Creature battleOfTwo(Creature creature0, Creature creature1) {
        return creature0.getFitness() > creature0.getFitness() ? creature0 : creature1;
    }


// --Commented out by Inspection START (04.05.2015 21:06):
//    public static Pair<Creature, Creature> recombinationRangeCreature(Creature creature0, Creature creature1, Pair<Double, Double>[] bounds) {
//
//        double[] creatureCoefficients0 = creature0.getCoefficients();
//        double[] creatureCoefficients1 = creature1.getCoefficients();
//        double[] resultCoefficients0 = new double[5];
//        double[] resultCoefficients1 = new double[5];
//
//        for (int i = 0; i < bounds.length; i++) {
//            Pair<Double, Double> temp = NumericOperations.recombinationRange(creatureCoefficients0[i], creatureCoefficients1[i], bounds[i]);
//            resultCoefficients0[i] = temp.getKey();
//            resultCoefficients1[i] = temp.getValue();
//        }
//
//        Creature resultCreature0 = new Creature(resultCoefficients0[0], resultCoefficients0[1], resultCoefficients0[2], resultCoefficients0[3], resultCoefficients0[4]);
//        Creature resultCreature1 = new Creature(resultCoefficients1[0], resultCoefficients1[1], resultCoefficients1[2], resultCoefficients1[3], resultCoefficients1[4]);
//        return new Pair<Creature, Creature>(resultCreature0, resultCreature1);
//    }
// --Commented out by Inspection STOP (04.05.2015 21:06)

    private static Pair<Creature, Creature> makeChildrenNoMLab(Creature creature0, Creature creature1, Pair<Double, Double>[] bounds) {
        double[] creatureCoefficients0 = creature0.getCoefficients();
        double[] creatureCoefficients1 = creature1.getCoefficients();
        double[] resultCoefficients0 = new double[5];
        double[] resultCoefficients1 = new double[5];

        for (int i = 0; i < bounds.length; i++) {
            Pair<Double, Double> temp = NumericOperations.recombinationRange(creatureCoefficients0[i], creatureCoefficients1[i], bounds[i]);
            resultCoefficients0[i] = temp.getKey();
            resultCoefficients1[i] = temp.getValue();
        }

        for (int i = 0; i < 5; i++) {
            double mutated0 = NumericOperations.mutateMapSegmentNoRate(resultCoefficients0[i], bounds[i]);
            double mutated1 = NumericOperations.mutateMapSegmentNoRate(resultCoefficients1[i], bounds[i]);
            resultCoefficients0[i] = mutated0;
            resultCoefficients1[i] = mutated1;
        }

        Creature resultCreature0 = new Creature(resultCoefficients0);
        Creature resultCreature1 = new Creature(resultCoefficients1);

        return new Pair<Creature, Creature>(resultCreature0, resultCreature1);
    }
    /**
     * Make first generation
     * @param oldCreatures - input creatures in this experiment
     * @param bounds - bounds of parameters values
     * @return - new generation of creatures
     * @throws InterruptedException
     */
    public static List<Future<Pair<Creature, Creature>>> formGenerationParallelStart(Creature[] oldCreatures, Pair<Double, Double>[] bounds) throws InterruptedException {
        int creatureNumber = oldCreatures.length;
        if (creatureNumber < 4) {
            throw new IllegalArgumentException("Not enough creatures in population");
        }
        int stepNumber = creatureNumber / 2;
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<CreatureCallableNoMLab> creatureCallables = new ArrayList<CreatureCallableNoMLab>();
        for (int i = 0; i < stepNumber; i++) {
            creatureCallables.add(new CreatureCallableNoMLab(oldCreatures, bounds));
        }
        List<Future<Pair<Creature, Creature>>> newGeneration = executor.invokeAll(creatureCallables);
        executor.shutdown();
        return newGeneration;
    }

    /**
     * Makes another generation of creatures
     * @param intermediateCreatures - input creatures on this step
     * @param bounds - bounds of parameters values
     * @return - output creatures
     * @throws java.util.concurrent.ExecutionException
     * @throws InterruptedException
     */
    public static List<Future<Pair<Creature, Creature>>> formGenerationParallelIntermediate(List<Future<Pair<Creature, Creature>>> intermediateCreatures, Pair<Double, Double>[] bounds) throws ExecutionException, InterruptedException {
        int stepNumber = intermediateCreatures.size();
        Creature[] newCreatures = new Creature[stepNumber * 2];
        for (int i = 0; i < stepNumber; i++) {
            newCreatures[2 *  i] = intermediateCreatures.get(i).get().getKey();
            newCreatures[2 *  i + 1] = intermediateCreatures.get(i).get().getValue();
        }
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<CreatureCallableNoMLab> creatureCallables = new ArrayList<CreatureCallableNoMLab>();
        for (int i = 0; i < stepNumber; i++) {
            creatureCallables.add(new CreatureCallableNoMLab(newCreatures, bounds));
        }
        List<Future<Pair<Creature, Creature>>> newGeneration = executor.invokeAll(creatureCallables);
        executor.shutdown();
        return newGeneration;
    }

// --Commented out by Inspection START (04.05.2015 21:06):
//    public static Creature[] formGenerationSequential(Creature[] oldCreatures, Pair<Double, Double>[] bounds) {
//        int creatureNumber = oldCreatures.length;
//        if (creatureNumber < 4) {
//            throw new IllegalArgumentException("Not enough creatures in population");
//        }
//        int stepNumber = creatureNumber / 2;
//
//        Integer[] numbers = new Integer[creatureNumber];
//        for (int i = 0; i < creatureNumber; i++) {
//            numbers[i] = i;
//        }
//        List<Integer> list = Arrays.asList(numbers);
//
//        Creature[] newCreatures = new Creature[creatureNumber];
//
//        for (int i = 0; i < stepNumber; i++) {
//            Collections.shuffle(list);
//            Pair<Creature, Creature> pair = fight(oldCreatures[list.get(0)], oldCreatures[list.get(1)],
//                    oldCreatures[list.get(2)], oldCreatures[list.get(3)], bounds);
//            newCreatures[2 * i] = pair.getKey();
//            newCreatures[2 * i + 1] = pair.getValue();
//        }
//        return newCreatures;
//    }
// --Commented out by Inspection STOP (04.05.2015 21:06)
}
