import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class SingleExperiment {
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
     * TODO maybe it makes sense to return ArrayList or Array
     * @return list of future element (in fact it is list of pairs, cause i'm waiting all threads to stop)
     */
    public List<Future<Pair<Creature, Creature>>> execute() {
        if (this.generationNumber < 1) {
            throw new IllegalArgumentException("Number of steps must be as least 1");
        }
        try {
            List<Future<Pair<Creature, Creature>>> currentGeneration = CreatureOperations.formGenerationParallelStart(this.initialCreatures, this.bounds);
            for (int i = 0; i < this.generationNumber - 1; i++) {
                currentGeneration = CreatureOperations.formGenerationParallelIntermediate(currentGeneration, this.bounds);
            }
            return currentGeneration;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
