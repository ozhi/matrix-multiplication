# matrix-multiplication

__Note__: dependency on Apache commons cli 1.4

This program aims to demonstrate the power of __parallel computing__. The task at hand is matrix multiplication. Through command line arguments you can provide two matrices and the max number of threads to be used and see metrics of how fast the computation was.

For more details on the task, solution and test data, [__see the documentation__](./bozhin-katsarski-81291-spo-documentation.pdf) (bulgarian).

### Usage
```sh
echo "Compiling..."

find -name "*.java" > sources.txt

paths_to_apache_jars="./Apache/commons-cli-1.4.jar:./Apache/commons-cli-1.4-javadoc.jar:./Apache/commons-cli-1.4-sources.jar:./Apache/commons-cli-1.4"

javac -cp .:$paths_to_apache_jars @sources.txt

echo "Running..."

java -cp .:src:$paths_to_apache_jars \
com.ozhi.matrixMultiplication.Main \
-m 1000 -n 1000 -k 1000 \
-t 32 \
-o matrix-multiplication.out
```

### Command line args

When providing matrices from input file:
* `-i` input file (when providing matrices from file)

When generating random matrices from provided dimensions:
* `-m` first matrix rows
* `-n` first matrix cols == second matrrix rows
* `-k` second matrix cols

__Note__: Either `-i` or all three of (`-m`, `-n` and `-k`) should be provided. 

* `-o` output file

* `-t` maxNumber of threads to use

* `-q` quiet mode
