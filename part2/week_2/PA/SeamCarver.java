import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

   // private Picture p;
   private double[][] energy; // energy for each pixel
   private int[][] pixels; // each element is a color code
   private int width, height;
   //
   /*
   private boolean cache_valid; // has underlying content changed
   private int[] vertical_seam; //cache
   private int[] horizontal_seam; //cache
   */
   // pixels[col][row]

   // create a seam carver object based on the given picture
   public SeamCarver(Picture picture) {
      if (picture == null)
         throw new IllegalArgumentException("null argument");
      // fill the pixels;
      width = picture.width();
      height = picture.height();
      pixels = new int[width][height];
      energy = new double[width][height];
      for (int i = 0; i < width; i++)
         for (int j = 0; j < height; j++) {
            pixels[i][j] = picture.getRGB(i, j);
            //energy[i][j] = 1000.0;
         }
      //if (width > 2 && height > 2)
      computeEnergy(pixels, energy);
   }

   //border energy: 1000.0
   private void computeEnergy(int[][] pixels, double[][] energy) {
      // ignore border pixels
      for (int col = 0; col < width; col++)
         for (int row = 0; row < height; row++)
            energy[col][row] = getEnergy(pixels, col, row);
   }
   private double getEnergy(int[][] pixels, int col, int row) {
      if ((col == 0 || col == width-1) || (row == 0 || row == height-1))
         return 1000.0;
      else {
         double tmp = delta_x_square(pixels, col, row) + delta_y_square(pixels, col, row);
         return Math.sqrt(tmp);
      }
   }
   private double delta_x_square(int[][] pixels, int col, int row) {
      // assume [col][row] is not border
      int rgb_left = pixels[col-1][row];
      int rl = (rgb_left >> 16) & 0xFF;
      int gl = (rgb_left >>  8) & 0xFF;
      int bl = (rgb_left >>  0) & 0xFF;

      int rgb_right = pixels[col+1][row];
      int rr = (rgb_right >> 16) & 0xFF;
      int gr = (rgb_right >>  8) & 0xFF;
      int br = (rgb_right >>  0) & 0xFF;
      
      return Math.pow((rl-rr), 2) + Math.pow((gl-gr), 2) + Math.pow((bl-br), 2);
   }
   private double delta_y_square(int[][] pixels, int col, int row) {
      // assume [col][row] is not border
      int rgb_up = pixels[col][row-1];
      int ru = (rgb_up >> 16) & 0xFF;
      int gu = (rgb_up >>  8) & 0xFF;
      int bu = (rgb_up >>  0) & 0xFF;

      int rgb_down = pixels[col][row+1];
      int rd = (rgb_down >> 16) & 0xFF;
      int gd = (rgb_down >>  8) & 0xFF;
      int bd = (rgb_down >>  0) & 0xFF;
      
      return Math.pow((ru-rd), 2) + Math.pow((gu-gd), 2) + Math.pow((bu-bd), 2);
   }
   // current picture
   public Picture picture() {
      Picture p = new Picture(width, height);
      for (int i = 0; i < width; i++)
         for (int j = 0; j < height; j++) {
            p.setRGB(i, j, pixels[i][j]);
         }
      return p;
   }

   // width of current picture
   public int width() {
      return width;
   }

   // height of current picture
   public int height() {
      return height;
   }

   private boolean within_width(int x) {
      if ((x <= width-1) && (x >= 0))
         return true;
      else
         return false;
   }
   private boolean within_height(int y) {
      if ((y <= height-1) && (y >= 0))
         return true;
      else
         return false;
   }

   // energy of pixel at column x and row y
   public double energy(int x, int y) {
      if (!within_width(x) || !within_height(y))
         throw new IllegalArgumentException("Out of image border!");
      return energy[x][y];
   }

   static private void transpose(double[][] original, double[][] after, int width, int height) {
      // assume space has been allocated
      // original is int[width][height], after is [height][width]
      for (int col = 0; col < width; col++)
         for (int row = 0; row < height; row++)
            after[height-row-1][col] = original[col][row];
   }
   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {
      // transpose the energy
      double[][] energy_T = new double[height][width];
      /*
      for (int col = 0; col < width; col++)
         for (int row = 0; row < height; row++)
            energy_T[height-row-1][col] = energy[col][row];
      */
      transpose(energy, energy_T, width, height);
      int[] path = _findVerticalSeam(energy_T, height, width);
      for (int i = 0; i < path.length; i++)
         path[i] = height-path[i]-1;
      return path;
   }
   
   
   public int[] findVerticalSeam() {
      return _findVerticalSeam(energy, width, height);
   }
   // sequence of cols for vertical seam
   static private int[] _findVerticalSeam(double[][] energy, int width, int height) {
      double[][] distTo = new double[width][height];
      // parent vertex, 0(left up), 1(up), 2(right up)
      int [][] edgeTo = new int[width][height];
      for (int col = 0; col < width; col++)
         for (int row = 0; row < height; row++) {
            distTo[col][row] = Double.POSITIVE_INFINITY;
            edgeTo[col][row] = -1;
         }

      // relax each vertex in topological order
      // relax source
      for (int i = 0; i < width; i++) {
         distTo[i][0] = 1000.0;
         edgeTo[i][0] = 3; // edge from source
      }
      // relax reach vertex
      for (int row = 0; row < height-1; row++)
         for (int col = 0; col < width; col++) { // no not relax bottom vertex
            relax(energy, width, distTo, edgeTo, col, row);
         }
      // find col of min of bottem pixels
      int col = -1;
      double d = Double.POSITIVE_INFINITY;
      for (int i = 0; i < width; i++) {
         if (distTo[i][height-1] < d) {
            col = i;
            d = distTo[i][height-1];
         }
      }
      int[] path = new int[height];
      for (int i = height-1; i > -1; i--) {
         path[i] = col;
         if (edgeTo[col][i] == 0)
            col--;
         else if (edgeTo[col][i] == 2)
            col++;
      }
      return path;
   }
   static private void relax(double[][] energy, int width, double[][] distTo, int[][] edgeTo, int col, int row) {
      //relax edge from vertex:[col][row]
      //int width = energy.length;
      //int height = energy[0].length;
      if (col > 0) {
         if (distTo[col][row] + energy[col-1][row+1] < distTo[col-1][row+1]) {
            distTo[col-1][row+1] = distTo[col][row] + energy[col-1][row+1];
            edgeTo[col-1][row+1] = 2;
         }
      }
      if (distTo[col][row] + energy[col][row+1] < distTo[col][row+1]) {
         distTo[col][row+1] = distTo[col][row] + energy[col][row+1];
         edgeTo[col][row+1] = 1;
      }
      if (col < width-1) {
         if (distTo[col][row] + energy[col+1][row+1] < distTo[col+1][row+1]) {
            distTo[col+1][row+1] = distTo[col][row] + energy[col+1][row+1];
            edgeTo[col+1][row+1] = 0;
         }
      }
   }

   
   // remove horizontal seam from current picture
   // a lot of special case to take care
   public void removeHorizontalSeam(int[] seam) {
      if (height <= 1)
         throw new IllegalArgumentException("image height = 1");
      if (seam == null)
         throw new IllegalArgumentException("null argument");
      if (seam.length != width)
         throw new IllegalArgumentException("invalid seam");
      for (int i = 0; i < seam.length; i++) {
         if (!within_height(seam[i]))
            throw new IllegalArgumentException("invalid seam");
         if (i < seam.length-1)
            if (Math.abs(seam[i+1] - seam[i]) > 1)
            throw new IllegalArgumentException("invalid seam");
      }
      //manipulate pixels and energy instance variable

      // update pixels
      for (int col = 0; col < seam.length; col++) {
         // move up pixels
         if (seam[col] < height-1) {
            for (int row = seam[col]+1; row < height; row++) {
               pixels[col][row-1] = pixels[col][row];
               energy[col][row-1] = energy[col][row];
            }
         }
      }
      height--;
      // update energy
      for (int col = 1; col < seam.length-1; col++) {
         if (seam[col]-1 >= 0)
            energy[col][seam[col]-1] = getEnergy(pixels, col, seam[col]-1);
         if (seam[col] <= height-1)
            energy[col][seam[col]] = getEnergy(pixels, col, seam[col]);

      }
   }

   // remove vertical seam from current picture
   public void removeVerticalSeam(int[] seam) {
      if (width <= 1)
         throw new IllegalArgumentException("image width = 1");
      if (seam == null)
         throw new IllegalArgumentException("null argument");
      if (seam.length != height)
         throw new IllegalArgumentException("invalid seam");
      for (int i = 0; i < seam.length; i++) {
         if (!within_width(seam[i]))
            throw new IllegalArgumentException("invalid seam");
         if (i < seam.length-1)
            if (Math.abs(seam[i+1] - seam[i]) > 1)
            throw new IllegalArgumentException("invalid seam");
      }
      //manipulate pixels and energy instance variable

      // update pixels
      for (int row = 0; row < seam.length; row++) {
         // move left pixels
         if (seam[row] < width-1) {
            for (int col = seam[row]+1; col < width; col++) {
               pixels[col-1][row] = pixels[col][row];
               energy[col-1][row] = energy[col][row];
            }
         }
      }
      width--;
      // update energy
      for (int row = 1; row < seam.length-1; row++) {
         // 
         if (seam[row]-1 >= 0)
            energy[seam[row]-1][row] = getEnergy(pixels, seam[row]-1, row);
         if (seam[row] <= width-1)
            energy[seam[row]][row] = getEnergy(pixels, seam[row], row);

      }
   }
   

   //  unit testing (optional)
   public static void main(String[] args) {
      Picture picture = new Picture(args[0]);
      StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
              
      SeamCarver sc = new SeamCarver(picture);
      int[] seam = {0, 1, 1, 1, 1, 1, 0};
      sc.removeHorizontalSeam(seam);
      /*
      StdOut.printf("Displaying energy calculated for each pixel.\n");
      for (int row = 0; row < picture.height(); row++) {
         for (int col = 0; col < picture.width(); col++)
            StdOut.print((int)sc.energy(col, row) + " ");
         StdOut.println();
      }
      int[] vertical_seam = sc.findVerticalSeam();
      int[] horizontal_seam = sc.findHorizontalSeam();
      StdOut.println("vertical seam: ");
      for (int i = 0; i < vertical_seam.length; i++)
         StdOut.println(vertical_seam[i]);
      
      StdOut.println("horizontal seam: ");
      for (int i = 0; i < horizontal_seam.length; i++)
         StdOut.println(horizontal_seam[i]);
      */
      
   }

}
