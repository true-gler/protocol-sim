package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import event.Event;

/**
 * This class handles the LogHandling for writing CSV Data
 * 
 * @author Thomas
 * 
 */

public class LogHandler {
	private static final String csvLogName = "./Logs/log";
	private static int logcounter = 0;
	private FileWriter writer;
	
	

	/**
	 * Instanciate the FileWriter for logging
	 * later adding strings of the form 
	 * String str = layer + "," + e.getInitNode().getId() + "," + action + "," + e.getreceiverNode().getId() + "," + e.getInitNode().getP().getPayload() + "," + e.getTimestamp();
	 * @throws IOException 
	 */
	public void writeHeader() throws IOException{
			try {
				writer = new FileWriter(csvLogName+logcounter+".csv");
			} catch (FileNotFoundException e1) {
				new File("./Logs").mkdirs();
				writer = new FileWriter(csvLogName+logcounter+".csv");
			} 
			String str = "Layer;sender;action;receiver;paket text;timestamp\n";
			appendData(str);
		
	}
	
	/**
	 * Method to append text to the log.csv file 
	 * in order to read information and analyse it out of the log file 
	 * the string format of str "Layer of communication" , "initnode ID" , "action" , "receiver ID" , "paket text"  , "Timestamp" ( evtl Latency )
	 * 
	 * @param str
	 */
	public String formattingLogOutput(Event e){
		String action = e.getClass().toString();
		String layer;
		
		if (e.isLayer7Flag()) layer = "L7";
		else layer = "L3";
		
		if (action.contains("RX")) action = "RX";
		else action = "TX";
		
		String str = layer + ";" + e.getInitNode().getId() + ";" + action + ";" + e.getreceiverNode().getId() + ";" + e.getInitNode().getP().getPayload() + ";" + e.getTimestamp().toString() + "\n";
		
		return str;
	}
	
	public void appendData(String str){
		   try {
			writer.append(str);
		   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void writeTotalTime(long time){
		try {
			writer.append("\n Totaltime: " + (time) + " ms");
			writer.flush();
			
			logcounter++;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	
}
