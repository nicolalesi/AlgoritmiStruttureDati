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
import java.util.Locale;
import java.util.Scanner;


public class Esercizio5 {

        int n;
        int m;
        boolean[][] matrix;
        long percorsi=0;

        public  Esercizio5 (String inputf)
        {
            readGraph(inputf);
        }

        private void readGraph(String inputf)
        {
            Locale.setDefault(Locale.US);

            try {
                Scanner f=new Scanner(new FileReader(inputf));
                n=f.nextInt();
                m=f.nextInt();

               matrix=new boolean[n][m];

                for(int i=0;i<n;i++) {
                    String nodo=f.next();
                    for (int j = 0; j < m; j++) {
                        if(nodo.charAt(j)=='.')
                            matrix[i][j]=true;
                        else
                            matrix[i][j]=false;
                    }
                }
            } catch(IOException ex){
                System.err.println(ex);
                System.exit(1);
            }
        }

        public long cercaPercorsi(int i,int j)
        {

            if(i==matrix.length-1 && j==matrix[0].length-1)
            {
                percorsi+=1;
            }
            if(j!=matrix[0].length-1 && matrix[i][j+1])
            {
                cercaPercorsi(i, j + 1);
            }
            if(i!=matrix.length-1 && matrix[i+1][j])
            {
                cercaPercorsi(i + 1, j);
            }
            return percorsi;

        }

    public static void main(String[]args)
    {
        Esercizio5 sp=new Esercizio5(args[0]);
        /*for(int i=0;i<sp.matrix.length;i++)
        {
            System.out.println();
            for(int j=0;j<sp.matrix[0].length;j++)
                System.out.print(sp.matrix[i][j]+" ");
        }
        System.out.println();*/
        System.out.println(sp.cercaPercorsi(0,0));
    }
}
