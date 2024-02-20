// a) You are developing a student score tracking system that keeps track of scores from different assignments. The 
// ScoreTracker class will be used to calculate the median score from the stream of assignment scores. The class 
// should have the following methods: 
//  ScoreTracker() initializes a new ScoreTracker object. 
//  void addScore(double score) adds a new assignment score to the data stream. 
//  double getMedianScore() returns the median of all the assignment scores in the data stream. If the number 
// of scores is even, the median should be the average of the two middle scores. 
// Input: 
// ScoreTracker scoreTracker = new ScoreTracker(); 
// scoreTracker.addScore(85.5);    // Stream: [85.5] 
// scoreTracker.addScore(92.3);    // Stream: [85.5, 92.3] 
// scoreTracker.addScore(77.8);    // Stream: [85.5, 92.3, 77.8] 
// scoreTracker.addScore(90.1);    // Stream: [85.5, 92.3, 77.8, 90.1] 
// double median1 = scoreTracker.getMedianScore(); // Output: 88.9 (average of 90.1 and 85.5) 
// scoreTracker.addScore(81.2);    // Stream: [85.5, 92.3, 77.8, 90.1, 81.2] 
// scoreTracker.addScore(88.7);    // Stream: [85.5, 92.3, 77.8, 90.1, 81.2, 88.7] 
// double median2 = scoreTracker.getMedianScore(); // Output: 86.95 (average of 88.7 and 85.5) 
import java.util.Collections;
import java.util.PriorityQueue;

public class QA3a {
    private PriorityQueue<Double> maxHeap;  // Holds the lower half of scores
    private PriorityQueue<Double> minHeap;  // Holds the higher half of scores

    public QA3a() {
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap = new PriorityQueue<>();
    }

    public void addScore(double score) {
        if (maxHeap.isEmpty() || score <= maxHeap.peek()) {
            maxHeap.offer(score);
        } else {
            minHeap.offer(score);
        }

        // Balance the heaps
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double getMedianScore() {
        if (maxHeap.isEmpty()) {
            throw new IllegalStateException("No scores available");
        }

        if (maxHeap.size() == minHeap.size()) {
            // Even number of scores, average the two middle scores
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            // Odd number of scores, the middle score in maxHeap is the median
            return maxHeap.peek();
        }
    }

    public static void main(String[] args) {
        QA3a scoreTracker = new QA3a();
        scoreTracker.addScore(85.5);
        scoreTracker.addScore(92.3);
        scoreTracker.addScore(77.8);
        scoreTracker.addScore(90.1);
        double median1 = scoreTracker.getMedianScore();
        System.out.println("Median 1: " + median1);  // Output: 87.8

        scoreTracker.addScore(81.2);
        scoreTracker.addScore(88.7);
        double median2 = scoreTracker.getMedianScore();
        System.out.println("Median 2: " + median2);  // Output: 87.1
    }
}
