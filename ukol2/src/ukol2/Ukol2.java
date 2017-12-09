/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukol2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author MazyK
 */
public class Ukol2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//      Vytvoření proměnné, do které uložím počet řádků
        int c = 0;
//      try blok pro zjištění počtu řádků, catch zachytává všechny možné výjimky
//      a ukončuje soubor přes System.exit(-1)
        try {
            BufferedReader vstup = new BufferedReader(new FileReader(args[0]));
            String r = vstup.readLine();
            String [] roww = r.split(",");
            c = Integer.parseInt(roww[0]);
        }
        catch(NumberFormatException ex){
            System.out.print("nalezeny chybné znaky\n");
            System.exit(-1);
        }
        catch(FileNotFoundException ex){
            System.out.print("Soubor nebyl nalezen\n");
            System.exit(-1);
        }
        catch(IOException ex){
            System.out.print("Chyba při načítání řádku\n");
            System.exit(-1);
        }
//      vytvoření pole p o velikosti počtu řádků * počet sloupců
        double p[] = new double[c*3];
//      try - na načtení řádků a naplnění pole items 
//      následné volání metody, která parsuje načtené hodnoty a naplní jimi 
//      pole p
        try{
            BufferedReader vstup = new BufferedReader(new FileReader(args[0]));
            String row;
            String [] items;
            int k = 0;
            vstup.readLine();
            for(int i=0;i < c;i++){
                row = vstup.readLine();
                items = row.split(",");
                parse(items,p,k);
                k = k + 3;
            }
        }
        catch(NumberFormatException ex){
            System.out.print("nalezeny chybné znaky\n");
                System.exit(-1);
        }
        catch(FileNotFoundException ex){
            System.out.print("Soubor nebyl nalezen\n");
            System.exit(-1);
        }
        catch(IOException ex){
            System.out.print("Chyba při načítání řádku\n");
            System.exit(-1);
        }
        
//      vytvoření polí, které reprezentují načtené hodnoty - pro 
//      větší přehlednost
        double x[] = new double [c];
        double y[] = new double [c];
        double value[] = new double [c];
//      naplnění polí z pole p
        for(int i =0; i < c;i++){
            x[i] = p[i*3];
            y[i] = p[(i*3)+1];
            value[i] = p[(i*3)+2];
        }
        
//      Vytvoření proměnných, které reprezentují mřížku
        double barsx []= new double[100];
        double barsy []= new double [100];
        maxmin(p,c,barsx,barsy);

//      Vytvoření konečného pole save, do kterého se uloží výsledná tabulka
        double save [] = new double[100*100];
        distance(x,y,barsx,barsy,save,value,c);

//      Zápis do výstupního souboru
        PrintWriter a;
        try {
            a = new PrintWriter(args[1]);
            for(int i =0;i < 100;i++){
                for(int j =0;j<100;j++){
                    a.format("%.2f ;", save[j+(i*100)]);
                }
               a.println();
//          zapisuje se po řádcích, dokud zápis nedosáhne 100 sloupců
//          pak se odřádkuje 
            }
            a.close();
        }
        catch(FileNotFoundException ex){
            System.out.print("Soubor nebyl nalezen\n");
            System.exit(-1);
        }
        
    }
//  metoda výpočtu idw - vstupy: pole vypočtených vzdáleností, pole s hodnotami
//  jednotlivých bodů a počet řádků. Výstup: hodnota interpolovaného bodu
//  první cyklus vypočte koeficient k, druhý cyklus použije vypočtený koeficient
//  a vytvoří vážené vzdálenosti, třetí cyklus vypočte výslednou hodnotu
//  interpolovaného bodu
    public static double idw(double dist[],double value[],int c){
//      Vytvoření pomocných proměnných
        double weightdist[] = new double[c];
        double helpdist[]= new double [c];
        double truevalue = 0;
        double k =0;
        for (int i =0; i < c;i++){
            k =k + (1/Math.pow(dist[i],2));
            helpdist[i] = (1/Math.pow(dist[i],2));
        }
        for (int i = 0; i< c;i++){
            weightdist[i] = helpdist[i] * (1/k);
            
        }
        for (int i =0;i < c;i++){
            truevalue = truevalue + (weightdist[i] * value[i]); 
        }
        return truevalue;
    }
//  Metoda, která počítá vzdálenosti načtených bodů od bodu interpolovaného
//  Vstup: souřadnice načtených bodů x a y, souřadnice mřížových bodů x a y
//  výsledné pole save, hodnoty načtených bodů a počet řádků
//  Výstup: žádný.
//  Metoda po výpočtu vzdálenosti volá metodu idw, která vrátí hodnotu, jenž je
//  uložena do pole save. Při vzdálenosti 0 je bodu přiřazena hodnota bodu
//  vstupního - jedná se o ten samí bod
    public static void distance(double x[], double y[],double
            barsx [],double barsy [],double save[], double value[],int c){
        double dist[]= new double [c];
        int u =0;
        int m =0;
        for (int j =0;j<(100+1);j++){
            if(j==100){
                u = u +1;
                j=0;
            }
            if (u == 100){
                break;
            }
            int count =  0;
            for(int i = 0; i < c;i++) {
                dist[i] = Math.sqrt((x[i] - barsx[j])*(x[i] - barsx[j]) + 
                        (y[i] - barsy[u])*(y[i] -barsy[u]));
                if (dist[i] == 0){
                    save[m] = value[i];
                    count = i;
                    break;
                }
            }
            if(save[m] == value[count]){
            }
            else {
            save[m]= idw(dist,value,c);
            }
            m++;
        }
    }
//  Metoda, která převede načtená čísla na hodnoty, které jim odpovídají
//  "4" = 4 Vstupy: pole p pro převedené hodnoty, pole items s načtenými
//  hodnotami a pomocná proměnná k
//  Výstup: nic 
    public static void parse(String []items,double []p,int k){
        for(int i = 0;i < items.length;i++){
            p[i+k] = Double.parseDouble(items[i]);
        }
    }
//  Metoda, která najde minimum a maximu načtených souřadnic x, a následně  
//  naplní pole barsx hodnotami se stejným rozdílem od min do max. Potom 
//  proces zopakuje pro souřadnice y
//  Vstup: pole p, počet řádků c, pole mřížových souřadnic x a y 
//  Výstup: nic
    public static void maxmin(double p[],int c,double barsx [],double barsy [])
    {
        for(int j =0;j<2;j++){
            double max =p[j];
            for(int i = 0;i < c;i++){
                if( max >= p[(i*3)+j]){ 
                }
                else{
                max = p[(i*3)+j];
                }
            }
            double min = p[j];
            for(int i = 0;i < c;i++){
                if( min <= p[(i*3)+j]){ 
                }
                else{
                    min = p[(i*3)+j];
                }
            }
            double r = max- min;
            r = r/(100-1);
            if (j==0){
                barsx[0] = min;
                for(int m = 1;m < 100;m++){
                    barsx[m] = barsx[m-1] + r;
                }
            }
            else {
                barsy[0] = min;
                for(int m = 1;m < 100;m++){
                    barsy[m] = barsy[m-1] + r;

                }
            }
        }
    }
}
