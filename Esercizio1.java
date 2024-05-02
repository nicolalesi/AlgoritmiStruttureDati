import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Esercizio1 {


    public static int[] composizioneNumero(String stringa,int i){
        String numero="";
        while(i<stringa.length() && (int)stringa.charAt(i)<64 )
        {
            numero=numero+stringa.charAt(i);
            i++;
        }
        int[] numeroIntero=new int[2];
         numeroIntero[0]=Integer.parseInt(numero);
         numeroIntero[1]=i;
        return numeroIntero;
    }

    public static TreeMap<Character, Integer> contaLettere(String stringa){
        stringa=stringa.toLowerCase(Locale.ROOT);
        //treeMap non accetta doppi
        TreeMap<Character,Integer> collezione=new TreeMap<Character,Integer>();
        collezione.put(stringa.charAt(0),composizioneNumero(stringa,1)[0]);
        for(int i=composizioneNumero(stringa,1)[1];i<stringa.length();i++)
        {
            //se è un carattere
            if((int)stringa.charAt(i)>64)
            {
                if(collezione.containsKey(stringa.charAt(i)))
                {
                    collezione.put(stringa.charAt(i),collezione.get(stringa.charAt(i))+composizioneNumero(stringa,i+1)[0]);
                }
                else
                    collezione.put(stringa.charAt(i),composizioneNumero(stringa,i+1)[0]);
            }
            else //se non è un carattere
                i=composizioneNumero(stringa,i)[1]-1;
        }
        return collezione;
    }

    public static boolean anagram(String stringa1,String stringa2)
    {
        TreeMap<Character,Integer> conteggioStringa1=contaLettere(stringa1);
        TreeMap<Character,Integer> conteggioStringa2=contaLettere(stringa2);

        if(conteggioStringa1.size()!=conteggioStringa2.size())
            return false;

        for(Character key: conteggioStringa1.keySet())
        {   //se c'è anche di là quel carattere
            if(conteggioStringa2.containsKey(key))
            {   //se non ha la stessa cardinalità non è un anagramma
                if(conteggioStringa2.get(key)!=conteggioStringa1.get(key))
                    return false;
            }   //se ha la stessa cardinalità continuiamo
            else //se non c'è quel carattere non sono anagrammi
                return false;
        }
        return true; //se arriviamo alla fine sarà true
    }

    public static void main(String[]args){

       /* TreeMap<Character,Integer> collezione=contaLettere("Q5b12c4");
        for (Character key : collezione.keySet()) {
            System.out.println(key + "=" + collezione.get(key));
        }

        System.out.println(anagram("B4c3Z82","Z1c4B3Z81"));*/

        Locale.setDefault(Locale.US);
        if(args.length!=1){
            System.exit(1);
        }
        try {
            Scanner scan=new Scanner(new FileReader(args[0]));
            String s1=scan.nextLine();
            String s2=scan.nextLine();

            System.out.println(anagram(s1,s2));
        } catch(IOException e){ e.printStackTrace(); }

        }
}
