/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package seamCarving;

import algs4.Picture;
import java.awt.Color;

/**
 *
 * @author huseyngasimov
 */
public class SeamCarver {
    private Picture pic;
    private int W, H;

    public SeamCarver(Picture picture) {
        pic = picture;
        W = pic.width();
        H = pic.height();
    }
    
    public Picture picture() { return pic; }  // current picture
    public int width() { return pic.width(); } // width of current picture
    public int height() { return pic.height(); } // height of current picture
    
    /*
     * squared gradient wr to x
     */
    private int dx(int x, int y) {
        Color c0 = pic.get(x - 1, y);
        Color c1 = pic.get(x + 1, y);
        return getGrad(c0, c1);
    }
    
    private int dy(int x, int y) {
        Color c0 = pic.get(x, y - 1);
        Color c1 = pic.get(x, y + 1);
        return getGrad(c0, c1);
    }
    
    private int getGrad(Color c0, Color c1) { 
        int rx = c1.getRed() - c0.getRed();
        int gx = c1.getGreen() - c0.getGreen();
        int bx = c1.getBlue() - c0.getBlue();               
        return rx*rx + gx*gx + bx*bx;
    }
    
    // energy of pixel at column x and row y
    public  double energy(int x, int y) {        
        if (!isValid(x, y)) throw new IndexOutOfBoundsException();        
        if (x == 0 || y == 0 || x == W-1 || y == H-1) return 195075;
        return dx(x, y) + dy(x, y); 
    }           
    
    private void calcEnergyRow(int j, double[] ener) {
        //double[] energy = new double[W];
        for(int i = 0; i < W; i++) ener[i] = energy(i, j);
        //return ener;
    }
    
    private void calcEnergyColumn(int i, double[] ener) {        
        for(int j = 0; j < H; j++) ener[j] = energy(i, j);        
    }    
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() { 
        double[] shortestDist = new double[H];
        double[] prev_shortestDist = new double[H];        
        int[][] cameFrom = new int[W][H];               
        
        double[] energy = new double[H];
        calcEnergyColumn(0, energy); // calculate the energy for the 0-th row
        System.arraycopy(energy, 0, shortestDist, 0, H);
        
        for(int j = 1; j < W; j++) {
            System.arraycopy(shortestDist, 0, prev_shortestDist, 0, H);            
            calcEnergyColumn(j, energy);
            
            // caculate the left corner
            shortestDist[0] = prev_shortestDist[0] + energy[0];
            cameFrom[j][0] = 0;
            if (prev_shortestDist[1] < prev_shortestDist[0]) {
                shortestDist[0] = prev_shortestDist[1] + energy[0];
                cameFrom[j][0] = 1;
            }
            
            // calculate the right corner
            shortestDist[H-1] = prev_shortestDist[H-2] + energy[H-1];  
            cameFrom[j][H-1] = H-2;
            if (prev_shortestDist[H-1] < prev_shortestDist[H-2]) {
                shortestDist[H-1] = prev_shortestDist[H-1] + energy[H-1];
                cameFrom[j][H-1] = H-1;
            }
            
            // calculate the middle points
            for(int i = 1; i < H-1; i++) {
                shortestDist[i] = Double.POSITIVE_INFINITY;  
                
                for(int n = -1; n < 2; n++) {
                    if (prev_shortestDist[i+n] + energy[i] < shortestDist[i]) {
                        shortestDist[i] = prev_shortestDist[i+n] + energy[i];
                        cameFrom[j][i] = i + n;
                    }
                }
            }            
        }
        
        // find the end of the min path
        double min = shortestDist[0];
        int min_i = 0;
        for(int i = 0; i < H; i++)
            if (shortestDist[i] < min) {
                min = shortestDist[i];
                min_i = i;
            }
        
        // backtrack from the found end
        int[] res = new int[W];
        res[W-1] = min_i;
        for(int j = W-2; j > -1; j--)
            res[j] = cameFrom[j+1][res[j+1]];
                
        return res;         
    }         
    
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() { 
        double[] shortestDist = new double[W];
        double[] prev_shortestDist = new double[W];        
        int[][] cameFrom = new int[H][W];               
        
        double[] energy = new double[W];
        calcEnergyRow(0, energy); // calculate the energy for the 0-th row
        System.arraycopy(energy, 0, shortestDist, 0, W);
        
        for(int j = 1; j < H; j++) {
            System.arraycopy(shortestDist, 0, prev_shortestDist, 0, W);            
            calcEnergyRow(j, energy);
            
            // caculate the left corner
            shortestDist[0] = prev_shortestDist[0] + energy[0];
            cameFrom[j][0] = 0;
            if (prev_shortestDist[1] < prev_shortestDist[0]) {
                shortestDist[0] = prev_shortestDist[1] + energy[0];
                cameFrom[j][0] = 1;
            }
            
            // calculate the right corner
            shortestDist[W-1] = prev_shortestDist[W-2] + energy[W-1];  
            cameFrom[j][W-1] = W-2;
            if (prev_shortestDist[W-1] < prev_shortestDist[W-2]) {
                shortestDist[W-1] = prev_shortestDist[W-1] + energy[W-1];
                cameFrom[j][W-1] = W-1;
            }
            
            // calculate the middle points
            for(int i = 1; i < W-1; i++) {
                shortestDist[i] = Double.POSITIVE_INFINITY;  
                
                for(int n = -1; n < 2; n++) {
                    if (prev_shortestDist[i+n] + energy[i] < shortestDist[i]) {
                        shortestDist[i] = prev_shortestDist[i+n] + energy[i];
                        cameFrom[j][i] = i + n;
                    }
                }
            }            
        }
        
        // find the end of the min path
        double min = shortestDist[0];
        int min_i = 0;
        for(int i = 0; i < W; i++)
            if (shortestDist[i] < min) {
                min = shortestDist[i];
                min_i = i;
            }
        
        // backtrack from the found end
        int[] res = new int[H];
        res[H-1] = min_i;
        for(int j = H-2; j > -1; j--)
            res[j] = cameFrom[j+1][res[j+1]];
                
        return res; 
    }   
    
    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] a) {
        if (!isRemoveAllowed()) throw new IllegalArgumentException();  
        if (!isValidSeam(a, W, H)) throw new IllegalArgumentException();
        
        Picture newpic = new Picture(W, --H);
        
        for (int i = 0; i < W; i++) { // column
            int j;
            for (j = 0; j < a[i]; j++) // row
                newpic.set(i, j, pic.get(i, j));
            
            for (j = a[i]; j < H; j++)
                newpic.set(i, j, pic.get(i, j+1));
        }
            
        pic = newpic;
    }  
    
    // remove vertical seam from picture
    public void removeVerticalSeam(int[] a) {        
        if (!isRemoveAllowed()) throw new IllegalArgumentException();
        if (!isValidSeam(a, H, W)) throw new IllegalArgumentException();
        
        Picture newpic = new Picture(--W, H);
        
        for (int j = 0; j < H; j++) { // row
            int i;
            for (i = 0; i < a[j]; i++) // column
                newpic.set(i, j, pic.get(i, j));
            
            for (i = a[j]; i < W; i++)
                newpic.set(i, j, pic.get(i+1, j));
        }
            
        pic = newpic;
    }    

    
    public static void main(String[] args) {

    }

    private boolean isValid(int x, int y) {
        return (x > -1 && x < W && y > -1 && y < H);
    }
    
    private boolean isRemoveAllowed() {
        return (W > 1 && H > 1);
    }
    
    private boolean isValidSeam(int[] a, int length, int bound) {
        if (a.length != length) return false;
        
        for (int i = 0; i < a.length; i++) 
            if (a[i] < 0 || a[i] > bound-1) return false;
        
        for (int i = 0; i < a.length-1; i++)
            if (Math.abs(a[i+1] - a[i]) > 1) return false;
        
        return true;
    }
}
