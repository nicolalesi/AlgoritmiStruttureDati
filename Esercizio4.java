/*
 * Nicol Alesi
 * Matricola : 921867
 * nicol.alesi@studio.unibo.it
 *
 * Progetto del corso di Algoritmi e Strutture dati 2020/2021
 *
 */

import java.io.*;
import java.util.*;

public class Esercizio4 {

    static class MinHeap {

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

    /***************************

     Implementation of Prim's algorithm

     **************************/
    static class Prim {

        int n;       // nodi grafo input
        int m;       // archi grafo input
        int s;       // sorgente grafo input
        Vector<LinkedList<Edge>> adjList; // Grafo in input è salvato in una lista di adiacenza con nodi e relativa lista di archi
        HashMap<Integer, Integer> contatore = new HashMap<>();  //HashMap che conta gli archiBlu di ogni nodo
        double wtot; // Peso totale
        int nripartitori;



        private class Edge {
            final int src;  //sorgente
            final int dst;  //destinazione
            final double w; //peso
            boolean arcoBlu;//segnala se l'arco appartiene ad mst

            public Edge(int src, int dst, double w)
            {
                this.src = src;
                this.dst = dst;
                this.w = w;
                this.arcoBlu=false;
            }

            public int opposite(int v)
            {
                assert ((src == v) || (dst == v));

                return (v == src ? dst : src);
                //indice v uguale alla sorgente? True torno dst false torno src
            }
        }

        //Legge il grafico dal file di input
        public Prim(String inputf)
        {
            wtot = 0.0;

            /* Read input data */
            readGraph(inputf);

            //richiamo MST sulla sorgente letta in input
            MST(s);
        }


        private void readGraph(String inputf)
        {
            Locale.setDefault(Locale.US);

            try {
                Scanner f = new Scanner(new FileReader(inputf));
                n = f.nextInt();
                m = f.nextInt();
                s = f.nextInt();

                adjList = new Vector<LinkedList<Edge>>(n);

                for (int i = 0; i < n; i++) {
                    adjList.add(i, new LinkedList<Edge>());
                }

                for (int i = 0; i < m; i++) {
                    final int src = f.nextInt();
                    final int dst = f.nextInt();
                    final double weight = f.nextDouble();
                    final Edge newEdge = new Edge(src, dst, weight);
                    adjList.get(src).add(newEdge);
                    adjList.get(dst).add(newEdge);
                }
            } catch (IOException ex) {
                System.err.println(ex);
                System.exit(1);
            }
        }

        private void MST(int s)
        {

            //mst_edges[v] è l'arco che connette v con suo padre in mst
            Edge[] mst_edges = new Edge[n];

            //d[v] è il minimo peso tra gli archi che connettono v ai suoi vicini già in mst
            double[] d = new double[n];

            //added[v]==true se il nodo v è già stato aggiunto al MST e quindi puoi ignorarlo
            boolean [] added = new boolean[n];

            Arrays.fill(d, Double.POSITIVE_INFINITY);
            Arrays.fill(added, false);

            //peso della sorgente
            d[s] = 0.0;
            //peso totale
            wtot = 0.0;

            //Min heap della grandezza di n ossia i nodi
            MinHeap q = new MinHeap(n);

            //Scorro tutti i nodi e li inserisco tutti nel minheap con il relativo peso
            for (int v = 0; v < n; v++)
            {
                q.insert(v, d[v]);
                contatore.put(v,0);
            }

            while (!q.isEmpty())
            {
                final int u = q.min();    //torna l'indice del minimo
                q.deleteMin();
                added[u] = true;
                wtot += d[u];
                if (mst_edges[u] != null)
                {
                    mst_edges[u].arcoBlu=true;
                }

                Iterator<Edge> iter = adjList.get(u).iterator();
                int frontiera=2;
                while (iter.hasNext())
                {
                    final Edge edge = iter.next();
                    final double w = edge.w;
                    final int v = edge.opposite(u);
                    if (!added[v] && (w < d[v]))
                    {
                        d[v] = w;
                        q.changePrio(v, d[v]);
                        mst_edges[v] = edge;
                    }
                    if (edge.arcoBlu)
                    {
                        contatore.put(v, contatore.get(v) + 1);
                        if(contatore.get(v)>=frontiera)
                        {
                            nripartitori++;
                            frontiera+=1;
                        }
                    }
                }
            }
        }


    }

    public static void main( String args[])
    {
        if (args.length != 1) {
            final int n = 100;
            System.out.printf("%d %d\n", n, n*(n-1)/2);
            for (int i=0; i<n-1; i++) {
                for (int j=i+1; j<n; j++) {
                    final double weight = Math.random() * 100;
                    System.out.printf("%d %d %f\n", i, j, weight);
                }
            }
            return;
        }

        Prim mst = new Prim(args[0]);

        System.out.print((int)mst.wtot+" ");

        System.out.println(mst.nripartitori);
    }
}