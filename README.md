# matrix-multiplication

__Note__: dependency on Apache commons cli 1.4

### Usage
```sh
echo "Compiling..."

find -name "*.java" > sources.txt

paths_to_apache_jars="./Apache/commons-cli-1.4.jar:./Apache/commons-cli-1.4-javadoc.jar:./Apache/commons-cli-1.4-sources.jar:./Apache/commons-cli-1.4"

javac -cp .:$paths_to_apache_jars @sources.txt

echo "Running..."

java -cp .:src:$paths_to_apache_jars com.ozhi.matrixMultiplication.Main
```

### Command line args

* `-i` input file (when providing matrices from file)

* `-m`
* `-n`
* `-k` matrix dimensions when generating random martrices

__Note__: Either `-i` or all three of `-m`, `-n` and `-k` should be provided 

* `-o` output file

* `-t` maxNumber of threads to use

* `-q` quiet mode
