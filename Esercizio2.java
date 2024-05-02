/*
 * Nicol Alesi
 * Matricola : 921867
 * nicol.alesi@studio.unibo.it
 *
 * Progetto del corso di Algoritmi e Strutture dati 2020/2021
 *
 */

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Esercizio2 {

    class MinHeap {

        heapElem heap[];
        /* pos[id] is the position of "id" inside the heap. Specifically,
           heap[pos[id]].key == id. This array is required to make
           decreaseKey() run in log(n) time. */
        int pos[];
        int size, maxSize;

        /**
         * An heap element is a pair (id, priority), where
         * id is an integer in 0..(maxSize-1)
         */
        private class heapElem {
            public final int data;
            public double prio;

            public heapElem(int data, double prio)
            {
                this.data = data;
                this.prio = prio;
            }
        }

        /**
         * Build an empty heap with at most maxSize elements
         */
        public MinHeap(int maxSize)
        {
            this.heap = new heapElem[maxSize];
            this.maxSize = maxSize;
            this.size = 0;
            this.pos = new int[maxSize];
            Arrays.fill(this.pos, -1);
        }


        /**
         * Return true iff index i is a valid index in the heap,
         * i.e., i>=0 and i<size
         */
        private boolean valid(int i)
        {
            return ((i >= 0) && (i < size));
        }

        /**
         * swap heap[i] with heap[j]
         */
        private void swap(int i, int j)
        {
            assert (pos[heap[i].data] == i);
            assert (pos[heap[j].data] == j);

            heapElem elemTmp = heap[i];
            heap[i] = heap[j];
            heap[j] = elemTmp;
            pos[heap[i].data] = i;
            pos[heap[j].data] = j;
        }

        /**
         * Return the index of the parent of heap[i]
         */
        private int parent(int i)
        {
            assert (valid(i));

            return (i+1)/2 - 1;
        }

        /**
         * Return the index of the left child of heap[i]
         */
        private int lchild(int i)
        {
            assert (valid(i));

            return (i+1)*2 - 1;
        }

        /**
         * Return the index of the right child of heap[i]
         */
        private int rchild(int i)
        {
            assert (valid(i));

            return lchild(i) + 1;
        }

        /**
         * Return true iff the heap is empty
         */
        public boolean isEmpty( )
        {
            return (size==0);
        }

        /**
         * Return true iff the heap is full, i.e., no more available slots
         * are available.
         */
        public boolean isFull( )
        {
            return (size > maxSize);
        }

        /**
         * Return the data of the element with lowest priority
         */
        public int min( )
        {
            assert ( !isEmpty() );
            return heap[0].data;
        }

        /**
         * Return the position of the child of i (if any) with minimum
         * priority. If i has no childs, return -1.
         */
        private int minChild(int i)
        {
            assert (valid(i));

            final int l = lchild(i);
            final int r = rchild(i);
            int result = -1;
            if (valid(l)) {
                result = l;
                if (valid(r) && (heap[r].prio < heap[l].prio)) {
                    result = r;
                }
            }
            return result;
        }

        /**
         * Exchange heap[i] with the parent element until it reaches the
         * correct position into the heap. This method requires time O(log n).
         */
        private void moveUp(int i)
        {
            assert (valid(i));

            int p = parent(i);
            while ( (p >= 0) && (heap[i].prio < heap[p].prio) ) {
                swap(i, p);
                i = p;
                p = parent(i);
            }
        }

        /**
         * Exchange heap[i] with the child with lowest priority, if any
         * exists, until it reaches the correct position into the heap.
         * This method requires time O(log n).
         */
        private void moveDown(int i)
        {
            assert (valid(i));

            boolean done = false;
            do {
                int dst = minChild(i);
                if (valid(dst) && (heap[dst].prio < heap[i].prio)) {
                    swap(i, dst);
                    i = dst;
                } else {
                    done = true;
                }
            } while (!done);
        }

        /**
         * Insert a new pair (data, prio) into the queue.
         * This method requires time O(log n).
         */
        public void insert(int data, double prio)
        {
            assert ((data >= 0) && (data < maxSize));
            assert (pos[data] == -1);
            assert ( !isFull() );

            final int i = size++;
            pos[data] = i;
            heap[i] = new heapElem(data, prio);
            moveUp(i);
        }

        /**
         * Delete the element with minimum priority. This method requires
         * time O(log n).
         */
        public void deleteMin( )
        {
            assert ( !isEmpty() );

            swap(0, size-1);
            pos[heap[size-1].data] = -1;
            size--;
            if (size>0) moveDown(0);
        }

        /**
         * Chenage the priority associated to |data|. This method requires
         * time O(log n).
         */
        public void changePrio(int data, double newprio)
        {
            int j = pos[data];
            assert ( valid(j) );
            final double oldprio = heap[j].prio;
            heap[j].prio = newprio;
            if (newprio > oldprio) {
                moveDown(j);
            } else {
                moveUp(j);
            }
        }
    }

    public int scambiPareggio(int[] bilanci)
    {
        MinHeap casseDebito=new MinHeap(bilanci.length);
        MinHeap casseCredito=new MinHeap(bilanci.length);

        double massimoCredito;
        double massimoDebito;

        int sizeDebito=0;
        int sizeCredito=0;

        //carico gli heap
        for(int i=0;i<bilanci.length;i++)
        {
            if(bilanci[i]>0)
            {
                //data non serve molto probabilmente vediamo alla fine, basta la prio
                //appoggio=bilanci[i]*-1;
                casseCredito.insert(sizeCredito,bilanci[i]*-1);
                sizeCredito++;
                //mettendo il meno trovo il minimo ricorda di cambiarlo
            }
            else if(bilanci[i]<0)
            {
                casseDebito.insert(sizeDebito,bilanci[i]);
                sizeDebito++;
            }
        }
        //ora devo trovare il massimo debito e il massimo credito ogni volta e sottrarli l'uno all'altroo fino ad avere tutto a zero
        //per trovare massimo credito con minheap metto un meno davanti a tutti i massimi e trovo il minimo
        //per trovare il massimo debito trovo il minimo

        int nScambi=0;

        while(!casseCredito.isEmpty()||!casseDebito.isEmpty())
        {
            nScambi++;

            massimoCredito = casseCredito.heap[0].prio;
            casseCredito.deleteMin();
            massimoCredito = -massimoCredito;

            massimoDebito = casseDebito.heap[0].prio;
            casseDebito.deleteMin();

            if(massimoCredito<-massimoDebito)
            {
                massimoDebito=massimoDebito+massimoCredito;
                casseDebito.insert(sizeDebito,massimoDebito); //mettendolo alla fine invece che all'inizio evito tutti gli scambi per riordinare
                sizeCredito--;  //il credito è =0 non viene reinserito quindi size diminuisce, in questo modo evito uno scambio

            }
            else if(massimoCredito>-massimoDebito)
            {
                massimoCredito=massimoCredito+massimoDebito;
                casseCredito.insert(sizeCredito,massimoCredito*-1);
                sizeDebito--; //il debito è =0 non viene reinserito nell'heap quindi size diminuisce, in questo modo evito un possibile scambio
            }
            else if(massimoCredito==-massimoDebito)
            {
                //dato che non saranno reinseriti la taglia dell'heap diminuisce di uno in entrambi i casi
                sizeCredito--;
                sizeDebito--;
            }
        }

        return nScambi;
    }

    public static void main(String[]args)
    {
        Esercizio2 e1=new Esercizio2();

        Locale.setDefault(Locale.US);
        if(args.length!=1){
            System.exit(1);
        }
        try {
            Scanner scan=new Scanner(new FileReader(args[0]));
            int n=scan.nextInt();
            int [] bilanci=new int[n];
            for(int i=0;i<n;i++)
                bilanci[i]=scan.nextInt();

            System.out.println(e1.scambiPareggio(bilanci));
        } catch(IOException e){ e.printStackTrace(); }
    }

    //C:\Users\niky0>javac Esercizio2.java
    //
    //C:\Users\niky0>java -cp . Esercizio2 C:\Users\niky0\Downloads\Progetti\Esercizio2\Input1.txt
    //7
    //
    //C:\Users\niky0>java -cp . Esercizio2 C:\Users\niky0\Downloads\Progetti\Esercizio2\Input2.txt
    //11
    //
    //C:\Users\niky0>java -cp . Esercizio2 C:\Users\niky0\Downloads\Progetti\Esercizio2\Input3.txt
    //7
}
