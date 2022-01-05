#include <stdio.h>

void exch(int* a, int i, int j) {
    int tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
}

int partition(int* a, int lo, int hi) {
	int v = a[lo];
	int i = lo + 1, j = hi;
	// lo~i-1 <=
	// i~j unknown
	// j+1~hi >
	while (i <= j) {
		if (a[i] <= v)
			i++;
		else {
			exch(a, i, j--);	
		}
	}
	// i = j+1
	exch(a, lo, j);
	return j;
}

void qsort(int* a, int lo, int hi) {
	if (lo >= hi)
		return;
	int par = partition(a, lo, hi);
	qsort(a, lo, par-1);
	qsort(a, par+1, hi);
}

int select(int* a, int lo, int hi, int k) {
	int j = partition(a, lo, hi);
	while (j != k) {
		if (j < k) {
			lo = j+1;
		}
		else {
			hi = j-1;
		}
		j = partition(a, lo, hi);

	}
	return a[j];
}
int main(){
	int a[10] = {6, 7, 8, 9, 3, 0, 1, 2, 4, 5};
	int rank = select(a, 0, 9, 5);
	printf("5 th is %d\n", rank);
	return 0;

}
