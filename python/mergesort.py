
def mergesort(list):
    
    if len(list) <= 1:
        return list

    left = mergesort(list[:len(list)/2])
    right = mergesort(list[len(list)/2:])
    
    result_list = []
    
    while len(left) > 0 or len(right) > 0:
        try:
            if left[0] <= right[0]:
                result_list.append(left.pop(0))
            else:
                result_list.append(right.pop(0))
        except IndexError:
            if len(left) > 0:
                result_list += left
            else:
                result_list += right
            break   
    
    return result_list
