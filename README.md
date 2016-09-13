# Clowncar Performance Benchmark

## Dependencies

### Install [Blosc] (https://github.com/Blosc/c-blosc)

* Untar latest release
* make a build directory
* cmake -DCMAKE\_INSTALL\_PREFIX=your\_install\_prefix\_directory ..
* cmake --build .
* ctest
* ctest --build . --target install

### Install [Clowncar] (https://github.com/dlmarion/clowncar)

* git clone git@github.com:dlmarion/clowncar.git
* mvn clean install

## Running the benchmarks

* export LD\_LIBRARY\_PATH=your\_install\_prefix\_directory
* mvn clean package
* java -jar target/benchmarks.jar
