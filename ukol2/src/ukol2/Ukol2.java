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
            System.out.print("Nebyly zadány parametry vstupu a výstupu\n");
            System.exit(-1);
        }

/**  Vytvoření dvourozměrného pole coor, do kterého se uloží jednotlivé
*    načtené hodnoty - volání metody nacteni.
*/
        double coor[][] = nacteni(args);

/** proměnná exponent je defaultně nastavená na 2
 */
        double exponent = 2;
        exponent = parametr(args,exponent);

/**  Vytvoření proměnných, které reprezentují mřížku a následné volání
 */
        double barsx []= new double[100];
        double barsy []= new double [100];
        maxmin(coor,barsx,barsy);
        
/**  Vytvoření konečného pole result, do kterého se uloží výsledná tabulka
 */
        double result [][] = new double[100][100];
        distance(coor,barsx,barsy,result,exponent);

        zapis(args,result);   
    }
/** Metoda výpočtu idw
*   první cyklus vypočte koeficient k, druhý cyklus použije vypočtený koeficient,
*   vytvoří vážené vzdálenosti a vypočte výslednou hodnotu interpolovaného bodu
*   @param dist - pole vypočtených vzdáleností
*   @param coor - dvourozměrné pole se souřadnicemi a hodnotami
*   @param exponent - exponent
*   @return truevalue - hodnota interpolovaného bodu
*/ 
    public static double idw(double dist[],double coor[][],double exponent){
/**     Vytvoření pomocných proměnných
*/
        double weightdist;
        double truevalue = 0;
        double k =0;
        for (int i =0; i < coor[0].length;i++){
             if (dist[i] == 0){
                return coor[2][i];
            }
            k =k + (1/Math.pow(dist[i],exponent));
        }
        for (int i = 0; i< coor[0].length;i++){
            weightdist = (1/Math.pow(dist[i],exponent)) * (1/k);
            truevalue = truevalue + (weightdist * coor[2][i]); 
        }
        return truevalue;
    }
/**Metoda, která počítá vzdálenosti načtených bodů od bodu interpolovaného a
*   volá metodu pro výpočet IDW.
*   @param exponent - exponent
*   @param coor - dvourozměrné pole se souřadnicemi a hodnotami
*   @param barsy - pole x souřadnic mřížky 
*   @param barsx - pole y souřadnic mřížky
*   @param result - dvourozměrné pole, do kterého se uloží výsledné hodnoty
*  Metoda po výpočtu vzdálenosti volá metodu idw, která vrátí hodnotu, jenž je
*  uložena do pole result. Při vzdálenosti 0 je bodu přiřazena hodnota bodu
*  vstupního - jedná se o ten samý bod
*/
    public static void distance(double coor[][],double barsx [],double barsy [],
            double result[][],double exponent){
        double dist[]= new double [coor[0].length];
        int u =0;
        for (int j =0;j<(100+1);j++){
            if(j==100){
                u = u +1;
                j=0;
            }
            if (u == 100){
                break;
            }
            for(int i = 0; i < coor[0].length;i++) {
                dist[i] = Math.sqrt((coor[0][i] - barsx[j])*(coor[0][i]
                        - barsx[j]) + (coor[1][i] - barsy[u])*(coor[1][i]
                                -barsy[u]));
            }
                result[u][j]= idw(dist,coor,exponent);
        }
    }
    
/** Metoda, která najde minimum a maximu načtených souřadnic x a y
*   a vytvoří mřížku
*   @param coor - dvourozměrné pole se souřadnicemi a hodnotami
*   @param barsy - pole x souřadnic mřížky 
*   @param barsx - pole y souřadnic mřížky
*/
    public static void maxmin(double coor[][],double barsx [],
            double barsy [])
    {
        double maxmin[] = {coor[0][0],coor[1][0],coor[0][0],coor[1][0]};
        
        for(int j =0;j<2;j++){
            
            for(int i = 0;i < coor[0].length;i++){
                if( maxmin[0+j] < coor[0+j][i]){ 
                    maxmin[0+j] = coor[0+j][i];
                }
                if( maxmin[2+j] > coor[0+j][i]){ 
                    maxmin[2+j] = coor[0+j][i];
                }
            }
        }
        double rx = maxmin[0]- maxmin[2];
        double ry = maxmin[1]- maxmin[3];
        barsx[0] = maxmin[2];
        barsy[0] = maxmin[3];
        for(int i =1;i<100;i++){
            double h = i; 
            barsx[i] = (h/99*rx + maxmin[2]);
            barsy[i] = (h/99*ry + maxmin[3]);
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
    public static void zapis(String[]args,double result[][]){
        PrintWriter a;
        try {
            Locale.setDefault(Locale.US);
            a = new PrintWriter(args[args.length-1]);
            for(int i =0;i < 100;i++){
                for(int j =0;j<100;j++){
                    a.format("%.2f ,", result[i][j]);
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
