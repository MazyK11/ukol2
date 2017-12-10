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
import java.util.Locale;

/**
 *
 * @author MazyK
 */
public class Ukol2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//      do proměnné delka si uložím počet parametrů v args 
        int delka = args.length; 
        int c = nacteni1radku(args,delka);
//      vytvoření pole p o velikosti počtu řádků * počet sloupců
        double p[] = new double[c*3];
        nactenizbytku(args,delka,p,c);  
//      proměnná exp je defaultně nastavená na 2
        double exp = 2;
        exp = parametr(args,exp);

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
        double result [] = new double[100*100];
        distance(x,y,barsx,barsy,result,value,c,exp);

//      Zápis do výstupního souboru
        PrintWriter a;
        try {
            Locale.setDefault(Locale.US);
            a = new PrintWriter(args[delka-1]);
            for(int i =0;i < 100;i++){
                for(int j =0;j<100;j++){
                    a.format("%.2f ;", result[j+(i*100)]);
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
//  Metoda výpočtu idw
//  vstupy: pole vypočtených vzdáleností, pole s hodnotamijednotlivých bodů,
//  počet řádků a exponent.
//  Výstup: hodnota interpolovaného bodu
//  první cyklus vypočte koeficient k, druhý cyklus použije vypočtený koeficient
//  a vytvoří vážené vzdálenosti, třetí cyklus vypočte výslednou hodnotu
//  interpolovaného bodu
    public static double idw(double dist[],double value[],int c, double exp){
//      Vytvoření pomocných proměnných
        double weightdist[] = new double[c];
        double helpdist[]= new double [c];
        double truevalue = 0;
        double k =0;
        for (int i =0; i < c;i++){
            k =k + (1/Math.pow(dist[i],exp));
            helpdist[i] = (1/Math.pow(dist[i],exp));
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
//  výsledné pole save, hodnoty načtených bodů, počet řádků a exponent
//  Výstup: žádný.
//  Metoda po výpočtu vzdálenosti volá metodu idw, která vrátí hodnotu, jenž je
//  uložena do pole save. Při vzdálenosti 0 je bodu přiřazena hodnota bodu
//  vstupního - jedná se o ten samí bod
    public static void distance(double x[], double y[],double
            barsx [],double barsy [],double result[], double value[],int c,
            double exp){
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
                    result[m] = value[i];
                    count = i;
                    break;
                }
            }
            if(result[m] == value[count]){
            }
            else {
            result[m]= idw(dist,value,c,exp);
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
//      Metoda, která najde parametr "-p" a naparsuje číslo, které
//      se nachází za parametrem "-p", danou hodnotu vrátí, pokud "-p" nenajde
//      vrátí defaultní hodnotu 2
//      Vstup: delka parametrů, parametry a exponent
//      Výstup: hodnota exponentu
//      pokud bude zadán nekorektní vstup program použije defaultní exponent 2
    public static double parametr(String[] args,double exp){
        try {
            for(int i = 0;i < args.length;i++){
                if("-p".equals(args[i])){
                    exp = Double.parseDouble(args[i+1]);
                }
                if (exp <= 0){
                    System.out.print("exponent nemůže být menší nebo rovno 0, "
                        + "bude použit defaultní exponent 2");
                    exp = 2;
                }
                return exp;
            }
        }
        catch(NumberFormatException ex){
            System.out.print("nalezeny chybné znaky, bude použit defaultní"
                    + " exponent 2\n");
            exp = 2;
        }
        return exp;
    }
//      try blok pro zjištění počtu řádků, catch zachytává všechny možné výjimky
//      a ukončuje program přes System.exit(-1)
//      Vytvoření proměnné c, do které uložím počet řádků
    public static int nacteni1radku(String[] args, int delka){
        try { 
            BufferedReader vstup = new BufferedReader(new FileReader
            (args[delka-2]));
            String r = vstup.readLine();
            String [] roww = r.split(",");
            return Integer.parseInt(roww[0]);
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
        return 0;
    }
//      try - na načtení řádků a naplnění pole items 
//      následné volání metody, která parsuje načtené hodnoty a naplní jimi 
//      pole p
    public static void nactenizbytku(String[] args, int delka, double p[],
            int c){
        try{
            BufferedReader vstup = new BufferedReader(new FileReader
            (args[delka-2]));
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
    }
}
