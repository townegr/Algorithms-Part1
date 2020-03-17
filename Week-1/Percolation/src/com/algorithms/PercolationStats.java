package com.algorithms;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int gridSize;
    private int trialCount; // number of computations
    private double[] results; // percolation threshold results

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validateArgs(n, trials);
        gridSize = n;
        trialCount = trials;
        results = new double[trialCount];

        for (int i = 0; i < trialCount; i++) {
            Percolation p = new Percolation(gridSize);

            while (!p.percolates()) {
                int row = StdRandom.uniform(gridSize);
                int col = StdRandom.uniform(gridSize);
                p.open(row, col);
            }

            results[i] = p.numberOfOpenSites() / (double) (gridSize * gridSize);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(trialCount));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(trialCount));
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

    private void validateArgs(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Both arguments must be greater than 0");
        }
    }
}
