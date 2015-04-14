/**
 * Created by 777 on 07.04.2015.
 */
import javafx.util.Pair;
import matlabcontrol.*;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class LoopCounter {
    public static void main(String[] args) throws MatlabConnectionException, MatlabInvocationException {
//        ThreadGroup group = new ThreadGroup("main");

//        Pair<Double, Double>[] bounds = new Pair[5];
//        bounds[0] = new Pair<Double, Double>(-15.0, -5.0);
//        bounds[1] = new Pair<Double, Double>(1.0, 3.0);
//        bounds[2] = new Pair<Double, Double>(0.0, 1.0);
//        bounds[3] = new Pair<Double, Double>(-100.0, -50.0);
//        bounds[4] = new Pair<Double, Double>(0.0, 0.5);
//
//        Creature c0 = CreatureOperations.generateCreature(bounds);
//        Creature c1 = CreatureOperations.generateCreature(bounds);
//        System.out.println(c0);
//        System.out.println(c1);
//
//        Pair<Creature, Creature> children = CreatureOperations.makeChildren(c0, c1, bounds);
//
//        System.out.println(children.getKey());
//        System.out.println(children.getValue());
        int creatureNumber = 6;
        int stepNumber = creatureNumber / 2;
        Integer[] numbers = new Integer[creatureNumber];
        for (int i = 0; i < creatureNumber; i++) {
            numbers[i] = i;
        }
//        System.out.println(Arrays.toString(numbers));
        Integer[] nums = new Integer[creatureNumber];
        for (int i = 0; i < stepNumber; i++) {
            nums[2 * i] = 2 * i;
            nums[2 * i + 1] = 2 * i + 1;
        }
        System.out.println(Arrays.toString(nums));
        for (int i = 0; i < creatureNumber; i = i + 2) {
            nums[i] = i;
            nums[i + 1] = i + 1;
        }
        System.out.println(Arrays.toString(nums));
        return;
    }
}
