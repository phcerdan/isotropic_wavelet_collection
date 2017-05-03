
using namespace std;

void buildFullSFpyr2(const vector< vector< float > >, vector< vector< vector <float > > >&, int, int, int, int);
void reconFullSFpyr2(vector< vector< float > >&, const vector< vector< vector <float > > >, int, int, int, int);
void size_filters(size_t **, size_t **, size_t, size_t, int, int);
void filters(float **, float **, size_t *[2], size_t *[2], size_t, size_t, int, int);
void analysisHb(float **, float *, float **, float **, size_t *[2], size_t *[2], size_t, size_t, int, int);
void synthesisHb(float *,float **, float **, float **, size_t *[2], size_t *[2],size_t,size_t, int, int);
float *high_filters(size_t, size_t, float);
float *low_filters(size_t, size_t, float);
float *steerable_filters(size_t, size_t, int, int);
fftwf_complex *pointwise_complexfloat_multiplication(fftwf_complex *, fftwf_complex *, float *, size_t);
float compute_alpha(int);
void fftshift(vector< vector< float > >&);
void do_fft(fftwf_complex *, float *, size_t , size_t);
void do_ifft(fftwf_complex *, float *, size_t, size_t);
void fft2(vector< vector< float > >&, const vector< vector< float > >);
void ifft2(vector< vector< float > >&, const vector< vector< float > >);

