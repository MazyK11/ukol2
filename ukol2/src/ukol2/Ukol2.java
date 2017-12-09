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
        double barsx []= new double[100];
        double barsy []= new double [100];
        maxmin(p,c,barsx,barsy);

//        System.out.print("\n");
//        for(int i = 0;i < 100;i++){
//            System.out.println(barsy[i]);
//        }
//        System.out.print("\n");
//        for(int i = 0;i < c;i++){
//            System.out.println(y[i]);
//        }
//        System.out.print("\n");
//       for(int i = 0;i < c;i++){
//            System.out.println(value[i]);
//        }

        double save [] = new double[100*100];
        double dist[]= new double [c];
        double weightdist[] = new double[c];
        double helpdist[]= new double [c];
        double truevalue = 0;
        
        distance(x,y,dist,barsx,barsy,save,weightdist,truevalue,value,
                helpdist,c);
        for(int i= 0;i < 100*100;i++){
        System.out.format("%.2f\n",save[i]);
        }
        
        PrintWriter a;
        try {
            a = new PrintWriter(args[1]);
            for(int i =0;i < 100;i++){
                for(int j =0;j<100;j++){
                    a.format("%.2f ;", save[j+(i*100)]);
                }
               a.println();
            }
            a.close();
        }
        catch(FileNotFoundException ex){
            System.out.print("Soubor nebyl nalezen\n");
            System.exit(-1);
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
    
    public static void distance(double x[], double y[],double dist[], double
            barsx [],double barsy [],double save[],double weightdist[],double
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
            save[m]= idw(dist,weightdist,truevalue,value,helpdist,c);
            }
            m++;
        }
    }
    public static void parse(String []items,double []p,int k){
        for(int i = 0;i < items.length;i++){
            p[i+k] = Double.parseDouble(items[i]);
//            System.out.println(p[i+k]);
        }
    }
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
//            System.out.print(min);
//            System.out.print("\n");
//            System.out.print(max);
//            System.out.print("\n");
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
