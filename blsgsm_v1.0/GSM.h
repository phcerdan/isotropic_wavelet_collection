/**
 * @file GSM.h
 * @brief GSM.cpp header file
 * @author Boshra Rajaei <bo.rajaei@gmail.com>
 *
 * Copyright (c) 2013, Boshra Rajaei
 * All rights reserved.
 *
 * This program is free software: you can use, modify and/or
 * redistribute it under the terms of the simplified BSD License. You
 * should have received a copy of this license along this program. If
 * not, see <http://www.opensource.org/licenses/bsd-license.html>.
 */

#include <fftw3.h>

using namespace std;

void denoi_BLS_GSM(vector< vector< float > >&, float, vector< vector< float > >, int[], int, int, int, int);
void decomp_reconst_full(vector< vector< float > >&, int, int, int[], vector< vector< float > >, int);
void denoi_BLS_GSM_band(vector< vector< float > >&, vector< vector< float > >, int[], vector< vector< float > >, vector< vector< float > >, int);
