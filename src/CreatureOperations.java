import javafx.util.Pair;
import matlabcontrol.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

final class CreatureOperations {
    /** generates Creature out of array of parameters bounds
     * @param bounds - bounds of parameters values
     * @return - generated creature
     */
    public static Creature generateCreature(Pair<Double, Double>[] bounds) {
        double[] params = new double[5];
        for (int i = 0; i < 5; i++) {
            params[i] = Math.random() * (bounds[i].getValue() - bounds[i].getKey()) + bounds[i].getKey();
        }
        return new Creature(params[0], params[1], params[2], params[3], params[4]);
    }

    /**
     * Recombination of creatures
     * @param creature0 - 1st parent
     * @param creature1 - 2nd parent
     * @param bounds - bounds of parameters values
     * @return recombinated pair of children
     * @throws MatlabConnectionException
     * @throws MatlabInvocationException
     */
    /*
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
    */
// --Commented out by Inspection START (23.04.2015 13:13):
//    /**
//     * We don't need this function anymore
//     * Calling matlab function recombination
//     * @param proxy - matlab proxy
//     * @param creature0 - 1st parent
//     * @param creature1 - 2nd parent
//     * @param bounds - bounds of parameters values
//     * @return - recombinated creatures
//     * @throws MatlabInvocationException
//     */
//    private static Object[] callMatlabRecombination(MatlabProxy proxy, Creature creature0, Creature creature1, Pair<Double, Double>[] bounds) throws MatlabInvocationException {
//        return proxy.returningFeval("recombination", 2, creature0.getA2(), creature0.getB2(), creature0.getC2(),
//                creature0.getAlpha2(), creature0.getBeta2(), creature1.getA2(), creature1.getB2(), creature1.getC2(),
//                creature1.getAlpha2(), creature1.getBeta2(), bounds[0].getKey(), bounds[0].getValue(), bounds[1].getKey(), bounds[1].getValue(),
//                bounds[2].getKey(), bounds[2].getValue(), bounds[3].getKey(), bounds[3].getValue(), bounds[4].getKey(), bounds[4].getValue());
//    }
// --Commented out by Inspection STOP (23.04.2015 13:13)

    /**
     * Calling matlab function make_children
     * @param proxy - matlab proxy
     * @param creature0 - 1st parent
     * @param creature1 - 2nd parent
     * @param bounds - bounds of parameters values
     * @return - children (in special matlabcontrol form)
     * @throws MatlabInvocationException
     */
    private static Object[] callMatlabMakeChildren(MatlabProxy proxy, Creature creature0, Creature creature1, Pair<Double, Double>[] bounds) throws MatlabInvocationException {
        return proxy.returningFeval("make_children", 2, creature0.getA2(), creature0.getB2(), creature0.getC2(),
                creature0.getAlpha2(), creature0.getBeta2(), creature1.getA2(), creature1.getB2(), creature1.getC2(),
                creature1.getAlpha2(), creature1.getBeta2(), bounds[0].getKey(), bounds[0].getValue(), bounds[1].getKey(), bounds[1].getValue(),
                bounds[2].getKey(), bounds[2].getValue(), bounds[3].getKey(), bounds[3].getValue(), bounds[4].getKey(), bounds[4].getValue());
    }
//    private static Object[] callMatlabRecombination() {
//        return callMatlabFunction("recombination");
//    }

//    private static Object[] callMatlabFunction(String functionName, MatlabProxy proxy,
//                                               Creature creature0,
//                                               Creature creature1,
//                                               Pair<Double, Double>[] bounds) throws MatlabInvocationException {
//        return proxy.returningFeval(functionName, 2, creature0.getA2(), creature0.getB2(), creature0.getC2(),
//                creature0.getAlpha2(), creature0.getBeta2(), creature1.getA2(), creature1.getB2(), creature1.getC2(),
//                creature1.getAlpha2(), creature1.getBeta2(), bounds[0].getKey(), bounds[0].getValue(), bounds[1].getKey(), bounds[1].getValue(),
//                bounds[2].getKey(), bounds[2].getValue(), bounds[3].getKey(), bounds[3].getValue(), bounds[4].getKey(), bounds[4].getValue());
//    }

    /**
     * Making of children out of parents (recombination & mutation)
     * @param creature0 - 1st parent
     * @param creature1 - 2nd parent
     * @param bounds - bounds of parameters values
     * @return - pair of children
     * @throws MatlabConnectionException
     * @throws MatlabInvocationException
     */
    private static Pair<Creature, Creature> makeChildren(Creature creature0, Creature creature1, Pair<Double, Double>[] bounds) throws MatlabConnectionException, MatlabInvocationException {
        Pair<MatlabProxy, Properties> connection = MatlabOperations.openConnection();
        MatlabProxy proxy = connection.getKey();
        String path = connection.getValue().getProperty("filepath");

        proxy.eval("addpath('" + path + "')");

        Object[] resultCreatures = callMatlabMakeChildren(proxy, creature0, creature1, bounds);
        double[] childParams0 = (double[]) resultCreatures[0];
        double[] childParams1 = (double[]) resultCreatures[1];

        Creature child0 = new Creature(childParams0[0], childParams0[1], childParams0[2], childParams0[3], childParams0[4]);
        Creature child1 = new Creature(childParams1[0], childParams1[1], childParams1[2], childParams1[3], childParams1[4]);

        proxy.eval("rmpath('" + path + "')");
        proxy.disconnect();
        return new Pair<Creature, Creature>(child0, child1);
    }

    /**
     * TODO Calculation of creatures' fitness using its amplitude function. Maybe can be put in Creature class
     * @param creature - input creature
     * @return - creatures' fitness
     */
    /*
    public static double calculateFitness(Creature creature) {
        // insert some matlab code here
        // proxy etc.
        return 0.0;
    }
    */

    /**
     * Making a pair of creatures out of four random ones
     * @param creature0 - 1st random creature
     * @param creature1 - 2nd random creature
     * @param creature2 - 3rd random creature
     * @param creature3 - 4th random creature
     * @param bounds - bounds of parameters values
     * @return - pair of creatures to put in new generation
     * @throws MatlabConnectionException
     * @throws MatlabInvocationException
     */
    static Pair<Creature, Creature> fight(Creature creature0, Creature creature1,
                                                 Creature creature2, Creature creature3,
                                                 Pair<Double, Double>[] bounds) throws MatlabConnectionException, MatlabInvocationException {
        List<Creature> creatures = Arrays.asList(creature0, creature1, creature2, creature3);
        Collections.shuffle(creatures);

        Creature parent0 = battleOfTwo(creatures.get(0), creatures.get(1));
        Creature parent1 = battleOfTwo(creatures.get(2), creatures.get(3));

        return makeChildren(parent0, parent1, bounds);
    }

    /**
     * TODO Comparing creatures. Comparison is based on creatures (equations, more precisely) amplitude function
     * @param creature0 - 1st input creature
     * @param creature1 - 2nd input creature
     * @return "better" creature (more likely its children would have more limit circle than other creatures' children)
     */
    private static Creature battleOfTwo(Creature creature0, Creature creature1) {
//        if (calculateFitness(creature0) >= calculateFitness(creature1)) {
//            return creature0;
//        } else {
//            return creature1;
//        }
        double tail = Math.random();
//        if (eagle > 0.5) {
//            return creature0;
//        }
//        else {
//            return creature1;
//        }
        return tail > 0.5 ? creature0 : creature1;
    }

    /**
     * Generation formation. Unparallel. Period.
     * @param oldCreatures - input creatures on this step
     * @param bounds - bounds of parameters values
     * @return - output generation
     * @throws MatlabInvocationException
     * @throws MatlabConnectionException
     */
    /*
    public static Creature[] formGenerationUnparallel(Creature[] oldCreatures, Pair<Double, Double>[] bounds) throws MatlabInvocationException, MatlabConnectionException {
        int creatureNumber = oldCreatures.length;
        if (creatureNumber < 4) {
            throw new IllegalArgumentException("Not enough creatures in population");
        }
        int stepNumber = creatureNumber / 2;

        Integer[] numbers = new Integer[creatureNumber];
        for (int i = 0; i < creatureNumber; i++) {
            numbers[i] = i;
        }
        List<Integer> list = Arrays.asList(numbers);

        Creature[] newCreatures = new Creature[creatureNumber];

        for (int i = 0; i < stepNumber; i++) {
            Collections.shuffle(list);
            Pair<Creature, Creature> pair = fight(oldCreatures[list.get(0)], oldCreatures[list.get(1)],
                    oldCreatures[list.get(2)], oldCreatures[list.get(3)], bounds);
            newCreatures[2 * i] = pair.getKey();
            newCreatures[2 * i + 1] = pair.getValue();
        }
        return newCreatures;
    }
    */
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
        List<CreatureCallable> creatureCallables = new ArrayList<CreatureCallable>();
        for (int i = 0; i < stepNumber; i++) {
            creatureCallables.add(new CreatureCallable(oldCreatures, bounds));
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
     * @throws ExecutionException
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
        List<CreatureCallable> creatureCallables = new ArrayList<CreatureCallable>();
        for (int i = 0; i < stepNumber; i++) {
            creatureCallables.add(new CreatureCallable(newCreatures, bounds));
        }
        List<Future<Pair<Creature, Creature>>> newGeneration = executor.invokeAll(creatureCallables);
        executor.shutdown();
        return newGeneration;
    }

// --Commented out by Inspection START (23.04.2015 13:13):
// --Commented out by Inspection START (23.04.2015 13:14):
////    /**
////     * TODO Building of amplitude function. Right here I need to obtain some info about creature (about its AF more precisely)
////     * @param creature - input creature
////     * @throws MatlabConnectionException
////     * @throws MatlabInvocationException
////     */
////    private static void poincareSmartDirection(Creature creature) throws MatlabConnectionException, MatlabInvocationException {
////        Pair<MatlabProxy, Properties> connection = MatlabOperations.openConnection();
////        MatlabProxy proxy = connection.getKey();
////        String path = connection.getValue().getProperty("filepath");
////
////        proxy.eval("addpath('" + path + "')");
////
////        callMatlabPoincareSmartDirection(proxy, creature);
////
////        proxy.eval("rmpath('" + path + "')");
////        proxy.disconnect();
////    }
//// --Commented out by Inspection STOP (23.04.2015 13:13)
    /**
     * TODO
     */
//    private static void callMatlabPoincareSmartDirection(MatlabProxy proxy, Creature creature) throws MatlabInvocationException {
//        proxy.feval("PSD", 0.1, 10000, 0, 0, 0.5, 1.e-15, creature.getA2(), creature.getB2(), creature.getC2(),
//                creature.getAlpha2(), creature.getBeta2());
//    }
// --Commented out by Inspection STOP (23.04.2015 13:14)
}
