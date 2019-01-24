
 import javax.imageio.ImageIO;
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.geom.Ellipse2D;
 import java.awt.image.BufferedImage;
 import java.io.File;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.Random;



 public class Color_Window extends JPanel  {
     private JFrame window;
     private static BufferedImage pic;
     private JLabel label;
     private static ImageIcon image;
     private final static int Threshold = 72;



     public Color_Window() throws IOException {
         window = new JFrame("Grey Scale");

         pic = ImageIO.read(new File("src/polly_wanna_ColorfuCraker.jpg" ));
         image = new ImageIcon(pic);
         label = new JLabel(image);
         //window.getContentPane().add(label);
         window.getContentPane().add(new JScrollPane(label));
         greyScaleML(label);



     }

     public void run() {
         window.setSize(500, 500);
         //window.setSize(image.getIconWidth(), image.getIconHeight());
         //window.setLocation(100, 100);
         window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         window.setVisible(true);


     }

     /**
      *
      * @param label
      */
     public static void greyScaleML(JLabel label){
         // create a mouseListener to interact with my picture
         label.addMouseListener(new MouseListener() {

             @Override
             public void mouseClicked(MouseEvent mouseEvent) {
                 // work with clicked as it will register after the pixel has been clicked and released

                 int x = mouseEvent.getX(); // gets the X coordinate when clicked
                 int y = mouseEvent.getY();//gets the y coordinate when clicked
                 int pxlColor = pic.getRGB(x, y); // gets the int value of the  color from pixel coordinates

                 long start = System.nanoTime();
                 for (int i = 0; i < pic.getHeight(); i++) {
                     for (int j = 0; j < pic.getWidth(); j++) {

                         int pxlValue = pic.getRGB(j, i);
                         Color pxl = new Color(pxlColor); // get the color of the original pixel
                         Color pxl2 = new Color(pxlValue);
                         double distance = getColorDistance(pxl, pxl2);
                         //System.out.println(distance);
                         if (distance < Threshold) {
                             Color greyScale = greyScale(pxl);
                             pic.setRGB(j, i, greyScale.getRGB());


                         }
                     }
                 }



             ImageIcon image2 = new ImageIcon(pic); //updates image in the frame
                 label.setIcon(image2);
                 long end = System.nanoTime();
                 Double total = Double.valueOf((end - start));
                 Double total2 = total/100_000_000;
                 System.out.println("It took "+ " "+total2+" "+"milli-seconds to convert all of the clicked color.");


         }

             @Override
             public void mousePressed(MouseEvent mouseEvent) {

             }

             @Override
             public void mouseReleased(MouseEvent mouseEvent) {

             }

             @Override
             public void mouseEntered(MouseEvent mouseEvent) {

             }

             @Override
             public void mouseExited(MouseEvent mouseEvent) {

             }
         });

     }

     /**
      *
      * @param c
      * @return
      */
     public static Color greyScale(Color c){
         int gray = (int) ((c.getRed()*0.2126) +(c.getGreen()*0.7152) +(c.getBlue()*0.0722));
         return new Color(gray, gray, gray);
     }
     public static double getColorDistance(Color c1, Color c2){
         long redM = ((long)c1.getRed() - (long) c2.getRed()) /2;
         long red = (long)c1.getRed() - (long) c2.getRed();
         long blue =(long)c1.getBlue() -(long) c2.getBlue();
         long green =(long)c1.getGreen() - (long) c2.getGreen();
         return Math.sqrt((((512+redM)*red*red)>>8) + 4 *green*green +(((767-redM)*blue*blue)>>8) );
     }







 }



