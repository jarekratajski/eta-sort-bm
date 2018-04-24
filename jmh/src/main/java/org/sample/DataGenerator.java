package org.sample;

import io.vavr.collection.List;

import java.util.Random;

public class DataGenerator {

    public InputData init(Random rnd, int size ) {
        final int[] arr = new int[size];
        for  ( int i = 0; i< arr.length ;  i++) {
            arr[i] = rnd.nextInt();
        }
        return new QSort(arr);
    }
}

class QSort implements InputData {

    private final int[] internal;
    private final List<Integer> list;

    QSort(int[] array) {

        this.internal = array;
        this.list = List.ofAll(array);
    }



    @Override
    public int[] elementsAsArray() {
        return this.internal;
    }

    @Override
    public List<Integer> elementsAsList() {
        return this.list;
    }

    @Override
    public List<Integer> checkRun() {
        return elementsAsList().sorted();
    }


}
