# Makefile created with Create Makefile.sh 12/03/2012

COPT      = -O99
CFLAGS    = $(COPT) -fopenmp 
CSTRICT   = -Wall -Wextra -ansi
CXXOPT    = -O99
CXXFLAGS  = $(CXXOPT) -fopenmp
CXXSTRICT = -Wall -Wextra -ansi
LDFLAGS   = -lpng -lfftw3f -lgomp -lm
EXEC      = blsgsm

default: $(EXEC)
all: $(EXEC)

# ------- C files -------
./Matrix.o: ./Matrix.c ./Matrix.h
	$(CC) $(CFLAGS) -c ./Matrix.c -o ./Matrix.o

./mt19937ar.o: ./mt19937ar.c ./mt19937ar.h
	$(CC) $(CFLAGS) -c ./mt19937ar.c -o ./mt19937ar.o

./io_png.o: ./io_png.c ./io_png.h
	$(CC) $(CFLAGS) -c ./io_png.c -o ./io_png.o

./eig3.o: ./eig3.c ./eig3.h
	$(CC) $(CFLAGS) -c ./eig3.c -o ./eig3.o

# ------- C++ files -------
./main.o: ./main.cpp
	$(CXX) $(CXXFLAGS) -c ./main.cpp -o ./main.o

./utilities.o: ./utilities.cpp ./utilities.h
	$(CXX) $(CXXFLAGS) -c ./utilities.cpp -o ./utilities.o

./GSM.o: ./GSM.cpp ./GSM.h
	$(CXX) $(CXXFLAGS) -c ./GSM.cpp -o ./GSM.o

./steerable_pyramid.o: ./steerable_pyramid.cpp ./steerable_pyramid.h
	$(CXX) $(CXXFLAGS) -c ./steerable_pyramid.cpp -o ./steerable_pyramid.o

# ------- Main -------
$(EXEC):  ./Matrix.o ./mt19937ar.o ./io_png.o ./eig3.o ./main.o ./utilities.o ./GSM.o ./steerable_pyramid.o
	$(CXX)  ./Matrix.o ./mt19937ar.o ./io_png.o ./eig3.o ./main.o ./utilities.o ./GSM.o ./steerable_pyramid.o $(LDFLAGS) -o $(EXEC)

lint: 
	$(MAKE) CFLAGS="$(CFLAGS) $(CSTRICT)" CXXFLAGS="$(CXXFLAGS) $(CXXSTRICT)"

clean: 
	rm -f *.o

distclean: clean
	rm -f $(EXEC)

