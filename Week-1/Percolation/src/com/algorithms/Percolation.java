package com.algorithms;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.Scanner;

public class Percolation {
    private int openSites;
    private final int gridSize;
    private final int source;
    private final int sink;
    private boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf2;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        validateArg(n);
        openSites = 0;
        gridSize = n;
        source = 0;
        sink = (n * n + 1);
        grid = new boolean[n][n]; // n-by-n grid with `false` default values
        uf = new WeightedQuickUnionUF(n * n + 2); // includes `source` and `sink`
        uf2 = new WeightedQuickUnionUF(n * n + 1); // includes `source` only
        connectVirtualSites(); // connect virtual sites to union-find data structure(s)
    }

    /*
        - opens the site (row, col) if it is not open already
        - increment openSites by one
        - check if any of its neighbors (left, right, up, down) are open
        - if so, connect the site corresponding to (row, col) with the site corresponding to that neighbor
        - make connection using `union` from WeightedQuickUnionUF package
    */
    public void open(int row, int col) {
        validateIndex(row, col);

        if (!isOpen(row, col)) {
            int site = encode(row, col);
            grid[row][col] = true;

            // check neighbor above
            if ((row - 1 >= 0) && isOpen(row - 1, col)) {
                connectNeighbor(site, encode(row - 1, col));
            }
            // check neighbor below
            if ((row + 1 < gridSize) && isOpen(row + 1, col)) {
                connectNeighbor(site, encode(row + 1, col));
            }
            // check neighbor left
            if ((col - 1 >= 0) && isOpen(row, col - 1)) {
                connectNeighbor(site, encode(row, col - 1));
            }
            // check neighbor right
            if ((col + 1 < gridSize) && isOpen(row, col + 1)) {
                connectNeighbor(site, encode(row, col + 1));
            }
            openSites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndex(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIndex(row, col);
        return isOpen(row, col) && isConnected(encode(row, col), source);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(source) == uf.find(sink) && numberOfOpenSites() > 0;
    }

    /* test client with 3x3 grid
        [
            [[0,0],[0,1],[1,1]],
            [[1,0],[1,1],[1,2]],
            [[2,0],[2,1],[2,2]]
        ]
    */
    public static void main(String[] args) {
        int gridSize, row, col, openSites;

        System.out.print("Enter size of N-by-N grid: ");
        Scanner scanner = new Scanner(System.in);
        gridSize = scanner.nextInt();
        Percolation p = new Percolation(gridSize);
        System.out.print("Enter the row and column of a site to open: ");

        while(scanner.hasNextInt()) {
            row = scanner.nextInt();
            col = scanner.nextInt();
            p.open(row, col);
            openSites = p.numberOfOpenSites();
            String pluralize = (openSites == 1) ? " site" : " sites";

            System.out.println("Opened: (" + row + ", " + col + ")");
            System.out.println(openSites + pluralize + " open");

            if (p.percolates()) {
                System.out.println("Yes! System percolates.");
                System.exit(0);
            } else {
                System.out.println("Try another. System is not percolating.");
            }

            try {
                Thread.sleep(1000);
                System.out.println("==========");
                System.out.print("Enter the row and column of a site to open: ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // ensure `source` and `sink` are connected to sites in top and bottom rows, respectively
    private void connectVirtualSites() {
        for (int col = 0; col < gridSize; col++) {
            uf.union(source, encode(0, col));
            uf.union(sink, encode(gridSize - 1, col));
            uf2.union(source, encode(0, col));
        }
    }

    // `union` site to neighbor
    private void connectNeighbor(int p, int q) {
        uf.union(p, q);
        uf2.union(p, q);
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
        if (row < 0 || row > gridSize - 1 || col < 0 || col > gridSize - 1) {
            throw new IllegalArgumentException("Index must be between 1 and " + gridSize);
        }
    }
}
