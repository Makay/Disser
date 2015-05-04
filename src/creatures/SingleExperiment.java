package creatures;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SingleExperiment {
    private final Pair<Double, Double>[] bounds;
    // maybe later we should make it stop via criteria
    private final int generationNumber;
    private final Creature[] initialCreatures;

    public SingleExperiment(Pair<Double, Double>[] bounds, int creatureNumber, int generationNumber) {
        this.bounds = bounds;
        this.generationNumber = generationNumber;
        this.initialCreatures = new Creature[creatureNumber];
        for (int i = 0; i < creatureNumber; i++) {
            initialCreatures[i] = CreatureOperations.generateCreature(bounds);
        }
    }

    /**
     * Function that makes experiment
     * @return list of future element (in fact it is list of pairs, cause i'm waiting all threads to stop)
     */
//    public List<Future<Pair<Creature, Creature>>> execute() {
//        if (this.generationNumber < 1) {
//            throw new IllegalArgumentException("Number of steps must be as least 1");
//        }
//        try {
//            List<Future<Pair<Creature, Creature>>> currentGeneration = CreatureOperations.formGenerationParallelStart(this.initialCreatures, this.bounds);
//            for (int i = 0; i < this.generationNumber - 1; i++) {
//                currentGeneration = CreatureOperations.formGenerationParallelIntermediate(currentGeneration, this.bounds);
//            }
//            return currentGeneration;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

// --Commented out by Inspection START (04.05.2015 21:05):
//    /**
//     * Method executes the GA algorithm
//     * @return - list of creatures of the generationNumber'th generation
//     */
//    public List<Creature> executeGeneticAlgorithm() {
//        if (this.generationNumber < 1) {
//            throw new IllegalArgumentException("Number of steps must be as least 1");
//        }
//        try {
//            List<Future<Pair<Creature, Creature>>> currentGeneration = CreatureOperations.formGenerationParallelStart(this.initialCreatures, this.bounds);
//            for (int i = 0; i < this.generationNumber - 1; i++) {
//                currentGeneration = CreatureOperations.formGenerationParallelIntermediate(currentGeneration, this.bounds);
//            }
//            List<Creature> result = new ArrayList<Creature>();
//            for (Future<Pair<Creature, Creature>> aCurrentGeneration : currentGeneration) {
//                result.add(aCurrentGeneration.get().getKey());
//                result.add(aCurrentGeneration.get().getValue());
//            }
//            return result;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (04.05.2015 21:05)
    /**
     * Method executes the GA algorithm
     * @return - list of creatures of the generationNumber'th generation
     */
    public List<Creature> executeGeneticAlgorithmNoMLab() {
        if (this.generationNumber < 1) {
            throw new IllegalArgumentException("Number of steps must be as least 1");
        }
        try {
            List<Future<Pair<Creature, Creature>>> currentGeneration = CreatureOperationsNoMLab.formGenerationParallelStart(this.initialCreatures, this.bounds);
            for (int i = 0; i < this.generationNumber - 1; i++) {
                currentGeneration = CreatureOperationsNoMLab.formGenerationParallelIntermediate(currentGeneration, this.bounds);
            }
            List<Creature> result = new ArrayList<Creature>();
            for (Future<Pair<Creature, Creature>> aCurrentGeneration : currentGeneration) {
                result.add(aCurrentGeneration.get().getKey());
                result.add(aCurrentGeneration.get().getValue());
            }
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
