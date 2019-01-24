import sun.net.www.content.image.png;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class forkJoinPoolColor_Window {
    private JFrame window;
    private static BufferedImage pic;
    private  JLabel label;
    private static ImageIcon image;
    private final static int Threshold = 78;
    private static  ForkJoinPool joinPool = new ForkJoinPool(4);


    public forkJoinPoolColor_Window() throws IOException {
        window = new JFrame("Grey Scale");
        pic = ImageIO.read(new File("src/polly_wanna_ColorfuCraker.jpg"));

        image = new ImageIcon(pic);
        label = new JLabel(image);
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
     * @param label
     */
    public void greyScaleML(JLabel label) {
        // create a mouseListener to interact with my picture
        label.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                long start = System.nanoTime();

                Color origCol = new Color(pic.getRGB(mouseEvent.getX(), mouseEvent.getY()));

                try {
                    joinPool.invoke(new RA(pic, origCol,0, pic.getHeight(), pic.getHeight() / 76));
                }
                     catch (IOException e) {
                    e.printStackTrace();

                }
                //ImageIcon image2 = new ImageIcon(pic); //updates image in the frame
                //label.setIcon(image2);
                label.repaint();
                long end2 = System.nanoTime();
                Double total = Double.valueOf((end2 - start));
                Double total2 = total / 100_000_000;
                System.out.println("It took " + " " + total2 + " " + "milli-seconds to convert all of the clicked color.");
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
     * @param c
     * @return
     */
    public static Color greyScale(Color c) {
        int gray = (int) ((c.getRed() * 0.2126) + (c.getGreen() * 0.7152) + (c.getBlue() * 0.0722));
        return new Color(gray, gray, gray);
    }

    public static double getColorDistance(Color c1, Color c2) {
        long redM = ((long) c1.getRed() - (long) c2.getRed()) / 2;
        long red = (long) c1.getRed() - (long) c2.getRed();
        long blue = (long) c1.getBlue() - (long) c2.getBlue();
        long green = (long) c1.getGreen() - (long) c2.getGreen();
        return Math.sqrt((((512 + redM) * red * red) >> 8) + 4 * green * green + (((767 - redM) * blue * blue) >> 8));
    }

    public static void getWork(Color origCol, int start, int end) {

        for (int i = start; i < end; i++) {
            for (int j = 0; j < pic.getWidth(); j++) {
                Color pxl2 = new Color(pic.getRGB(j, i));
                double distance = getColorDistance(origCol, pxl2);
                //System.out.println(distance);
                if (distance < Threshold) {
                    Color greyScale = greyScale(pxl2);
                    pic.setRGB(j, i, greyScale.getRGB());
                }
            }


        }




    }

    static class RA extends RecursiveAction {
        public int start = 0;
        public int end ;
        public BufferedImage i;
        public Color origCol;
        public int threshold ;

        public RA(BufferedImage i, Color origCol, int start, int end, int threshold) throws IOException {
            this.i = i;
            this.start = start;
            this.origCol = origCol;
            this.threshold = threshold;
            this.end = end;
        }

        protected void compute() {
            if (end - start > threshold) {
                int mid = (end + start) / 2;

                try {
                    invokeAll(new RA(i, origCol, start, mid, threshold), new RA(i, origCol, mid, end, threshold));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                //do task
                for (int j = 0; j < i.getHeight(); j++) {
                    forkJoinPoolColor_Window.getWork(origCol, start, end);
                }

            }

        }
    }
}
