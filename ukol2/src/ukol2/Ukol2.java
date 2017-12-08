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

/**
 *
 * @author MazyK
 */
public class Ukol2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String row;
        String [] items;
        int c = 0;
        int k = 0;
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
        double p[] = new double[c*3];
        // Další try - na naplnění pole p
        try{
            BufferedReader vstup = new BufferedReader(new FileReader(args[0]));
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
        double x[] = new double [c];
        double y[] = new double [c];
        double value[] = new double [c];
        
        for(int i =0; i < c;i++){
            x[i] = p[i*3];
            y[i] = p[(i*3)+1];
            value[i] = p[(i*3)+2];
        }
//        System.out.print("\n");
//        for(int i = 0;i < c;i++){
//            System.out.println(x[i]);
//        }
//        System.out.print("\n");
//        for(int i = 0;i < c;i++){
//            System.out.println(y[i]);
//        }
//        System.out.print("\n");
//       for(int i = 0;i < c;i++){
//            System.out.println(value[i]);
//        }

        int barsx []= new int[100];
        int barsy []= new int [100];
        double save [] = new double[100*100];
        
        for (int i =0;i<100;i++){
            barsx[i] = i;
            barsy[i] = i;
        }

        double dist[]= new double [c];
        double weightdist[] = new double[c];
        double helpdist[]= new double [c];
        double truevalue = 0;
        
        distance(x,y,dist,barsx,barsy,save,weightdist,truevalue,value,
                helpdist,c);
        for(int i= 0;i < 100*100;i++){
        System.out.format("%f\n",save[i]);
        }
        
    }
    public static double idw(double dist[],double weightdist[],double
            truevalue, double value[], double helpdist[],int c){
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
    
    public static void distance(double x[], double y[],double dist[], int
            barsx [],int barsy [],double save[],double weightdist[],double
            truevalue, double value[], double helpdist[],int c){
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
            for(int i = 0; i < c;i++) {
                dist[i] = Math.sqrt((x[i] - barsx[j])*(x[i] - barsx[j]) + 
                        (y[i] - barsy[u])*(y[i] -barsy[u])); 
            }
            save[m]= idw(dist,weightdist,truevalue,value,helpdist,c);
            m++;
        }
    }
    public static void parse(String []items,double []p,int k){
        for(int i = 0;i < items.length;i++){
            p[i+k] = Double.parseDouble(items[i]);
            System.out.println(p[i+k]);
        }
    }
}
