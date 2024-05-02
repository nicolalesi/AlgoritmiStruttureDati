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

public class Esercizio3 {

    public static int sommaCambi(int[][]colori)
    {

        int[][] S=new int[colori.length][colori[0].length]; //Creo una matrice che conterrà le soluzioni
        int perno;                                          //Valore che verrà confrontato con tutti gli altri

        //una possibile invariante di ciclo potrebbe essere che sia all'inizio che alla fine del ciclo il valore di S[i-1][j]=S[i][j-1]+S[i][j]
        for (int i=0;i<colori.length;i++)
        {
            for (int j = 0; j < colori[0].length; j++)
            {
                assert (S[i-1][j]==S[i-1][j-1]+S[i-1][j]);  //invariante di ciclo

                perno=colori[i][j];                        //Fisso ogni volta come perno il valore in i j
                for(int k=i;k<colori.length;k++)           //Scorro tutte le righe a partire da i
                {
                    if(colori[k][j]!=perno)
                    {                                       //Conto scambi necessari per quel colore di quel veicolo
                        S[i][j]++;
                        S[k][j]++;
                    }
                }
                if(j>0)                                    //Sommiamo i valori presenti nella colonna precedente
                    S[i][j]=S[i][j]+S[i][j-1];              //colonna finale somma degli scambi necessari adottando il sistema di colori di una determinata città

                assert (S[i-1][j]==S[i-1][j-1]+S[i-1][j]);  //La soluzione della riga precedente è = alla Soluzione della colonna precedente della riga precedente più la colonna successiva
            }

        }

        return min(S);                     //Ritorno il minimo della colonna finale ossia il numero minimo di scambi necessario
    }

    //Trova il minimo dell'ultima colonna della matrice soluzione, ritorna l'indice della città con il minimo
     public static int min(int[][]S)
     {
         int min=S[0][S[0].length-1];
         int indexMin=0;

         for(int i=1;i<S.length;i++)
         {
             assert (S[indexMin][0]<=S[i-1][0]);    //ad ogni giro il valore trovato deve essere <= del precedente

            if(S[i][S[0].length-1]<min)
            {
                min=S[i][S[0].length-1];
                indexMin=i;
            }

             assert (S[indexMin][0]<=S[i][0]);      //valore trovato deve essere <=di quello visitato
         }
         return indexMin;
     }

     public static void main(String[]args)
     {

         Locale.setDefault(Locale.US);
         if(args.length!=1){
             System.exit(1);
         }
         try {
             Scanner scan=new Scanner(new FileReader(args[0]));
             int n=scan.nextInt();
             int[][] colori=new int[n][5];

             for(int i=0;i<colori.length;i++)
                 for(int j=0;j<colori[0].length;j++)
                     colori[i][j]=scan.nextInt();

             System.out.println(sommaCambi(colori));

         } catch(IOException e){ e.printStackTrace(); }

         //C:\Users\niky0>javac Esercizio3.java
         //
         //C:\Users\niky0>java -cp . Esercizio3 C:\Users\niky0\Downloads\Progetti\Esercizio3\Input1.txt
         //0
         //
         //C:\Users\niky0>java -cp . Esercizio3 C:\Users\niky0\Downloads\Progetti\Esercizio3\Input2.txt
         //3
         //
         //C:\Users\niky0>java -cp . Esercizio3 C:\Users\niky0\Downloads\Progetti\Esercizio3\Input3.txt
         //15
     }
}
