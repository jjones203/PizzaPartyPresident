#/bin/sh
mkdir out
javac -classpath ./lib/rsyntaxtextarea.jar:. @sourceArgs.txt -d ./out 
