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
        try {
            BufferedReader vstup = new BufferedReader(new FileReader(args[0]));
            int k = 0;
            String r = vstup.readLine();
            String [] roww = r.split(",");
            int c = Integer.parseInt(roww[0]);           
            double p[] = new double[c*3];
            
            for(int i=0;i < c;i++){
                row = vstup.readLine();
                items = row.split(",");
                parse(items,p,k);
                k = k + 3;
            }  
            while ((row = vstup.readLine())!=null){
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
        
        
      
        
        
        
        
        
        
        
        
        
        
        int width = 3;
        int high = 3;
        double barsx []= {1,2,3};
        double barsy []= {1,2,3};
        double save [] = new double[9];
//        double barsx []= new double [width];
//        double barsy []= new double [high];
//        for (int i =0;i<3;i++){
//            barsx[i] = i;
//            barsy[i] = i;
//        }
        double x[] = {10,4,0};
        double y[] = {80,7,3};
        
        double dist[]= new double [3];
        double weightdist[] = new double[3];
        double helpdist[]= new double [3];
        double value[] = {3,5,1};
        double truevalue = 0;
        
        distance(x,y,dist,barsx,barsy,save,weightdist,truevalue,value,helpdist);
        for(int i= 0;i<9;i++){
        System.out.format("%f\n",save[i]);
        }
        
    }
    public static double idw(double dist[],double weightdist[],double
            truevalue, double value[], double helpdist[]){
        double k =0;
        for (int i =0; i < 3;i++){
            k =k + (1/Math.pow(dist[i],2));
            helpdist[i] = (1/Math.pow(dist[i],2));
        }
        for (int i = 0; i< 3;i++){
            weightdist[i] = helpdist[i] * (1/k);
            
        }
        for (int i =0;i < 3;i++){
            truevalue = truevalue + (weightdist[i] * value[i]); 
        }
        return truevalue;
    }
    
    public static void distance(double x[], double y[],double dist[], double
            barsx [],double barsy [],double save[],double weightdist[],double
            truevalue, double value[], double helpdist[]){
        int u =0;
        int m =0;
        for (int j =0;j<(3+1);j++){
            if(j==3){
                u = u +1;
                j=0;
            }
            if (u == 3){
                break;
            }
            for(int i = 0; i < 3;i++) {
                dist[i] = Math.sqrt((x[i] - barsx[j])*(x[i] - barsx[j]) + 
                        (y[i] - barsy[u])*(y[i] -barsy[u])); 
            }
            save[m]= idw(dist,weightdist,truevalue,value,helpdist);
            m++;
        }
    }
    public static void parse(String []items,double []p,int k){
        for(int i = 0;i < items.length;i++){
            p[i+k] = Double.parseDouble(items[i]);
            System.out.println(p[i]);
        }
    }
}
