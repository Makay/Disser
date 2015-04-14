import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 777 on 07.04.2015.
 */
public class Experiment {
//    private final ArrayList<Creature> initialCreatures;
    private final Creature[] initialCreatures;
    private final double a2Min;
    private final double a2Max;
    private final double b2Min;
    private final double b2Max;
    private final double c2Min;
    private final double c2Max;
    private final double alpha2Min;
    private final double alpha2Max;
    private final double beta2Min;
    private final double beta2Max;
    private final int creatureNumber;

    public Experiment(double a2Min, double a2Max, double b2Min, double b2Max, double c2Min, double c2Max,
                      double alpha2Min, double alpha2Max, double beta2Min, double beta2Max, int creatureNumber) {
        this.a2Min = a2Min;
        this.a2Max = a2Max;
        this.b2Min = b2Min;
        this.b2Max = b2Max;
        this.c2Min = c2Min;
        this.c2Max = c2Max;
        this.alpha2Min = alpha2Min;
        this.alpha2Max = alpha2Max;
        this.beta2Min = beta2Min;
        this.beta2Max = beta2Max;
        Pair<Double, Double>[] bounds = new Pair[5];
        bounds[0] = new Pair<Double, Double>(a2Min, a2Max);
        bounds[1] = new Pair<Double, Double>(b2Min, b2Max);
        bounds[2] = new Pair<Double, Double>(c2Min, c2Max);
        bounds[3] = new Pair<Double, Double>(alpha2Min, alpha2Max);
        bounds[4] = new Pair<Double, Double>(beta2Min, beta2Max);
        this.creatureNumber = creatureNumber;

//        initialCreatures = new ArrayList<Creature>();
        initialCreatures = new Creature[creatureNumber];

        for (int i = 0; i < creatureNumber; i++) {
            initialCreatures[i] = CreatureOperations.generateCreature(bounds);
//            initialCreatures.add(creature);
        }
    }
}
