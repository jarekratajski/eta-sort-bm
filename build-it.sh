#!/bin/bash
cd eta-sort
etlas install
etlas configure --enable-uberjar-mode
etlas build
mvn install:install-file -Dfile=dist/build/eta-sort/eta-sort.jar  -DgroupId=pl.setblack.eta -DartifactId=eta-sort -Dpackaging=jar -Dversion=1.0-SNAPSHOT
cd ..
mvn clean package
./benchmark.sh