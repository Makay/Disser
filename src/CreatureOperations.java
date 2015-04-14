import javafx.util.Pair;
import matlabcontrol.*;

import java.io.InputStream;
import java.util.*;

/**
 * Created by 777 on 07.04.2015.
 */
public final class CreatureOperations {
    /** generates Creature out of array of parameters's bounds
     * @param bounds
     * @return
     * @throws Exception
     */
    public static Creature generateCreature(Pair<Double, Double>[] bounds) {
        double[] params = new double[5];
        for (int i = 0; i < 5; i++) {
            params[i] = Math.random() * (bounds[i].getValue() - bounds[i].getKey()) + bounds[i].getKey();
        }
        return new Creature(params[0], params[1], params[2], params[3], params[4]);
    }

    public static Pair<Creature, Creature> recombination(Creature creature0, Creature creature1, Pair<Double, Double>[] bounds) throws MatlabConnectionException, MatlabInvocationException {
        Pair<MatlabProxy, Properties> connection = MatlabOperations.openConnection();
        MatlabProxy proxy = connection.getKey();
        String path = connection.getValue().getProperty("filepath");

        proxy.eval("addpath('" + path + "')");

        Object[] resultCreatures = callMatlabRecombination(proxy, creature0, creature1, bounds);
        double[] childParams0 = (double[]) resultCreatures[0];
        double[] childParams1 = (double[]) resultCreatures[1];

        Creature child0 = new Creature(childParams0[0], childParams0[1], childParams0[2], childParams0[3], childParams0[4]);
        Creature child1 = new Creature(childParams1[0], childParams1[1], childParams1[2], childParams1[3], childParams1[4]);

        proxy.eval("rmpath('" + path + "')");
        proxy.disconnect();
        return new Pair<Creature, Creature>(child0, child1);
    }

    private static Object[] callMatlabRecombination(MatlabProxy proxy,
                                                    Creature creature0,
                                                    Creature creature1,
                                                    Pair<Double, Double>[] bounds) throws MatlabInvocationException {
        return proxy.returningFeval("recombination", 2, creature0.getA2(), creature0.getB2(), creature0.getC2(),
                creature0.getAlpha2(), creature0.getBeta2(), creature1.getA2(), creature1.getB2(), creature1.getC2(),
                creature1.getAlpha2(), creature1.getBeta2(), bounds[0].getKey(), bounds[0].getValue(), bounds[1].getKey(), bounds[1].getValue(),
                bounds[2].getKey(), bounds[2].getValue(), bounds[3].getKey(), bounds[3].getValue(), bounds[4].getKey(), bounds[4].getValue());
    }

    private static Object[] callMatlabMutate(MatlabProxy proxy, Creature creature0, Creature creature1, Pair<Double, Double>[] bounds) throws MatlabInvocationException {
        return proxy.returningFeval("make_children", 2, creature0.getA2(), creature0.getB2(), creature0.getC2(),
                creature0.getAlpha2(), creature0.getBeta2(), creature1.getA2(), creature1.getB2(), creature1.getC2(),
                creature1.getAlpha2(), creature1.getBeta2(), bounds[0].getKey(), bounds[0].getValue(), bounds[1].getKey(), bounds[1].getValue(),
                bounds[2].getKey(), bounds[2].getValue(), bounds[3].getKey(), bounds[3].getValue(), bounds[4].getKey(), bounds[4].getValue());
    }

    public static Pair<Creature, Creature> makeChildren(Creature creature0, Creature creature1, Pair<Double, Double>[] bounds) throws MatlabConnectionException, MatlabInvocationException {
        Pair<MatlabProxy, Properties> connection = MatlabOperations.openConnection();
        MatlabProxy proxy = connection.getKey();
        String path = connection.getValue().getProperty("filepath");

        proxy.eval("addpath('" + path + "')");

        Object[] resultCreatures = callMatlabMutate(proxy, creature0, creature1, bounds);
        double[] childParams0 = (double[]) resultCreatures[0];
        double[] childParams1 = (double[]) resultCreatures[1];

        Creature child0 = new Creature(childParams0[0], childParams0[1], childParams0[2], childParams0[3], childParams0[4]);
        Creature child1 = new Creature(childParams1[0], childParams1[1], childParams1[2], childParams1[3], childParams1[4]);

        proxy.eval("rmpath('" + path + "')");
        proxy.disconnect();
        return new Pair<Creature, Creature>(child0, child1);
    }

    public static double calculateFitness(Creature creature) {
        // insert some matlab code here
        // proxy etc.
        return 0.0;
    }

    private static Pair<Creature, Creature> fight(Creature creature0, Creature creature1,
                                                 Creature creature2, Creature creature3,
                                                 Pair<Double, Double>[] bounds) throws MatlabConnectionException, MatlabInvocationException {
        List<Creature> creatures = Arrays.asList(creature0, creature1, creature2, creature3);
        Collections.shuffle(creatures);

        Creature parent0 = battleOfTwo(creatures.get(0), creatures.get(1));
        Creature parent1 = battleOfTwo(creatures.get(2), creatures.get(3));

        Pair<Creature, Creature> result = makeChildren(parent0, parent1, bounds);
        return result;
    }

    private static Creature battleOfTwo(Creature creature0, Creature creature1) {
        if (calculateFitness(creature0) >= calculateFitness(creature1)) {
            return creature0;
        } else {
            return creature1;
        }
    }
    public static Creature[] formGenerationUnparallel(Creature[] oldOne, Pair<Double, Double>[] bounds) throws MatlabInvocationException, MatlabConnectionException {
        int creatureNumber = oldOne.length;
        if (creatureNumber < 4) {
            throw new IllegalArgumentException("Not enough creatures in population");
        }
        int stepNumber = creatureNumber / 2;

        Integer[] numbers = new Integer[creatureNumber];
        for (int i = 0; i < creatureNumber; i++) {
            numbers[i] = i;
        }
        List<Integer> list = Arrays.asList(numbers);

        Creature[] newOne = new Creature[creatureNumber];

        for (int i = 0; i < stepNumber; i++) {
            Collections.shuffle(list);
            Pair<Creature, Creature> pair = fight(oldOne[list.get(0)], oldOne[list.get(1)],
                    oldOne[list.get(2)], oldOne[list.get(3)], bounds);
            newOne[2 * i] = pair.getKey();
            newOne[2 * i + 1] = pair.getValue();
        }
        return newOne;
    }
}
