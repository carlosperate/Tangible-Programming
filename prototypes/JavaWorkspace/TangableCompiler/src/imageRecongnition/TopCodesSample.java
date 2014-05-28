package imageRecongnition;

import webcam.*;
import topcodes.*;
import java.util.List;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;


/**
 * This is a sample application that integrates the webcam library
 * with the TopCode scanner.  This code will only work on windows
 * machines--I tested with XP, but it should work fine with 
 * Vista as well.
 * 
 * To run this sample, you will need a webcamera with VGA (640x480)
 * resolution.  A Logitech QuickCam is a good choice.  Plug in your
 * camera, and then use this command to launch the demo:
 * <blockquote>
 *   $ java -cp lib/topcodes.jar WebCamSample
 * </blockquote>
 *
 * @author Michael Horn
 * @version $Revision: 1.1 $, $Date: 2008/02/04 15:00:59 $
 */
public class TopCodesSample extends JPanel
   implements ActionListener, WindowListener {

   
   /** The main app window */
   protected JFrame frame;

   /** Camera Manager dialog */
   protected WebCam webcam;

   /** TopCode scanner */
   protected Scanner scanner;

   /** Animates display */
   protected Timer animator;
   


   public TopCodesSample() {
      super(true);
      this.frame    = new JFrame("TopCodes Webcam Sample");
      this.webcam   = new WebCam();
      this.scanner  = new Scanner();
      this.animator = new Timer(100, this);  // 10 frames / second

      
      //--------------------------------------------------
      // Set up the application frame
      //--------------------------------------------------
      setOpaque(true);
      setPreferredSize(new Dimension(640, 480));
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      frame.setContentPane(this);
      frame.addWindowListener(this);
      frame.pack();
      frame.setVisible(true);

      
      //--------------------------------------------------
      // Connect to the webcam (this might fail if the
      // camera isn't connected yet).
      //--------------------------------------------------
      try {
         this.webcam.initialize();

         //---------------------------------------------
         // This can be set to other resolutions like
         // (320x240) or (1600x1200) depending on what
         // your camera supports
         //---------------------------------------------
         this.webcam.openCamera(640, 480);
      } catch (Exception x) {
         x.printStackTrace();
      }

      requestFocusInWindow();
      animator.start();
   }


   protected void paintComponent(Graphics graphics) {
      Graphics2D g = (Graphics2D)graphics;
      List<TopCode> codes = null;

      //----------------------------------------------------------
      // Capture a frame from the video stream and scan it for
      // TopCodes. 
      //----------------------------------------------------------
      try {
         if (webcam.isCameraOpen()) {
            webcam.captureFrame();
            codes = scanner.scan(
               webcam.getFrameData(),
               webcam.getFrameWidth(),
               webcam.getFrameHeight());
         }
      } catch (WebCamException wcx) {
         System.err.println(wcx);
      }

      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);
      g.setFont(new Font(null, 0, 12));

      BufferedImage image = scanner.getImage();
      if (image != null) {
         g.drawImage(image, 0, 0, null);
      }

      if (codes != null) {
         for (TopCode top : codes) {

            // Draw the topcode in place
            top.draw(g);

            //--------------------------------------------
            // Draw the topcode ID number below the symbol
            //--------------------------------------------
            String code = String.valueOf(top.getCode());
            int d = (int)top.getDiameter();
            int x = (int)top.getCenterX();
            int y = (int)top.getCenterY();
            int fw = g.getFontMetrics().stringWidth(code);
            
            g.setColor(Color.WHITE);
            g.fillRect((int)(x - fw/2 - 3),
                       (int)(y + d/2 + 6),
                       fw + 6, 12);
            g.setColor(Color.BLACK);
            g.drawString(code, x - fw/2, y + d/2 + 16);
         }
      }
   }


   
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == animator) repaint();
   }

      
/******************************************************************/
/*                        WINDOW EVENTS                           */
/******************************************************************/
   public void windowClosing(WindowEvent e) {
      this.webcam.closeCamera();
      this.webcam.uninitialize();
      frame.setVisible(false);
      frame.dispose();
      System.exit(0);
   }
   
   public void windowActivated(WindowEvent e) { } 
   public void windowClosed(WindowEvent e) { }
   public void windowDeactivated(WindowEvent e) { }
   public void windowDeiconified(WindowEvent e) { } 
   public void windowIconified(WindowEvent e) { } 
   public void windowOpened(WindowEvent e) { }


   public static void main(String[] args) {

      //--------------------------------------------------
      // Fix cursor flicker problem (sort of :( )
      //--------------------------------------------------
      System.setProperty("sun.java2d.noddraw", "");
      
      //--------------------------------------------------
      // Use standard Windows look and feel
      //--------------------------------------------------
      try { 
         UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
      } catch (Exception x) { ; }

      //--------------------------------------------------
      // Schedule a job for the event-dispatching thread:
      // creating and showing this application's GUI.
      //--------------------------------------------------
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               new TopCodesSample();
            }
         });
   }
}
