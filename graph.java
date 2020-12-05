package tonePkg;

import java.awt.*;
import java.util.Arrays;

import javax.swing.*;
  
public class graph extends JPanel {
	private static final long serialVersionUID = 1L;
	double[] data = new double[128];
    final int PAD = 10;
    boolean ampVfile = false;
  
    graph(byte[] buf){
    	for(int i = 0; i <= 127; i++) {
    		this.data[i] = buf[i];
    	}
    }
    
    graph(double[] outreal){
    	for(int i = 0; i <= 127; i++) {
    		this.data[i] = outreal[i];
    	}
    	this.ampVfile = true;
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g2.drawLine(PAD, PAD, PAD, h-PAD);
        if(ampVfile) {
        	g2.drawLine(PAD, h-PAD, w-PAD, h-PAD);
        }
        else {
        	g2.drawLine(PAD, (h/2)-PAD, w-PAD, (h/2)-PAD);
        }
  
        double xScale = (w - PAD)/(data.length);
        
       
        
        double maxValue = data[0];
        if(ampVfile) {
        	 for(int i = 1; i < data.length; i++) {      
             	if(data[i] > maxValue) {
             		maxValue = data[i];
             	}
             }
        }
        else {
        	maxValue = 300;
        }
       
        
        double yScale = (h - 2*PAD)/maxValue;
        int x0 = PAD;
        int y0 = h/2;
        g2.setPaint(Color.red);
        for(int j = 0; j < data.length - 1; j++) {
            int x1 = x0 + (int)(xScale * (j+1));
            int y1 = y0 - (int)(yScale * data[j]);
            int x2 = x0 + (int)(xScale * (j+2));
            int y2 = y0 - (int)(yScale * data[j+1]);
            g2.drawLine(x1, y1, x2, y2);
        }
    }
  
    public static void init(byte[] buf) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(new graph(buf));
        f.setSize(600,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
   
    public static void init(double[] outreal) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(new graph(outreal));
        f.setSize(600,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
    
} 