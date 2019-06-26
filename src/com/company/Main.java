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
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int[][] redukuj(int[][] graf, int m){
        List<Integer> indeksy = new ArrayList<>();
        int max_j=-1;
        int k;
        boolean petla;
        boolean stop=diagonKoniec(graf, m);

        while(stop){
            max_j=indeksMax(graf, max_j);
            k=0;
            petla=true;
            while(petla){
                //if(max_j==0)return graf;
                if(graf[max_j][k]==1){
                    if(sprawdzDiagon(graf,m,k)) {
                        if(max_j==k)continue;
                        graf[max_j][k]=0;
                        indeksy.add(k);
                        petla=false;
                    }
                    else k=k+1;
                }
                else k=k+1;
                if(k>7)petla=false;
            }
            if(!sprawdzWystarczajacy(graf,m)){
                for(Integer index : indeksy){
                    graf[max_j][index]=1;
                }
                indeksy.clear();
            }
            stop=diagonKoniec(graf, m);
        }
        return graf;
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

    public static int indeksMax( int[][] graf, int p){
        int suma =0;
        int temp_suma=0;
        int temp_j=0;
        for (int j = 0; j < graf.length; j++) {
            for (int i = 0; i < graf.length; i++) {
                if(i==j) continue; //Nie sprawdzamy skosów
                suma=suma+graf[j][i];
            }
            if(suma>temp_suma){
               temp_suma=suma;
               temp_j=j;
            }
            /*
            if((suma==temp_suma)&&(temp_j==p)){
                temp_j=j;
            }*/
            suma=0;
        }
        return temp_j;
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