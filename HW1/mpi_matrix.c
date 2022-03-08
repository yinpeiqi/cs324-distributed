#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>

#define MAT_SIZE 500

void brute_force_matmul(double mat1[MAT_SIZE][MAT_SIZE], double mat2[MAT_SIZE][MAT_SIZE], 
                        double res[MAT_SIZE][MAT_SIZE]) {
   /* matrix multiplication of mat1 and mat2, store the result in res */
    for (int i = 0; i < MAT_SIZE; ++i) {
        for (int j = 0; j < MAT_SIZE; ++j) {
            res[i][j] = 0;
            for (int k = 0; k < MAT_SIZE; ++k) {
                res[i][j] += mat1[i][k] * mat2[k][j];
            }
        }
    }
}

void p(double a[MAT_SIZE][MAT_SIZE]) {
    for (int i = 0; i < MAT_SIZE; i++) {
        for (int j = 0; j < MAT_SIZE; j++) printf("%.0f ", a[i][j]);
        printf("\n");
    }
    printf("\n");
}
int main(int argc, char *argv[])
{
   int rank;
   int mpiSize;
   double a[MAT_SIZE][MAT_SIZE],    /* matrix A to be multiplied */
       b[MAT_SIZE][MAT_SIZE],       /* matrix B to be multiplied */
       c[MAT_SIZE][MAT_SIZE],       /* result matrix C */
       bfRes[MAT_SIZE][MAT_SIZE];   /* brute force result bfRes */

   /* You need to intialize MPI here */
   MPI_Init(NULL, NULL);
   MPI_Comm_size(MPI_COMM_WORLD, &mpiSize);
   MPI_Comm_rank(MPI_COMM_WORLD, &rank);
   int num_row_per_proc = MAT_SIZE / mpiSize;

   if (rank == 0)
   {
      /* master */

      /* First, fill some numbers into the matrix */
      for (int i = 0; i < MAT_SIZE; i++)
         for (int j = 0; j < MAT_SIZE; j++)
            a[i][j] = i + j;
      for (int i = 0; i < MAT_SIZE; i++)
         for (int j = 0; j < MAT_SIZE; j++)
            b[i][j] = i + j;

      /* Measure start time */
      double start = MPI_Wtime();

      /* Send matrix data to the worker tasks */
      MPI_Bcast(b, MAT_SIZE * MAT_SIZE, MPI_DOUBLE, 0, MPI_COMM_WORLD);

      double *a_buf = malloc(sizeof(double) * num_row_per_proc * MAT_SIZE);
      MPI_Scatter(a, num_row_per_proc * MAT_SIZE, MPI_DOUBLE, a_buf, num_row_per_proc * MAT_SIZE, MPI_DOUBLE, 0, MPI_COMM_WORLD);

      // master compute
      double *c_buf = malloc(sizeof(double) * num_row_per_proc * MAT_SIZE);
      for (int i = 0; i < num_row_per_proc; ++i) {
          for (int j = 0; j < MAT_SIZE; ++j) {
              *(c_buf + i * MAT_SIZE + j) = 0;
              for (int k = 0; k < MAT_SIZE; ++k) {
                  *(c_buf + i * MAT_SIZE + j) += *(a_buf + i * MAT_SIZE + k) * b[k][j];
              }
          }
      }
      free(a_buf);

      /* Receive results from worker tasks */
      MPI_Gather(c_buf, num_row_per_proc * MAT_SIZE, MPI_DOUBLE, c, num_row_per_proc * MAT_SIZE, MPI_DOUBLE, 0, MPI_COMM_WORLD);

      /* Measure finish time */
      double finish = MPI_Wtime();
      printf("Done in %f seconds.\n", finish - start);

      /* Compare results with those from brute force */
      brute_force_matmul(a, b, bfRes);

      int correct = 1;
      for (int i = 0; i < MAT_SIZE; i++) {
         for(int j = 0; j < MAT_SIZE; j++) {
            if (bfRes[i][j] != c[i][j]) correct = 0;
         }
      }
      if (correct)
         printf("The result is correct!\n");
      else
         printf("The result is not correct!\n");
   }
   else
   {
      /* worker */
      /* Receive data from master and compute, then send back to master */
      MPI_Bcast(b, MAT_SIZE * MAT_SIZE, MPI_DOUBLE, 0, MPI_COMM_WORLD);

      double *a_buf = malloc(sizeof(double) * num_row_per_proc * MAT_SIZE);
      MPI_Scatter(a, num_row_per_proc * MAT_SIZE, MPI_DOUBLE, a_buf, num_row_per_proc * MAT_SIZE, MPI_DOUBLE, 0, MPI_COMM_WORLD);

      double *c_buf = malloc(sizeof(double) * num_row_per_proc * MAT_SIZE);
      for (int i = 0; i < num_row_per_proc; ++i) {
          for (int j = 0; j < MAT_SIZE; ++j) {
              *(c_buf + i * MAT_SIZE + j) = 0;
              for (int k = 0; k < MAT_SIZE; ++k) {
                  *(c_buf + i * MAT_SIZE + j) += *(a_buf + i * MAT_SIZE + k) * b[k][j];
              }
          }
      }
      free(a_buf);

      MPI_Gather(c_buf, num_row_per_proc * MAT_SIZE, MPI_DOUBLE, NULL, num_row_per_proc * MAT_SIZE, MPI_DOUBLE, 0, MPI_COMM_WORLD);
   }

   /* Don't forget to finalize your MPI application */
    MPI_Finalize();
}
