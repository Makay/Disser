import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

class CreatureCallable implements Callable<Pair<Creature, Creature>> {

    private final Creature[] creatureList;
    private final Pair<Double, Double>[] bounds;

    public CreatureCallable(Creature[] creatureList, Pair<Double, Double>[] bounds) {
        this.creatureList = creatureList;
        this.bounds = bounds;
    }

    @Override
    public Pair<Creature, Creature> call() throws Exception {
        int creatureNumber = creatureList.length;
        Integer[] numbers = new Integer[creatureNumber];
        for (int i = 0; i < creatureNumber; i++) {
            numbers[i] = i;
        }
        List<Integer> list = Arrays.asList(numbers);
        Collections.shuffle(list);

        return CreatureOperations.fight(creatureList[list.get(0)], creatureList[list.get(1)],
                creatureList[list.get(2)], creatureList[list.get(3)], bounds);
    }
}
