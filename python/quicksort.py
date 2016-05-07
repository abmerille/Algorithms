def quicksort(array):
    qsort(array, 0, len(array))
    return array
            
def qsort(array, low, high):
    if high !=low:
        pivot = partition(array, low, high)
        sort(array, low, pivot)                 # sort below the pivot
        sort(array, pivot + 1, high)            # sort above the pivot

def partition(a, low, high):
    pivot = a[low]
    i = low
    for j in range(low + 1, high):
        if a[j] < pivot:
            i += 1                              # increment less than pointer
            a[j], a[i] = a[i], a[j]             # swap lower element
    a[low], a[i] = a[i], a[low]                 # swap pivot into correct place
    return i

