package com.company;

import java.io.File;
import java.util.*;
import com.google.common.collect.Sets;

public class Main {

    public static void main(String[] args) throws java.io.FileNotFoundException{
        Scanner input = new Scanner(new File("src/graph.txt"));
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj liczbe wierzcholkow grafu: ");
        int num = scan.nextInt();
        System.out.println("Podaj diagnozowalność układu: ");
        int m = scan.nextInt();

        if(2*m>=num){
            System.out.println("Układ nie spełnia warunku koniecznego.");
            System.exit(0);
        }
        int graf[][] = new int[num][num];
        int zredukowany[][] = new int[num][num];
        int max=num*num;
        int suma=0;

        try {
            for(int i=0;i<num;i++){
                for(int j =0;j<num;j++){
                    if(!(input.hasNextLine())) {
                        System.out.println("Graf wejściowy zawiera za mało dnych");
                        System.exit(0);
                    }
                    int n=input.nextInt();
                    if((n==0)||(n==1)){
                        graf[i][j]=n;
                        suma=suma+1;
                    }
                    else{
                        System.out.println("Błędne dane wejściowe w grafie.");
                        System.exit(0);
                    }
                }
            }
            if(input.hasNext()){
                System.out.println("Graf wejściowy zawiera za dużo danych");
                System.exit(0);
            }

            if(!sprawdzDiagnozowalnosc(graf,m)){
                System.out.println("Warunek konieczny nie spelniony. System nie jest "+m+"-diagnozowalny!");
                System.exit(0);
            }
            else if(!sprawdzWystarczajacy(graf,m)) {
                System.out.println("Warunek wystarczający nie spelniony. System nie jest "+m+"-diagnozowalny!");
                System.exit(0);
            } else System.out.println("Waruneki spelnione. System jest "+m+"-diagnozowalny!");
            System.out.print("\n");

            System.out.println("MACIERZ WEJŚCIOWA:");
            wypiszTablice(num,graf);


            zredukowany = redukuj(graf,m);
            System.out.println("\nMACIERZ OPTYMALNA:");
            wypiszTablice(num, zredukowany);

            if(sprawdzWystarczajacy(zredukowany,m)){
                System.out.println("Warunek wystarczający zoptymalizowanego grafu jest spełniony.");
            }
            if(sprawdzDiagnozowalnosc(zredukowany,m)){
                System.out.println("Warunek konieczny zoptymalizowanego grafu jest spełniony.");}

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<Integer> ktoreZbadac(int[][] graf, int m){
        int sum=0;
        List<Integer> doZbadania = new ArrayList<>();

        for(int i=0;i<graf.length;i++){ ///WYZEROWANIE DIAGONALII
            graf[i][i]=0;
        }

        for(int j=0;j<graf.length;j++) {    ///ZEBRANIE INFORMACJI O KOLUMNACH MOŻLIWYCH DO ZOPTYMALIZOWANIA
            for (int i = 0; i < graf.length; i++) { //SPAWDZANIE KOLUMN CZY RÓWNE M
                if(graf[i][j]==1) sum++;
            }
            if(sum>m) doZbadania.add(j);
            sum=0;
        }

        return doZbadania;
    }

    public static int[][] redukuj(int[][] graf, int m){
        int[][] wejsciowy = graf;

        for(int k=0;k<graf.length;k++) {
            List<Integer> x = ktoreZbadac(graf, m);
            List<Integer> y = new ArrayList<>();
            int wiersz = 0;

            for (Integer zb : x) {
                for (int i = 0; i < graf.length; i++) {
                    if (graf[i][zb] == 1) {
                        y.add(i);
                    }
                }

                wiersz = wyznaczCiecie(graf, y);
                y.clear();
                graf[wiersz][zb] = 0;
            }
        }

        return graf;
    }

    public static Integer wyznaczCiecie(int[][] graf, List<Integer> y){
        int max=0;
        int maxIndeks=0;
        int tempIndeks=0;
        int sum=0;

        for(Integer w : y){
            for(int j=0;j<graf[w].length;j++){
                //System.out.print(graf[w][j]);
                if(graf[w][j]==1) sum++;
                tempIndeks=w;
            }
            //System.out.println("");
            if(max<sum){ max=sum; maxIndeks=tempIndeks; }
            sum=0;
        }
        //System.out.println(maxIndeks);

        return maxIndeks;
    }

    public static boolean sprawdzDiagnozowalnosc( int[][] graf, int m){
        int sum =0;
        for (int j = 0; j < graf.length; j++) {
            for (int i = 0; i < graf.length; i++) {
                if(i==j) continue; //Nie sprawdzamy skosów
                sum=sum+graf[i][j];
            }
            if(sum<m)return false;
            sum=0;
        }

        return true;
    }

    public static boolean sprawdzDiagon(int[][] graf, int m,int j){
        int sum =0;
        for (int i = 0; i < graf.length; i++) {
            if (i == j) continue; //Nie sprawdzamy skosów
            sum = sum + graf[i][j];
        }
        if(sum>m)return true;
        else return false;
    }

    public static boolean diagonKoniec( int[][] graf, int m){
        int sum =0;
        for (int j = 0; j < graf.length; j++) {
            for (int i = 0; i < graf.length; i++) {
                if(i==j) continue;
                sum=sum+graf[i][j];
            }
            if(sum>m)return true;
            sum=0;
        }
        return false;
    }

    public static boolean sprawdzWystarczajacy(int[][] graf, int m){
        Set<Integer> set = new HashSet<Integer>();
        for(int i=0;i<graf.length;i++){
            set.add(i);
        }
        for(int p=0;p<m;p++){
            int licz=graf.length-2*m+p;
            int suma=0;
            Set<Set<Integer>> temp = Sets.combinations(set,licz);
            for (Set<Integer> z: temp) {
                for (int k:z)
                {
                    for(int i=0;i<graf.length;i++){
                        if(k==i)continue;
                        if(!z.contains(i))suma=suma+graf[k][i];
                    }
                }
                if(suma<=p)return false;
            }
        }
        return true;
    }

    public static void wypiszTablice(int num, int[][] graf){
        for(int i=0;i<num;i++) {
            for(int j=0;j<num;j++){
                if(i==j)System.out.print("0 ");
                else System.out.print(graf[i][j]+" ");
            }
            System.out.println("");
        }
    }
}