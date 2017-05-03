 /*CCMATH mathematics library source code.
 *
 *  Copyright (C)  2000   Daniel A. Atkinson    All rights reserved.
 *  This code may be redistributed under the terms of the GNU library
 *  public license (LGPL). ( See the lgpl.license file for details.)
 * ------------------------------------------------------------------------
 */

#include <stdlib.h>
#include <math.h>
#include "Matrix.h"

void Mat2Array(const float **mat, float* arr, int Ny, int Nx)
{
    int i, j;
    for(i=0;i<Ny ;i++)
    {
        for(j=0;j<Nx;j++)
        {
            arr[i*Nx+j] = mat[i][j];
        }
    }
}

void Array2Mat(const float* arr, float** mat, int Ny, int Nx)
{
    int i, j;
    for(i=0;i<Ny ;i++)
    {
        for(j=0;j<Nx;j++)
        {
            mat[i][j] = arr[i*Nx+j];
        }
    }
}

void housev(float *a,float *d,float *dp,int n)
{ float sc,x,y,h;
  int i,j,k,m,e;
  float *qw,*qs,*pc,*p;
  qs=(float *)calloc(n,sizeof(float));
  for(j=0,pc=a; j<n-2 ;++j,pc+=n+1){
    m=n-j-1;
    for(i=1,sc=0.; i<=m ;++i) sc+=pc[i]*pc[i];
    if(sc>0.){ sc=sqrt(sc);
      if((x= *(pc+1))<0.){ y=x-sc; h=1./sqrt(-2.*sc*y);}
      else{ y=x+sc; h=1./sqrt(2.*sc*y); sc= -sc;}
      for(i=0,qw=pc+1; i<m ;++i){
        qs[i]=0.; if(i) qw[i]*=h; else qw[i]=y*h;
       }
      for(i=0,e=j+2,p=pc+n+1,h=0.; i<m ;++i,p+=e++){
        qs[i]+=(y=qw[i])* *p++;
	for(k=i+1; k<m ;++k){
          qs[i]+=qw[k]* *p; qs[k]+=y* *p++;
         }
        h+=y*qs[i];
       }
      for(i=0; i<m ;++i){
	qs[i]-=h*qw[i]; qs[i]+=qs[i];
       }
      for(i=0,e=j+2,p=pc+n+1; i<m ;++i,p+=e++){
        for(k=i; k<m ;++k) *p++ -=qw[i]*qs[k]+qs[i]*qw[k];
       }
     }
    d[j]= *pc; dp[j]=sc;
   }
  d[j]= *pc; dp[j]= *(pc+1); d[j+1]= *(pc+=n+1);
  free(qs);
  for(i=0,m=n+n,p=pc; i<m ;++i) *p-- =0.;
  *pc=1.; *(pc-=n+1)=1.; qw=pc-n;
  for(m=2; m<n ;++m,qw-=n+1){
    for(j=0,p=pc,*pc=1.; j<m ;++j,p+=n){
      for(i=0,qs=p,h=0.; i<m ;) h+=qw[i++]* *qs++;
      for(i=0,qs=p,h+=h; i<m ;) *qs++ -=h*qw[i++];
     }
    for(i=0,p=qw+m; i<n ;++i) *(--p)=0.;
    *(pc-=n+1)=1.;
   }
}

int qrevec(float *ev,float *evec,float *dp,int n)
{ float cc,sc,d,x,y,h,tzr=1.e-15;
  int i,j,k,m,mqr=8*n;
  float *p;
  for(j=0,m=n-1;;++j){
    while(1){ if(m<1) return 0; k=m-1;
      if(fabs(dp[k])<=fabs(ev[m])*tzr) --m;
      else{ x=(ev[k]-ev[m])/2.; h=sqrt(x*x+dp[k]*dp[k]);
        if(m>1 && fabs(dp[m-2])>fabs(ev[k])*tzr) break;
	    if((cc=sqrt((1.+x/h)/2.))!=0.) sc=dp[k]/(2.*cc*h); else sc=1.;
        x+=ev[m]; ev[m--]=x-h; ev[m--]=x+h;
        for(i=0,p=evec+n*(m+1); i<n ;++i,++p){
	      h=p[0]; p[0]=cc*h+sc*p[n]; p[n]=cc*p[n]-sc*h;
         }
       }
     }
    if(j>mqr) return -1;
    if(x>0.) d=ev[m]+x-h; else d=ev[m]+x+h;
    cc=1.; y=0.; ev[0]-=d;
    for(k=0; k<m ;++k){
      x=ev[k]*cc-y; y=dp[k]*cc; h=sqrt(x*x+dp[k]*dp[k]);
      if(k>0) dp[k-1]=sc*h;
      ev[k]=cc*h; cc=x/h; sc=dp[k]/h; ev[k+1]-=d; y*=sc;
      ev[k]=cc*(ev[k]+y)+ev[k+1]*sc*sc+d;
      for(i=0,p=evec+n*k; i<n ;++i,++p){
        h=p[0]; p[0]=cc*h+sc*p[n]; p[n]=cc*p[n]-sc*h;
       }
     }
    ev[k]=ev[k]*cc-y; dp[k-1]=ev[k]*sc; ev[k]=ev[k]*cc+d;
   }
  return 0;
}

void trnm(float *a,int n)
{ float s,*p,*q;
  int i,j,e;
  for(i=0,e=n-1; i<n-1 ;++i,--e,a+=n+1){
    for(p=a+1,q=a+n,j=0; j<e ;++j){
      s= *p; *p++ = *q; *q=s; q+=n;
     }
   }
}

void sorteig(float **eigvec, float *eigval, int n)
{
    int max1[n],i,j;
    float eigvalTemp[n];
    float eigvecTemp[n][n];
    for(i=0;i<n;i++)
    {
        eigvalTemp[i]=eigval[i];
        for(j=0;j<n;j++)
        {
            eigvecTemp[i][j]=eigvec[i][j];
        }
    }
    for(i=0;i<n;i++)
    {
        max1[i] = 0;
        for(j=0;j<n;j++)
            if(eigval[j]>eigval[max1[i]])
            {
                max1[i] = j;
            }
        eigval[max1[i]] = -10000;
    }
    for(i=0;i<n;i++)
    {
        eigval[i] = eigvalTemp[max1[i]];
        for(j=0;j<n;j++)
            eigvec[j][i] = eigvecTemp[j][max1[i]];
    }
}

void eigen(float **mat,float **eigvec, float **eigval,int n)
{ float *dp, *ev;
    float *a;
    int i, j;
  dp=(float *)calloc(n,sizeof(float));
  ev=(float *)calloc(n,sizeof(float));
  a=(float *)calloc(n*n,sizeof(float));
  Mat2Array((const float **) mat, a, n, n);
  housev(a,ev,dp,n);
  qrevec(ev,a,dp,n); trnm(a,n);
  Array2Mat(a, eigvec,n, n);
  sorteig(eigvec, ev, n);

  for(i=0;i<n;i++)
  {
      for(j=0;j<n;j++)
      {
        if(i==j)
            eigval[i][i] = ev[i];
        else
            eigval[i][j] = 0;

      }

  }
  free(dp);

}

int minv(float **inMat, float **outMat, int n)
{
    float *a = (float *)malloc(n*n*sizeof(int));
    Mat2Array((const float **) inMat, a, n, n);
    int lc,*le; float s,t,tq=0.,zr=1.e-15;
  float *pa,*pd,*ps,*p,*q,*q0;
  int i,j,k,m;
  le=(int *)malloc(n*sizeof(int));
  q0=(float *)malloc(n*sizeof(float));
  for(j=0,pa=pd=a; j<n ;++j,++pa,pd+=n+1){
    if(j>0){
      for(i=0,q=q0,p=pa; i<n ;++i,p+=n) *q++ = *p;
      for(i=1; i<n ;++i){ lc=i<j?i:j;
        for(k=0,p=pa+i*n-j,q=q0,t=0.; k<lc ;++k) t+= *p++ * *q++;
      	q0[i]-=t;
       }
      for(i=0,q=q0,p=pa; i<n ;++i,p+=n) *p= *q++;
     }
    s=fabs(*pd); lc=j;
    for(k=j+1,ps=pd; k<n ;++k){
      if((t=fabs(*(ps+=n)))>s){ s=t; lc=k;}
     }
    tq=tq>s?tq:s; if(s<zr*tq){ free(le-j); free(q0); return -1;}
    *le++ =lc;
    if(lc!=j){
      for(k=0,p=a+n*j,q=a+n*lc; k<n ;++k){
        t= *p; *p++ = *q; *q++ =t;
       }
     }
    for(k=j+1,ps=pd,t=1./ *pd; k<n ;++k) *(ps+=n)*=t;
    *pd=t;
   }
  for(j=1,pd=ps=a; j<n ;++j){
    for(k=0,pd+=n+1,q= ++ps; k<j ;++k,q+=n) *q*= *pd;
   }
  for(j=1,pa=a; j<n ;++j){ ++pa;
    for(i=0,q=q0,p=pa; i<j ;++i,p+=n) *q++ = *p;
    for(k=0; k<j ;++k){ t=0.;
      for(i=k,p=pa+k*n+k-j,q=q0+k; i<j ;++i) t-= *p++ * *q++;
      q0[k]=t;
     }
    for(i=0,q=q0,p=pa; i<j ;++i,p+=n) *p= *q++;
   }
  for(j=n-2,pd=pa=a+n*n-1; j>=0 ;--j){ --pa; pd-=n+1;
    for(i=0,m=n-j-1,q=q0,p=pd+n; i<m ;++i,p+=n) *q++ = *p;
    for(k=n-1,ps=pa; k>j ;--k,ps-=n){ t= -(*ps);
      for(i=j+1,p=ps,q=q0; i<k ;++i) t-= *++p * *q++;
      q0[--m]=t;
     }
    for(i=0,m=n-j-1,q=q0,p=pd+n; i<m ;++i,p+=n) *p= *q++;
   }
  for(k=0,pa=a; k<n-1 ;++k,++pa){
    for(i=0,q=q0,p=pa; i<n ;++i,p+=n) *q++ = *p;
    for(j=0,ps=a; j<n ;++j,ps+=n){
      if(j>k){ t=0.; p=ps+j; i=j;}
      else{ t=q0[j]; p=ps+k+1; i=k+1;}
      for(; i<n ;) t+= *p++ *q0[i++];
      q0[j]=t;
     }
    for(i=0,q=q0,p=pa; i<n ;++i,p+=n) *p= *q++;
   }
  for(j=n-2,le--; j>=0 ;--j){
    for(k=0,p=a+j,q=a+ *(--le); k<n ;++k,p+=n,q+=n){
      t=*p; *p=*q; *q=t;
     }
   }
  free(le); free(q0);

  Array2Mat(a, outMat, n, n);

  return 0;
}
