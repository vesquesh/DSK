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

            if(!sprawdzDiagnozowalnosc(num,graf,m)){
                System.out.println("Warunek konieczny nie spelniony. System nie jest "+m+"-diagnozowalny!");
                System.exit(0);
            }
            else {
                System.out.println("Warunek konieczny spelniony. System jest "+m+"-diagnozowalny!");
            }
            System.out.print("\n");

            //PRZED REDUKCJA
            System.out.println("MACIERZ WEJŚCIOWA:");
            wypiszTablice(num,graf);

            zredukowany = redukuj(graf,m);

            //PO REDUKCJI
            System.out.println("");
            System.out.println("MACIERZ OPTYMALNA:");
            wypiszTablice(num, zredukowany);

            //if(sprawdzWystarczajacyAlfa(zredukowany,m))System.out.println("System jest optymalny");


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //METODA SŁUŻY DO USUWANIA NADMIAROWYCH POŁACZEN MIEDZY WEZLAMI
    public static int[][] redukuj(int[][] graf, int m){
        List<Integer> indeksy = new ArrayList<>();

        //ZEBRANIE DANYCH O WSZYSTKICH ISTNIEJĄCYCH POŁACZENIACH
        for(int j=0;j<graf.length;j++){
            for(int i=0;i<graf[j].length;i++){
                if(i==j)continue;
                if(graf[i][j]==1){
                    indeksy.add(i);  /// Zapamiętanie wszystkich pozycji z 1 w wierszu
                }

                graf[i][j]=0;
                if(!(sprawdzDiagnozowalnosc(graf.length,graf,m))) {
                    graf[i][j]=1;   /// Usuwanie nadmiarowych połączeń
                }
            }

           if(!sprawdzWystarczajacy(graf,m)){
                for(Integer index : indeksy){
                    graf[index][j]=1;     /// Jeśli warunek wystarczający niespełniony, to wycofaj wszystkie zmiany dla danego wiersza
                }
                indeksy.clear();          /// Wyczyść pamięć =
            }

        }

        return graf;
    }

    public static boolean sprawdzDiagnozowalnosc(int num, int[][] graf, int m){
        int sum =0;
        boolean diag = true;
        for (int j = 0; j < num; j++) {
            for (int i = 0; i < num; i++) {
                if(i==j) continue; //Nie sprawdzamy skosów
                sum=sum+graf[i][j];
            }
            if(sum<m)diag=false;
            sum=0;
        }

        return diag;
    }

    public static boolean sprawdzWystarczajacy(int[][] graf, int m){
        Set<Integer> set = new HashSet<Integer>();
        for(int i=0;i<graf.length;i++){
            set.add(i);
        }

        Set<Set<Integer>> comb= new HashSet<>();
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
