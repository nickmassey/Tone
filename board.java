package tonePkg;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.*;
import javax.swing.JTextField;
import javax.sound.sampled.*;;

public class board {

	public static void launchMainWindow() throws IOException {
		tone usrTone = new tone();
		JFrame frame = new JFrame();		
		
		final int FRAME_WIDTH = 800;
		final int FRAME_HEIGHT = 400;
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setTitle("ToneMonkey");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		panel.setBackground(Color.darkGray);
		
		BufferedImage testImage = ImageIO.read(new File("./testImage.png"));
		Image resizeTest = testImage.getScaledInstance(400, 150, Image.SCALE_SMOOTH);	
		JLabel label = new JLabel(new ImageIcon(resizeTest));
		c.gridx = 1;
		c.gridy = 0;
		panel.add(label, c);
		
		JButton enterFreq = new JButton("Enter Frequency");
		enterFreq.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e1)
				{
					board.launchFreqWindow(usrTone);
				}
			});
		c.gridx = 0;
		c.gridy = 1;
		panel.add(enterFreq, c);
		
		JButton plotAmp = new JButton("Plot Amplitude");
		plotAmp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e1)
			{
				board.launchAmpWindow(usrTone);
			}		
		});
		c.gridx = 0;
		c.gridy = 2;
		panel.add(plotAmp, c);
		
		JButton selectWav = new JButton("Select .wav File");
		selectWav.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e1)
			{
				board.launchSelectWindow(usrTone);
			}
		});
		c.gridx = 2;
		c.gridy = 1;
		panel.add(selectWav, c);
		
		JButton plotFreq = new JButton("Plot Frequency");
		plotFreq.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e1)
			{
				try {
					board.launchPlotWindow(usrTone);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				
			
		});
		c.gridx = 2;
		c.gridy = 2;
		panel.add(plotFreq, c);
		
		JButton playTone = new JButton("Play Audio Tone");
		playTone.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e1)
			{
				if(usrTone.getFileStatus()) {
					try {
						usrTone.playSound();
						usrTone.setFileInactive();
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(usrTone.getFreqStatus()) {
					try {
						usrTone.playFreq();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
				}
			}
		});
		c.gridx = 1;
		c.gridy = 3;
		panel.add(playTone, c);	
		frame.add(panel);		
		frame.setVisible(true);
	}
	
	public static void launchFreqWindow(tone usrTone) {
		JFrame freqFrame = new JFrame();
		freqFrame.setSize(450, 250);
		freqFrame.setTitle("Frequency Input");
		freqFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel freqPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JTextField freqInput = new JTextField(5);
		JLabel freqLabel = new JLabel("Please enter the desired frequency in Hertz: ",JLabel.LEFT);
		
		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		freqPane.add(freqLabel,c);
		c.gridx = 2;
		freqPane.add(freqInput, c);
		
		JButton enterBtn = new JButton("Enter");
		enterBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e1)
			{
				String freqTextBox = freqInput.getText();
				try {
					double freqCast = Double.parseDouble(freqTextBox);
					usrTone.setFreq(freqCast);
					//System.out.println(usrTone.getFreq());
					usrTone.setFreqActive();
					freqFrame.setVisible(false);
				}
				catch (Exception e) {
					Object[] options = { "OK" };
					JOptionPane.showOptionDialog(null, "Only digits can be entered in this field", "Warning",
					        					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					        					null, options, options[0]);
				}
			}
		});
		c.gridy = 1;
		c.gridx = 1;
		freqPane.add(enterBtn, c);
		
		freqPane.setBackground(Color.lightGray);
		freqFrame.add(freqPane);
		freqFrame.setVisible(true);
	}
	
	public static void launchAmpWindow(tone usrTone) {
		if(usrTone.getFileStatus() && usrTone.getFreqStatus()) {
			Object[] options = {"Use Frequency" , "Use File" };
			int selection = JOptionPane.showOptionDialog(null, "Both a file and frequency have been input, select which to plot:",
															"Plot Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
															null, options, options[0]);
			if(selection == 0) {
				usrTone.plotAmpFreq();
			}
			if(selection == 1) {
				try {
					usrTone.plotAmpFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (usrTone.getFileStatus() && !usrTone.getFreqStatus()) {
			try {
				usrTone.plotAmpFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (!usrTone.getFileStatus() && usrTone.getFreqStatus()) {
			usrTone.plotAmpFreq();
		} else {
			Object[] options = { "OK" };
			JOptionPane.showOptionDialog(null, "Neither frequency or file has been activated.", "Warning",
			        					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
			        					null, options, options[0]);
		}
		
	}
	
	public static void launchPlotWindow(tone usrTone) throws IOException {
		if(usrTone.getFileStatus() && usrTone.getFreqStatus()) {
			Object[] options = {"Use Frequency" , "Use File" };
			int selection = JOptionPane.showOptionDialog(null, "Both a file and frequency have been input, select which to plot:",
															"Plot Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
															null, options, options[0]);
			if(selection == 0) {
				usrTone.plotUsrFreq();
			}
			if(selection == 1) {
				usrTone.plotFileFreq();
			}
		} else if (usrTone.getFileStatus() && !usrTone.getFreqStatus()) {
			try {
				usrTone.plotFileFreq();
				//System.out.println("got to usrtone.plotFF");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (!usrTone.getFileStatus() && usrTone.getFreqStatus()) {
			usrTone.plotUsrFreq();
		} else {
			Object[] options = { "OK" };
			JOptionPane.showOptionDialog(null, "Neither frequency or file has been activated.", "Warning",
			        					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
			        					null, options, options[0]);
		}
		
	}
	
	public static void launchSelectWindow(tone usrTone) {	
		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("wave", "wav");
		j.setFileFilter(filter);		
		
		int returnValue = j.showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File usrFile = j.getSelectedFile();
			usrTone.setFile(usrFile);
			usrTone.setFileActive();
			//System.out.println(usrFile.getAbsolutePath());
		}
		
	}
}
