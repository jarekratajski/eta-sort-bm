/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import eta.example.MyExportedClass;
import io.vavr.collection.List;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

public class MyBenchmark {
    private static final int SIZE = 10000;
    private static final int CHECKINDEX = SIZE - 33;

    @State(Scope.Thread)
    public static class MyState {

        @Setup(Level.Trial)
        public void doSetup() {
            System.out.println("creating random array");
            vqsort = new DataGenerator().init(new Random(777), SIZE);
            checkElement = vqsort.checkRun().get(CHECKINDEX);
            System.out.println("Done setup");
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            System.out.println("Do TearDown");
        }

        InputData vqsort;
        int checkElement;

    }


    @Benchmark
    public void testVavrNaive(MyState state, Blackhole blackhole) {

        List<Integer> result = VavrNainveSort.qsort(state.vqsort.elementsAsList());
        if (result.get(CHECKINDEX) != state.checkElement) {
            throw new IllegalStateException("wrong computation");
        }
    }

    @Benchmark
    public void testEtaNaive(MyState state, Blackhole blackhole) {

        int[] result = MyExportedClass.sort(state.vqsort.elementsAsArray());

        if (result[CHECKINDEX] != state.checkElement) {
            throw new IllegalStateException("wrong computation");
        }
    }

    @Benchmark
    public void testEtaVector(MyState state, Blackhole blackhole) {

        int[] result = MyExportedClass.sortv(state.vqsort.elementsAsArray());

        if (result[CHECKINDEX] != state.checkElement) {
            throw new IllegalStateException("wrong computation");
        }
    }



    @Benchmark
    public void testVavrFast(MyState state, Blackhole blackhole) {

        final List<Integer> result = List.ofAll(state.vqsort.elementsAsArray()).sorted();

        if (result.get(CHECKINDEX) != state.checkElement) {
            throw new IllegalStateException("wrong computation");
        }
    }





}
