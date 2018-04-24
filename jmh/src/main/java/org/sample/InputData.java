package org.sample;

import io.vavr.collection.List;

/**
 * Helper interface input data as list or array.
 */
public interface InputData {

    int[] elementsAsArray();

    List<Integer> elementsAsList();

    List<Integer> checkRun();
}
