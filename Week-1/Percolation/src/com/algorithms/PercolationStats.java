package com.algorithms;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double mean;
    private final double stdDev;
    private final double confidenceInterval;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validateArgs(n, trials);
        final double confidenceLevel = 1.96; // 95% confidence level
        final double[] results = new double[trials]; // percolation threshold results

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                int row = StdRandom.uniform(n);
                int col = StdRandom.uniform(n);
                p.open(row, col);
            }

            results[i] = p.numberOfOpenSites() / (double) (n * n);
        }

        mean = StdStats.mean(results);
        stdDev = StdStats.stddev(results);
        confidenceInterval = confidenceLevel * stdDev / Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - confidenceInterval();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + confidenceInterval();
    }

    // test client (see below)
    public static void main(String[] args) {
        int gridSize = Integer.parseInt(args[0]);
        int trialCount = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(gridSize, trialCount);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

    private double confidenceInterval() {
        return confidenceInterval;
    }

    private void validateArgs(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Both arguments must be greater than 0");
        }
    }
}
