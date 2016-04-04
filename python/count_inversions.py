
def sort_and_count_inversions(list):
    if len(list) == 1:
        return list, 0
    
    lhs, x = sort_and_count_inversions(list[:len(list)/2])
    rhs, y = sort_and_count_inversions(list[len(list)/2:])
    result, z = merge_and_count_split_inversions(lhs, rhs)
    
    return result, (x + y + z)

def merge_and_count_split_inversions(lhs, rhs):
    result = []
    count = 0

    while len(lhs) > 0 or len(rhs) > 0:
        try:
            if lhs[0] <= rhs[0]:
                result.append(lhs.pop(0))
            else:
                result.append(rhs.pop(0))
                count += len(lhs)
        except IndexError:
            if len(lhs):
                result += lhs
                count += len(lhs)
            else:
                result += rhs
            break

    return result, count
