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
/**     ošetření proti pádu, když nejsou zadány argumenty 
 */
        if (args.length  <= 1){
            System.out.print("Nebyly zadány parametry vstupu a výstupu");
            System.exit(-1);
        }

/**  Vytvoření dvourozměrného pole souradnice, do kterého se uloží jednotlivé
*    načtené hodnoty - volání metody nacteni.
*/
        double souradnice[][] = nacteni(args);
        int radky = souradnice[0].length;

        double x[] = new double [20];
        double y[] = new double [20];
        double value[] = new double [20];
        for(int i =0;i<radky;i++){
            x[i] = souradnice[0][i];
            y[i] = souradnice[1][i];
            value[i] = souradnice[2][i];
        }

/** proměnná exponent je defaultně nastavená na 2
 */
        double exponent = 2;
        exponent = parametr(args,exponent);

/**  Vytvoření proměnných, které reprezentují mřížku a následné volání
 */
        double barsx []= new double[100];
        double barsy []= new double [100];
        maxmin(x,y,radky,barsx,barsy);
        
/**  Vytvoření konečného pole result, do kterého se uloží výsledná tabulka
 */
        double result [] = new double[100*100];
        distance(x,y,barsx,barsy,result,value,radky,exponent);

        zápis(args,result);   
    }
/** Metoda výpočtu idw
*   první cyklus vypočte koeficient k, druhý cyklus použije vypočtený koeficient,
*   vytvoří vážené vzdálenosti a vypočte výslednou hodnotu interpolovaného bodu
*   @param dist - pole vypočtených vzdáleností
*   @param value - pole s hodnotami jednotlivých bodů
*   @param radky - počet řádků
*   @param exponent - exponent
*   @return truevalue - hodnota interpolovaného bodu
*/ 
    public static double idw(double dist[],double value[],int radky, 
            double exponent){
/**     Vytvoření pomocných proměnných
*/
        double weightdist;
        double truevalue = 0;
        double k =0;
        for (int i =0; i < radky;i++){
            k =k + (1/Math.pow(dist[i],exponent));
        }
        for (int i = 0; i< radky;i++){
            weightdist = (1/Math.pow(dist[i],exponent)) * (1/k);
            truevalue = truevalue + (weightdist * value[i]); 
        }
        return truevalue;
    }
/**Metoda, která počítá vzdálenosti načtených bodů od bodu interpolovaného
*   @param value - pole s hodnotami jednotlivých bodů
*   @param radky - počet řádků
*   @param exponent - exponent
*   @param x - pole x souřadnic načtených bodů
*   @param y - pole y souřadnic načtených bodů
*   @param barsy - pole x souřadnic mřížky 
*   @param barsx - pole y souřadnic mřížky
*   @param result - pole do kterého se uloží výsledné hodnoty
*  Metoda po výpočtu vzdálenosti volá metodu idw, která vrátí hodnotu, jenž je
*  uložena do pole result. Při vzdálenosti 0 je bodu přiřazena hodnota bodu
*  vstupního - jedná se o ten samí bod
*/
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
    
/** Metoda, která najde minimum a maximu načtených souřadnic x a y
*   a vytvoří mřížku
*   @param x - pole x souřadnic načtených bodů
*   @param y - pole y souřadnic načtených bodů
*   @param barsy - pole x souřadnic mřížky 
*   @param barsx - pole y souřadnic mřížky
*   @param radky - počet řádků
*/
    public static void maxmin(double x[],double y[],int radky,
            double barsx [],double barsy [])
    {
        double xy []= new double [radky*2];
        for(int i =0;i<x.length;i++){
            xy [i*2] =x[i];
            xy [(i*2)+1] =y[i];
        }
        double maxmin[] = {x[0],y[0],x[0],y[0]};
        for(int j =0;j<2;j++){
            for(int i = 0;i < x.length;i++){
                if( maxmin[0+j] < xy[(i*2)+j]){ 
                    maxmin[0+j] = xy[(i*2)+j];
                }
                if( maxmin[2+j] > xy[(i*2)+j]){ 
                    maxmin[2+j] = xy[(i*2)+j];
                }
            }
        }
        double rx = maxmin[0]- maxmin[2];
        double ry = maxmin[1]- maxmin[3];
        rx = rx/(100-1);
        ry = ry/(100-1);
        barsx[0] = maxmin[2];
        barsy[0] = maxmin[3];
        for(int m = 1;m < 100;m++){
            barsx[m] = barsx[m-1] + rx;
            barsy[m] = barsy[m-1] + ry;
        }
    }
       
/** Metoda, která najde parametr "-p" a naparsuje číslo, které
*   se nachází za parametrem "-p", danou hodnotu vrátí, pokud "-p" nenajde
*   vrátí defaultní hodnotu 2
*   @param exponent - exponent
*   @param args - string argumentů
*   @return exponent
*/     
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
    
/**   Metoda, která načte první řádek a naparsuje ho, dále načte
 *    jednotlivě řádky se souřadnicemi a rozláme je podle
*     čárky. Následně parsuje načtené hodnoty a naplní jimi pole p,
*     catch zachytává možné výjimky a ukončuje program přes System.exit(-1)
*     @param args - string argumentů
*     @return p - vrací pole
*/    
    public static double[][] nacteni(String[] args){
        try { 
            BufferedReader vstup = new BufferedReader(new FileReader
            (args[args.length-2]));
            String r = vstup.readLine();
            String [] roww = r.split(",");
            int radky = Integer.parseInt(roww[0]);
            double p[][] = new double [3][radky];
            String row;
            String [] items;
            for(int i=0;i < radky;i++){
                row = vstup.readLine();
                items = row.split(",");
                p[0][i] = Double.parseDouble(items[0]);
                p[1][i] = Double.parseDouble(items[1]);
                p[2][i] = Double.parseDouble(items[2]);
            }
            return p;      
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
        return null;
    }
    /** Metoda, která otevře výstupní soubor, a zapíše do něj výsledky
     * @param args
     * @param result 
     */
    public static void zápis(String[]args,double result[]){
        PrintWriter a;
        try {
            Locale.setDefault(Locale.US);
            a = new PrintWriter(args[args.length-1]);
            for(int i =0;i < 100;i++){
                for(int j =0;j<100;j++){
                    a.format("%.2f ;", result[j+(i*100)]);
                }
               a.println();
            }
            a.close();
        }
        catch(FileNotFoundException ex){
            System.out.format("Soubor %s nebyl nalezen\n",args[args.length-1]);
            System.exit(-1);
        }
    }
}
