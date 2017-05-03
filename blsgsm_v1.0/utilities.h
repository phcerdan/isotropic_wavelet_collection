
using namespace std;

#define min(x,y)    ((x > y) ? (x) : (y))

const float DCTbasis3x3[3][3] =
{
	{	0.5773502588272094726562500000000000000000,
		0.5773502588272094726562500000000000000000,
		0.5773502588272094726562500000000000000000,	},

	{	0.7071067690849304199218750000000000000000,
		0.0000000000000000000000000000000000000000,
		-0.7071067690849304199218750000000000000000, },

	{
		0.4082483053207397460937500000000000000000,
		-0.8164966106414794921875000000000000000000,
		0.4082483053207397460937500000000000000000 	}
};

void bound_extension(vector< vector< float > >&, int, int, int);
void ColorTransform(vector< vector< vector< float > > >&, int, int, int);
void Mat2Array(const vector< vector< float > >, float*);
void Array2Mat(const float*, vector< vector< float > >&);
void resizedelta(vector< vector< float > >&, int, int);
float meansqr(const vector< vector< float > >);
void multiply(float**, float**, float**, int, int, int );
void transp(float**, float**, int);
void addnoise(float*, int, float);
void clearMatrix(vector< vector< float > >&);
void clearMatrix(vector< vector< vector< float > > >&);
void cov(vector< vector< float > > sub, vector< vector< float > > subp, float **C, int block[], int prnt);
void symroot(float **mat, float **S, int N);
void Make_SemiDefinite(float **mat, int N);
void boundary_handling(vector< vector< float > >& y, float **p_lz, float *zi, int block[], int nsamp_z, float sx2, float sig2);
void PrintMeasurements(const float *inputImg, const float *denoisedImg, int N);
void expand(vector< vector< float > >& mat);
