package org.sample;

import io.vavr.collection.List;

public class VavrNainveSort {
    public static List<Integer> qsort(List<Integer> input) {
        if (!input.isEmpty()) {
            final int middle =  input.head();
            final List<Integer> left = input.tail().filter( x -> x <= middle);
            final List<Integer> right = input.tail().filter( x -> x > middle);
            return qsort(left).appendAll(qsort(right).prepend(middle));
        } else {
            return input;
        }
    }
}
