package com.hl.neuralNetwork;

import java.util.List;

/**
 * 认知神经网 分类器
 * 输入层-- 概念层 -- 输出层
 * <p>
 * <p>
 * <p>
 * x1 概念1   ---> y1 概念1'
 * x2 概念2   ---. y2 概念2'
 * 若y1,y2同时激活，则y1 , y2 --> z3
 * 神经元有多个输入，一个输出
 * 学习的过程是若同时多个感知神经元被激活，则他们激活一个概念神经元。
 * 当概念神经元被激活，则反向激活感知神经元
 * <p>
 * 可以理解为  若干低级特征 --> 中级特征， 若干中级特征 --> 高级特征
 * 概念的因果关联。
 * 例如  当人看到一辆车，感知神经元A（感知车的视觉刺激），B（感知车的听觉刺激）：同时被激活
 * 则A,B同时会激活概念神经元C（车的概念）。
 * 而当听到“车”时，B被激活，则引起C被激活，C又反向传播神经冲动给A,B，导致人感知到起车的视觉刺激。
 */
public class RecognizeNet implements Cloneable {

    /**
     * 经历时间的对象
     */
    interface Timeable {
        // 经历一个时间刻度
        void spendTick();
    }

    // 神经元
    static class Cell {

        /**
         * 遗忘周期，即每经过FORGET_CYCLE个时间单位，遗忘
         */
        private static final int FORGET_CYCLE = 5;

        /**
         * 初始阈值
         */
        private static final int INIT_THRESHOLD = 10;

        /**
         * 初始连接强度
         */
        private static final int INIT_CONNECT_STRENGTH = 10;

        /**
         * 当前时间点，在周期内的时刻，每到一个周期，就归零
         */
        private int tick;

        // 名称
        private String name= "";

        // 所连接的下个神经元
        private Cell next = null;

        // 输入
        private int input = 0;

        // 输出电位（此时积累在神经元中的，如果已经输出了，则归零）
        private int output = 0;

        // 阈值
        private int threshold = INIT_THRESHOLD;

        // 到下个神经元的连接强度
        private int connectStrength = 0;

        // 输入连接数(即有多少个外部cell连接到本cell)
        private int inputConnectNum =0;

        public Cell() {

        }

        public Cell(String name) {
            setName(name);
        }
        /**
         * 激活函数. 即输入的处理变换
         *
         * @return
         */
        private int activeFunction(int input) {
            return input;
        }

        // 是否激活
        public boolean isActive() {
            return output > 0;
        }

        // 传播冲动到本CELL连接的输出神经元
        private void activeNext() {
            if (next == null) {
                return;
            }
            next.perceptInput(this.output);

            // 加强连接，增强记忆
            this.strongConnect();

            // 传播后，本CELL的输出和输入复位.
            this.output = 0;
            this.input = 0;
        }

        // 接受冲动
        public void perceptInput(int input) {
            this.input += input;
            this.output = activeFunction(this.input) - threshold;
            if (this.output < 0) {
                this.output = 0;
            }
            // 最高电平设置为阈值
            if (this.output > threshold) {
                this.output = threshold;
            }

            if (this.isActive()) {
                this.activeNext();
            }
        }

        // 输出
        public int getOutput() {
            return output;
        }

        // 输入
        public int getInput() {
            return input;
        }

        // 建立到某个神经元的关联
        public void connect(Cell cell) {
            // 不允许连接到自身
            if (cell == this) {
                return;
            }

            // 如果当期那有连接，先断开当前连接
            if (this.next != null) {
                disconnect();
            }

            // 建立到下个神经元的连接
            this.next = cell;
            this.connectStrength = INIT_CONNECT_STRENGTH;
            cell.inputConnectNum++;

            // 传导冲动
            activeNext();
        }

        // 断开和下个神经元的关联
        private void disconnect() {
            connectStrength =0;
            this.next.inputConnectNum--;
            this.next = null;
        }

        // 减弱连接强度
        public void weakConnect() {
            // 如果没有连接，则不用减弱。
            if (this.next == null) {
                return;
            }

            this.connectStrength--;
            // 如果减弱到0，则断开和下个神经元的关联
            if (connectStrength <=0) {
                disconnect();
            }
        }

        // 加强连接
        public void strongConnect() {
            // 如果没有连接，则不用加强。
            if (this.next == null) {
                return;
            }
            this.connectStrength++;
        }

        // 是否有外部关联
        public boolean hasInputConnection() {
            return this.inputConnectNum>0;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        private void doForget() {
            // 为了腾出概念神经元空间，学习新概念，需要遗忘。
            // 即每隔一段周期，就减弱概念神经元的已有连接
            this.weakConnect();

            // 放电
            this.input=0;
            this.output=0;
        }

        public void forget() {
            tick++;
            if (tick % FORGET_CYCLE ==0) {
                System.out.println("do forget");
                doForget();
                tick = 0;
            }
        }

        @Override
        public String toString() {
            return output+"["+(next==null?"":next.getName())+"]";
        }
    }

    static class Person implements Timeable {
        static final int N = 3;
        Cell[][] visionCells = new Cell[N][N];
        Cell[][] hearCells = new Cell[N][N];
        Cell[][] conceptCells = new Cell[N][N];

        Cell[] allCells = new Cell[N*N*3];
        Cell[] allConceptCells = new Cell[N*N];

        public Person() {
            int p =0;
            int q = 0;
            for (int i=0;i<N;i++)
                for (int j=0;j<N;j++) {
                    visionCells[i][j] = new Cell(String.format("v(%d,%d)",i,j));
                    allCells[p++] = visionCells[i][j];
                }
            for (int i=0;i<N;i++)
                for (int j=0;j<N;j++) {
                    hearCells[i][j] = new Cell(String.format("h(%d,%d)",i,j));
                    allCells[p++] = hearCells[i][j];
                }
            for (int i=0;i<N;i++)
                for (int j=0;j<N;j++) {
                    conceptCells[i][j] = new Cell(String.format("c(%d,%d)",i,j));
                    allCells[p++]=conceptCells[i][j];
                    allConceptCells[q++]=conceptCells[i][j];
                }

        }

        // 学习：
        // 如果此时多个神经元同时激活，则建立关联，他们公共连接到一个概念神经元上，并且概念神经元会反向传播刺激。
        // 先检查此时是否有已有概念神经元被激活，如果有，则加强关联。
        // 如果没有对应的概念神经元被激活，则建立新的关联。
        public void learn() {
            boolean hasConceptActive = false; //是否有概念神经元被激活
            Cell activeConceptCell = null; //激活的概念神经元
            for (Cell conceptCell:allConceptCells) {
                if (conceptCell.isActive()) {
                    hasConceptActive = true;
                    activeConceptCell = conceptCell;
                    break;
                }
            }

            if (hasConceptActive) {
                //先检查此时是否有已有概念神经元被激活，如果有，则加强关联。
                System.out.println("I recalled a concept:" + activeConceptCell);
            } else {
                // 如果没有对应的概念神经元被激活，则建立新的关联。
                // 寻找一个无外部关联的概念神经元
                Cell spareConceptCell = null;
                for (Cell cell:allConceptCells) {
                    if (!cell.hasInputConnection()) {
                        spareConceptCell=cell;
                        break;
                    }
                }

                boolean newConnected= false;
                for (Cell cell:allCells) {
                    if (cell.isActive()) {
                        cell.connect(spareConceptCell);
                        newConnected = true;
                    }
                }

                if (newConnected) {
                    System.out.println("I learned a new concept:" + spareConceptCell);
                }
            }
        }

        public void forget() {
            for (Cell cell:allCells) {
                cell.forget();
            }
        }

        @Override
        public void spendTick() {

            //System.out.println("========= tick  ===========");
            //printAllCells();

            learn();
            //System.out.println("--- after learn ----");
            //printAllCells();

            forget();

            //System.out.println("--- after forget ----");
            //printAllCells();
        }

        // 打印当前细胞状态
        public void printCells(String title, Cell[][] cells) {
            System.out.println(title);
            for (int i=0;i<N;i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(cells[i][j]+ "\t");
                }
                System.out.println();
            }
        }

        public void printAllCells() {
            printCells("visionCells",visionCells);
            printCells("hearCells",hearCells);
            printCells("conceptCells",conceptCells);
        }
    }

    /**
     * 学习车的概念
     */
    public static void learnCar() {
        Person person = new Person();

        int time =0;
        // 学习
        // 20个时间单位下，持续收到视觉和听觉刺激，学习车的概念
        for (;time<10;time++) {
            System.out.println("time:"+time);
            // vision
            // 0 0 0
            // 1 1 1
            // 0 0 0
            person.visionCells[1][0].perceptInput(11);
            person.visionCells[1][1].perceptInput(11);
            person.visionCells[1][2].perceptInput(11);

            // hear
            // 1 0 0
            // 1 0 0
            // 1 0 0
            person.hearCells[0][0].perceptInput(11);
            person.hearCells[1][0].perceptInput(11);
            person.hearCells[2][0].perceptInput(11);

            person.spendTick();
        }


//        System.out.println("===========  NOW I STOP TO LEARN ==========");
//        // 不再学习，模拟遗忘的过程
//        for (int time=0;time<1000;time++) {
//            System.out.println("time:"+time);
//            person.spendTick();
//        }


        for (;time<22;time++) {
            System.out.println("time:"+time);
            // vision
            // 0 0 0
            // 1 1 1
            // 0 0 0
            person.visionCells[1][0].perceptInput(11);
            person.visionCells[1][1].perceptInput(11);
            person.visionCells[1][2].perceptInput(11);

            person.spendTick();
        }
    }

    public static void main(String[] args) {
        learnCar();
    }
}
