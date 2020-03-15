package com.algorithms.java;

import java.util.Arrays;
import java.util.HashMap;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // creates n-by-n grid, with all sites initially blocked
    private int openSites;
    private int gridSize;
    private int source = 0;
    private int sink;
    private boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf2;

    public Percolation(int n) {
        validateArg(n);
        openSites = 0;
        gridSize = n;
        grid = new boolean[n][n]; // n-by-n grid with `false` default values
        uf = new WeightedQuickUnionUF(n * n + 2); // includes `source` and `sink`
        uf2 = new WeightedQuickUnionUF(n * n + 1); // includes `source` only
        connectVirtualSites(); // connect virtual sites to union-find data structure(s)
    }

    // opens the site (row, col) if it is not open already
    // increment openSites by one
    // check if any of its neighbors (left, right, up, down) are open
    // if so, connect the site corresponding to (row, col) with the site corresponding to that neighbor
    // make connection using `union` from WeightedQuickUnionUF package
    public void open(int row, int col) {
        validateIndex(row, col);

        if (!isOpen(row, col)) {
            grid[row][col] = true;

            // check neighbor above
            if ((row - 1 >= 0) && isOpen(row - 1, col)) {
                uf.union(encode(row, col), encode(row - 1, col));
                uf2.union(encode(row, col), encode(row - 1, col));
            }
            // check neighbor below
            if ((row + 1 < gridSize) && isOpen(row + 1, col)) {
                uf.union(encode(row, col), encode(row + 1, col));
                uf2.union(encode(row, col), encode(row + 1, col));
            }
            // check neighbor left
            if ((col - 1 >= 0) && isOpen(row, col - 1)) {
                uf.union(encode(row, col), encode(row, col - 1));
                uf2.union(encode(row, col), encode(row, col - 1));
            }
            // check neighbor right
            if ((col + 1 < gridSize) && isOpen(row, col + 1)) {
                uf.union(encode(row, col), encode(row, col + 1));
                uf2.union(encode(row, col), encode(row, col + 1));
            }
            openSites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isConnected(encode(row, col), source);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

//    // does the system percolate?
    public boolean percolates() {
        return uf.find(source) == uf.find(sink);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        int i = p.encode(1,2);
        System.out.println(i);
    }

    // ensure `source` and `sink` are connected to sites in top and bottom rows, respectively
    private void connectVirtualSites() {
        for (int col = 0; col < gridSize; col++) {
            uf.union(encode(0, col), source);
            uf.union(encode(gridSize - 1, col), sink);
            uf2.union(encode(0, col), source);
        }
    }

    private boolean isConnected(int p, int q) {
        return uf2.find(p) == uf2.find(q);
    }

    private int encode(int row, int col) {
        return (row * gridSize + col + 1);
    }

    private void validateArg(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Invalid argument: " + i + ". Must be at least 1.");
        }
    }

    private void validateIndex(int row, int col) {
        if (row < 0 || row > gridSize || col < 0 || col > gridSize) {
            throw new IndexOutOfBoundsException("Index does not exist");
        }
    }
}
