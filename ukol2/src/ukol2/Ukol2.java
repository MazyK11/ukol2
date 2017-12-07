/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ukol2;

/**
 *
 * @author MazyK
 */
public class Ukol2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int exp = 2;
        int width = 100;
        int high = 100;
        double dist[]= {1,2,2};
        double weightdist[] = new double[3];
        double value[] = {3,5,1};
        double helpdist[]= new double [3];
        double truevalue = 0;
        
        double point = idw(exp,dist,weightdist,truevalue,value,helpdist);
        System.out.print(point);
        
    }
    public static double idw(int exp,double dist[],double weightdist[],double
            truevalue, double value[], double helpdist[]){
        int z0;
        double k =0;
        for (int i =0; i < 3;i++){
            k =k + (1/dist[i]);
            helpdist[i] = (1/dist[i]);
        }
        for (int i = 0; i< 3;i++){
            weightdist[i] = helpdist[i] * (1/k);
            
        }
        for (int i =0;i < 3;i++){
            truevalue = truevalue + (weightdist[i] * value[i]); 
        }
        return truevalue;
    }
}
