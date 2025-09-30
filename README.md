# DAA_Assignment1
# Assignment Report
This report documents the implementation and theoretical analysis of four classic divide-and-conquer algorithms, detailing architectural choices made to ensure performance and safe recursion, as well as the derived time complexity (Θ notation).

 # Architecture Notes: Controlling Depth and Allocations
Effective divide-and-conquer implementation requires control over memory allocation overhead and recursive stack depth, particularly for large inputs.

# Depth Control (Safe Recursion)
QuickSort: The primary safety mechanism is tail recursion elimination. By implementing a loop that always recurses on the smaller of the two partitioned sub-arrays and then iterates (using a while loop) over the larger one, the maximum recursion depth is mathematically bounded to O(logn) in all cases, preventing stack overflow errors that plague standard QuickSort.

MergeSort & Deterministic Select: These algorithms rely on controlled, balanced partitioning (always n/2 or guaranteed 7n/10 split), which naturally leads to a maximum recursion depth of O(logn).

# Allocation Control
MergeSort: The linear-time merge operation typically requires auxiliary space. To control allocation overhead, the implementation pre-allocates a single, reusable buffer (O(n) space) outside the recursive calls. This avoids the time-consuming process of allocating and deallocating memory during every merge step.

Small-N Cutoff: For both MergeSort and potentially QuickSort, recursion stops for sub-arrays smaller than a set constant (e.g., n≤7). Instead, a simple iterative algorithm like Insertion Sort is called. This reduces the number of function calls and memory management overhead, lowering the constant factor of the total running time.

# Recurrence Analysis for Implemented Algorithms
1. MergeSort (Θ(nlogn))
MergeSort is defined by the recurrence T(n)=2T(n/2)+Θ(n). This relation falls into Case 2 of the Master Theorem. The two recursive calls (2T(n/2)) represent the conquer step, while the Θ(n) term represents the linear work done during the merging of the two sorted halves. Since n 
log 
2
​
 2
 =n is asymptotically equal to the cost of the merging step f(n)=Θ(n), the resulting running time is determined to be Θ(nlogn).

2. QuickSort (Θ(nlogn) Expected)
The best and average-case running time of QuickSort is Θ(nlogn), often proven by analyzing the recurrence tree where the cost is balanced across logarithmic levels. Although the worst-case is O(n 
2
 ), the use of a randomized pivot ensures that the expected number of comparisons remains proportional to nlogn. The key architectural optimization guarantees the maximum stack depth is bounded to O(logn) regardless of the partitioning quality.

3. Deterministic Select (Median-of-Medians, Θ(n))
This algorithm is designed to guarantee linear time complexity by using the Median-of-Medians (MoM) pivot, which ensures a good partition. The running time is defined by the recurrence T(n)≤T(n/5)+T(7n/10)+Θ(n). This is a non-Master Theorem form, solved by the Akra–Bazzi method or substitution. Crucially, the sum of the subproblem sizes (n/5+7n/10=9n/10) is less than n, meaning the linear work f(n)=Θ(n) dominates the complexity, yielding a worst-case time of Θ(n).

4. Closest Pair of Points (Θ(nlogn))
The Closest Pair algorithm follows the recurrence T(n)=2T(n/2)+Θ(n), which is governed by Master Theorem Case 2. The initial complexity is set by the required O(nlogn) sorting step. The subsequent recursive calls take 2T(n/2) time, and the critical strip check combining step takes only Θ(n) time because only a constant number (7 or 8) of neighbors must be checked per point. The final time complexity is therefore Θ(nlogn).

# Plots: Time vs. N and Depth vs. N
(Note to Self/User: Since I cannot generate dynamic plots, this section serves as a placeholder to describe the required visualizations.)

Time vs. N
Plot the measured execution time (in milliseconds or nanoseconds) against the input size (n) for n values (e.g., 10 
3
 ,10 
4
 ,10 
5
 ).

Expected Shape: QuickSort, MergeSort, and Closest Pair should show a clear concave shape consistent with nlogn. Deterministic Select should exhibit a nearly linear growth, resembling a Θ(n) curve.

Depth vs. N
Plot the recorded maximum recursion depth against the input size (n).

Expected Shape: All algorithms, due to their O(logn) depth control, should show a gradual, logarithmic increase in maximum depth. For example, doubling n should increase the depth by approximately 1.

# Discussion of Constant-Factor Effects (Cache, GC)
While Big O notation describes asymptotic behavior, constant factors often determine real-world performance:

Cache Locality: QuickSort and Insertion Sort benefit heavily from good cache locality because they operate primarily on contiguous memory regions and often access elements that were recently used. This is why QuickSort, despite the same O(nlogn) asymptotic complexity, typically outperforms MergeSort.

Garbage Collection (GC) Overhead: MergeSort suffers from higher constant factors related to memory. Although the implementation uses a reusable buffer, the overall memory footprint is O(n), and repeated manipulation of two large memory blocks (original array and buffer) can impact the operating system and GC, increasing runtime overhead compared to QuickSort's O(logn) auxiliary space.

Small-N Cutoff: The use of Insertion Sort for n≤7 effectively eliminates the constant overhead of recursive function calls, partition logic, and the O(n) merge step for small leaf nodes in the recursion tree, leading to faster execution for real-world inputs.

# Summary: Alignment between Theory and Measurements

The theoretical analysis strongly suggests Θ(nlogn) for the sorting and geometric algorithms and Θ(n) for selection. The measurements collected should generally align with these theoretical bounds. Deviations at small n are expected due to constant-factor effects and the small-n cut-off. Major deviations at large n (e.g., QuickSort suddenly running O(n 
2
 ) time) would indicate a failure of the randomization or partitioning mechanism. The consistent O(logn) depth measurements should validate the stack safety of the robust QuickSort implementation.
