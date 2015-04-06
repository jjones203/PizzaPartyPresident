#!/bin/sh
mkdir out
javac -classpath ./lib/rsyntaxtextarea.jar:./lib/commons-csv-1.1.jar:. @sourceArgs.txt -d ./out
