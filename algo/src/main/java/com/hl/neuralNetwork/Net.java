package com.hl.neuralNetwork;

/**
 * 神经网分类器
 * 输入层--输出层
 */
public class Net implements Cloneable {

    private int[][] w;

    // create an NN (input=x)
    public Net(int[] x, int nKlass) {
        init(x, nKlass);
    }

    // copy from net
    public Net(Net net) {
        this.w = net.w.clone();
    }

    public static void printInputAndKlass(int[] x, int klass) {
        for (int i = 0; i < x.length; i++) {
            System.out.print(x[i] + " ");
        }
        System.out.println(" -- " + klass);
    }

    public static void main(String[] args) {
        // 1 0 0 0 --> 1
        // 0 1 0 0 --> 2
        // 0 0 1 0 --> 3
        // 0 0 0 1 --> 4
        int[] x1 = new int[]{1, 0, 0, 0};
        int[] x2 = new int[]{0, 1, 0, 0};
        int[] x3 = new int[]{0, 0, 1, 0};
        int[] x4 = new int[]{0, 0, 0, 1};

        int[][] xSet = new int[][]{x1, x2, x3, x4};
        int nKlass = 4;
        Net net = new Net(x1, nKlass);

        // train
        for (int k = 0; k < xSet.length; k++) {
            System.out.print("train: ");
            printInputAndKlass(xSet[k], k);
            net.train(xSet[k], k);
        }

        // classify
        for (int i = 0; i < xSet.length; i++) {
            int klass = net.classify(xSet[i]);
            System.out.print("classify: ");
            printInputAndKlass(xSet[i], klass);
        }
    }

    // init net, make weight as new w[][]=w[x.length][nKlass], y[]=y[nKlass]
    public void init(int[] x, int nKlass) {
        w = new int[x.length][nKlass];
    }

    // give example: (x,klass), adjust weights w
    public void train(int[] x, int klass) {
        int outKlass = classify(x); //output
        while (outKlass != klass) {
            // adjust weights
            // (reward right connection) maximize y[klass] --> w[j][klass] = 1
            // (punish wrong connection) minimize y[outKlass] --> w[j][outKlass] =0
            for (int j = 0; j < x.length; j++) {
                w[j][klass] += x[j];
                w[j][outKlass] -= x[j];
            }

            // re-output
            outKlass = classify(x);
        }
        printWeights();
    }

    // give input x, calc klass of x
    public int classify(int[] x) {
        // w*x
        int[] y = output(w, x);
        return findKlass(y);
    }

    // output
    private int[] output(int[][] w, int[] x) {
        // y[k] = sum(w[j][k]*x[j]) --- j=[0,x.length]
        int nKlass = w[0].length;
        int[] y = new int[nKlass];
        for (int k = 0; k < nKlass; k++) {
            int sum = 0;
            for (int j = 0; j < x.length; j++) {
                //int o = w[j][k] * x[j];
                int o = w[j][k] * x[j];
                sum += o;
            }
            y[k] = sum;
        }
        return y;
    }

    // find class of output
    private int findKlass(int[] output) {
        int[] y = output;
        int nKlass = y.length;
        // find max y[k]
        int maxY = y[0];
        int maxK = 0;
        for (int k = 0; k < nKlass; k++) {
            if (y[k] > maxY) {
                maxY = y[k];
                maxK = k;
            }
        }
        return maxK;
    }

    // print current weight
    private void printWeights() {
        int n = w.length;
        int m = w[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(w[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
