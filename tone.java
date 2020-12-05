package tonePkg;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.*;

public class tone {
	
	private double frequency;
	private int sampleRate = 8000;
	private int fadeSamples = 80;
	private boolean fileActive = false;
	private boolean freqActive = false;
	private File usrFile;
	
	public  void setFreq(double freqInput) {
		frequency = freqInput;
	}
	
	public double getFreq() {
		return frequency;
	}
	
	public void setFileActive() {
		fileActive = true;
	}
	
	public void setFileInactive() {
		fileActive = false;
	}
	
	public void setFreqActive() {
		freqActive = true;
	}
	
	public void setFreqInactive() {
		freqActive = false;
	}
	
	
	public boolean getFileStatus() {
		return (fileActive);
	}
	
	public boolean getFreqStatus() {
		return (freqActive);
	}
	
	public void setFile(File file) {
		usrFile = file;
	}
	
	public void playSound() throws UnsupportedAudioFileException, IOException {
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(usrFile.getAbsoluteFile());
		Clip clip;
		
		try {
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace(); 	
		}
		
	}
	
	public void playFreq() throws LineUnavailableException {
		byte[] buf = new byte[5000 * Byte.SIZE];
	    for (int i = 0; i < buf.length; i++) {
	        double angle = i / (sampleRate / frequency) * 2 * Math.PI;
	        buf[i] = (byte) (Math.sin(angle) * Byte.MAX_VALUE);
	    }
	    for (int i = 0; i < fadeSamples && i < buf.length / 2; i++) {
	        buf[i] = (byte) (buf[i] * i / fadeSamples);
	        buf[buf.length - 1 - i] = (byte) (buf[buf.length - 1 - i] * i / fadeSamples);
	    }
	    AudioFormat af = new AudioFormat(sampleRate, Byte.SIZE, 1, true, false);
	    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
	    sdl.open(af);
	    sdl.start();
	    sdl.write(buf, 0, buf.length);
	    sdl.drain();
	    sdl.close();
	}		

	public void plotAmpFreq() {
		//System.out.println("Plot Amp using Freq");		
		byte[] buf = new byte[5000 * Byte.SIZE];
	    for (int i = 0; i < buf.length; i++) {
	        double angle = i / (sampleRate / frequency) * 2 * Math.PI;	        
	        buf[i] = (byte) (Math.sin(angle) * Byte.MAX_VALUE);     
	    }
	   graph.init(buf);	
	}
	
	public void plotAmpFile() throws IOException {
		//System.out.println("Plot Amp using File");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(usrFile));

		int read;
		byte[] buff = new byte[1024];
		try {
			while ((read = in.read(buff)) > 0)
			{
			    out.write(buff, 0, read);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		out.flush();
		byte[] audioBytes = out.toByteArray();
		graph.init(audioBytes);
	}

	public void plotUsrFreq() {
		//System.out.println("Plot Amp using Freq");		
		byte[] buf = new byte[5000 * Byte.SIZE];
	    for (int i = 0; i < buf.length; i++) {
	        double angle = i / (sampleRate / frequency) * 2 * Math.PI;	        
	        buf[i] =	 (byte) (Math.sin(angle) * Byte.MAX_VALUE);     
	    }
		
		double[] inreal = new double[128];
		
		for(int i = 0; i < 127; i++)
		{
			inreal[i] = buf[i];
		}
		
		double[] inimag = new double[128];
		double[] outreal = new double[128];
		double[] outimag = new double[128];
		
		computeDft(inreal, inimag, outreal, outimag);
	}

	public void plotFileFreq() throws IOException {
		//System.out.println("Plot Freq using File");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(usrFile));

		int read;
		byte[] buff = new byte[1024];
		try {
			while ((read = in.read(buff)) > 0)
			{
			    out.write(buff, 0, read);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		out.flush();
		byte[] audioBytes = out.toByteArray();
		
		double[] inreal = new double[128];
		
		for(int i = 0; i < 127; i++)
		{
			inreal[i] = audioBytes[i];
		}
		
		double[] inimag = new double[128];
		double[] outreal = new double[128];
		double[] outimag = new double[128];
		
		computeDft(inreal, inimag, outreal, outimag);
	}
	
	public static void computeDft(double[] inreal, double[] inimag, double[] outreal, double[] outimag) {
		int n = inreal.length;
		for (int k = 0; k < n; k++) {  // For each output element
			double sumreal = 0;
			double sumimag = 0;
			for (int t = 0; t < n; t++) {  // For each input element
				double angle = 2 * Math.PI * t * k / n;
				sumreal +=  inreal[t] * Math.cos(angle) + inimag[t] * Math.sin(angle);
				sumimag += -inreal[t] * Math.sin(angle) + inimag[t] * Math.cos(angle);
			}
			outreal[k] = sumreal;
			outimag[k] = sumimag;
		}
		//System.out.println("we got to here");
		graph.init(outreal);
		//System.out.println("past graph init?");
	}
	
	
}


	


