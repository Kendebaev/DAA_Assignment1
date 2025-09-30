# DAA_Assignment1
# Assignment Report
This report details the time complexity and efficiency of the four implemented algorithms, focusing on the Big O performance derived from their recurrence relations.

# 1. MergeSort 
MergeSort is a deterministic sorting algorithm that uses a classic divide-and-conquer approach.

Recurrence and Big O: Its running time is defined by the recurrence T(n)=2T(n/2)+Θ(n). This falls under Master Theorem Case 2.

The two recursive calls (2T(n/2)) represent dividing the problem in half.

The Θ(n) term represents the linear work required to merge the two sorted halves.

Since the cost of recursion (n 
log 
2
​
 2
 =n) equals the merge cost, the overall time complexity is a guaranteed O(nlogn) in the worst, average, and best cases.

Space and Stability: The algorithm requires O(n) auxiliary space for the reusable buffer used in the merge step. It is inherently stable.

# 2. QuickSort 
QuickSort is an in-place sorting algorithm that, in this implementation, is randomized for robustness.

Average Case Big O: Due to the randomized pivot selection, the probability of a consistently bad split is minimized, resulting in a robust average-case time complexity of O(nlogn).

Worst Case and Optimization: Standard QuickSort has a O(n 
2
 ) worst-case time, but the implementation uses tail recursion elimination (always recursing on the smaller side and iterating on the larger). This optimization limits the maximum depth of the recursive call stack to a safe O(logn), guaranteeing the algorithm won't suffer a stack overflow even in the O(n 
2
 ) time worst-case scenario.

Space: The space complexity is also O(logn) due to the bounded recursion stack.

# 3. Deterministic Select (Median-of-Medians)
Deterministic Select solves the selection problem (finding the k-th smallest element) with a guaranteed linear time complexity.

Recurrence and Big O: The algorithm is defined by the recurrence T(n)≤T(n/5)+T(7n/10)+Θ(n). This is analyzed using the Akra–Bazzi method.

The T(n/5) term comes from recursively finding the Median-of-Medians (MoM) from n/5 groups.

The T(7n/10) term is the worst-case size of the partition, guaranteed by the MoM to be no more than 70% of the original size.

Since the sum of the subproblem sizes (n/5+7n/10=9n/10) is less than n, the overall time complexity is a guaranteed O(n) in all cases.

# 4. Closest Pair of Points (Task 4)
This geometric algorithm uses a sophisticated divide-and-conquer approach to find the minimum distance between any two points in a 2D plane.

Recurrence and Big O: The algorithm follows the recurrence T(n)=2T(n/2)+Θ(n), identical to MergeSort, falling under Master Theorem Case 2.

The O(nlogn) complexity is established by the initial sort of the points by the X-coordinate.

The strip check (the combining step) is the key to the linear cost, Θ(n). It maintains efficiency because each point in the strip only needs to be checked against a constant number of its neighbors (at most 7 or 8) in Y-sorted order.

Final Complexity: The total running time is determined by the sorting and is therefore O(nlogn).
