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
//      ošetření proti pádu bez argumentů
        if (args.length  <= 1){
            System.out.print("Nebyly zadány parametry vstupu a výstupu");
            System.exit(-1);
        }
        int radky = nacteni1radku(args);
//      vytvoření pole p o velikosti počtu řádků * počet sloupců
        double x[] = new double [radky];
        double y[] = new double [radky];
        double value[] = new double [radky];
        
        nactenizbytku(args,radky,x,y,value);

//      proměnná exp je defaultně nastavená na 2
        double exponent = 2;
        exponent = parametr(args,exponent);


        
//      Vytvoření proměnných, které reprezentují mřížku
        double barsx []= new double[100];
        double barsy []= new double [100];
        maxmin(x,y,radky,barsx,barsy);

//      Vytvoření konečného pole save, do kterého se uloží výsledná tabulka
        double result [] = new double[100*100];
        distance(x,y,barsx,barsy,result,value,radky,exponent);

//      Zápis do výstupního souboru
        PrintWriter a;
        try {
            Locale.setDefault(Locale.US);
            a = new PrintWriter(args[args.length-1]);
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
            System.out.format("Soubor %s nebyl nalezen\n",args[args.length-1]);
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
    public static double idw(double dist[],double value[],int radky, 
            double exponent){
//      Vytvoření pomocných proměnných
        double weightdist[] = new double[radky];
        double helpdist[]= new double [radky];
        double truevalue = 0;
        double k =0;
        for (int i =0; i < radky;i++){
            k =k + (1/Math.pow(dist[i],exponent));
            helpdist[i] = (1/Math.pow(dist[i],exponent));
        }
        for (int i = 0; i< radky;i++){
            weightdist[i] = helpdist[i] * (1/k);
            
        }
        for (int i =0;i < radky;i++){
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
            barsx [],double barsy [],double result[], double value[],int radky,
            double exponent){
        double dist[]= new double [radky];
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
            for(int i = 0; i < radky;i++) {
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
            result[m]= idw(dist,value,radky,exponent);
            }
            m++;
        }
    }
    
//  Metoda, která najde minimum a maximu načtených souřadnic x, a následně  
//  naplní pole barsx hodnotami se stejným rozdílem od min do max. Potom 
//  proces zopakuje pro souřadnice y
//  Vstup: pole p, počet řádků c, pole mřížových souřadnic x a y 
//  Výstup: nic
    public static void maxmin(double x[],double y[],int radky,
            double barsx [],double barsy [])
    {
        double max = 0;
        double min = 0;
        for(int j =0;j<2;j++){
            if(j ==0){
                max =x[j];
                min= x[j];
                for(int i = 0;i < radky;i++){
                    if( max >= x[i]){ 
                    }
                    else{
                    max = x[i];
                    }
                    if( min <= x[i]){ 
                    }
                    else{
                    min = x[i];
                    }
                }
                double r = max- min;
                r = r/(100-1);
                barsx[0] = min;
                for(int m = 1;m < 100;m++){
                    barsx[m] = barsx[m-1] + r;
                }
            }  
            else{
                max = y[j-1];
                min = y[j-1];
                for(int i = 0;i < radky;i++){
                    if( max >= y[i]){ 
                    }
                    else{
                    max = y[i];
                    }
                    if( min <= y[i]){ 
                    }
                    else{
                    min = y[i];
                    }
                }
                double r = max- min;
                r = r/(100-1);
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
    public static double parametr(String[] args,double exponent){
        try {
            for(int i = 0;i < args.length;i++){
                if("-p".equals(args[i])){
                    exponent = Double.parseDouble(args[i+1]);
                }
                if (exponent <= 0){
                    System.out.print("exponent nemůže být menší nebo rovno 0, "
                        + "bude použit defaultní exponent 2");
                    exponent = 2;
                }
                return exponent;
            }
        }
        catch(NumberFormatException ex){
            System.out.print("nalezeny chybné znaky, bude použit defaultní"
                    + " exponent 2\n");
            exponent = 2;
        }
        return exponent;
    }
//      try blok pro zjištění počtu řádků, catch zachytává všechny možné výjimky
//      a ukončuje program přes System.exit(-1)
//      Vytvoření proměnné c, do které uložím počet řádků
    public static int nacteni1radku(String[] args){
        try { 
            BufferedReader vstup = new BufferedReader(new FileReader
            (args[args.length-2]));
            String r = vstup.readLine();
            String [] roww = r.split(",");
            return Integer.parseInt(roww[0]);
        }
        catch(NumberFormatException ex){
            System.out.print("nalezeny chybné znaky\n");
            System.exit(-1);
        }
        catch(FileNotFoundException ex){
            System.out.format("Soubor %s nebyl nalezen\n",args[args.length-2]);
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
//  Metoda, která převede načtená čísla na hodnoty, které jim odpovídají
//  "4" = 4 
    public static void nactenizbytku(String[] args,int radky, double x[],
            double y[],double value[]){
        try{
            BufferedReader vstup = new BufferedReader(new FileReader
            (args[args.length-2]));
            String row;
            String [] items;
            int k = 0;
            vstup.readLine();
            for(int i=0;i < radky;i++){
                row = vstup.readLine();
                items = row.split(",");
                x[k] = Double.parseDouble(items[0]);
                y[k] = Double.parseDouble(items[1]);
                value[k] = Double.parseDouble(items[2]);
                k++;
            }
        }
        catch(NumberFormatException ex){
            System.out.print("nalezeny chybné znaky\n");
                System.exit(-1);
        }
        catch(FileNotFoundException ex){
            System.out.format("Soubor %s nebyl nalezen\n",args[args.length-2]);
            System.exit(-1);
        }
        catch(IOException ex){
            System.out.print("Chyba při načítání řádku\n");
            System.exit(-1);
        }
    }
}
