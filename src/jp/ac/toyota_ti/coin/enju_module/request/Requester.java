package jp.ac.toyota_ti.coin.enju_module.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import jp.ac.toyota_ti.coin.enju_module.boot.EnjuController;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.exception.ParseException;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class Requester {
	private static final Logger log = Logger.getLogger(Requester.class);
	private static HttpURLConnection connection;
	private static Serializer serializer;
	private static BufferedReader reader;
	private static int REPEAT_COUNT;
	private static UrlBuilder builder;
	
	/**
	 * Use default REPEAT_COUNT.<br>
	 * REPEAT_COUNT : 5
	 * @throws Exception 
	 */
	public Requester() throws Exception{
		this(5);
	}
	
	/**
	 * Set REPEAT_COUNT.
	 * @param repeat_count
	 * @throws Exception 
	 */
	public Requester(int repeat_count) throws Exception{
		REPEAT_COUNT = repeat_count;
		
		serializer = new Persister();
		builder = new UrlBuilder();
	}
	
	public synchronized Sentence request(String sentenceStr) throws IOException, Exception{
		builder.setSentence(sentenceStr);
		Sentence sentence = this.request(builder.buildURL());
		sentence.setRawSentence(sentenceStr);
		
		return sentence;
	}
	
	/**
	 * If you want to use own URL builder, you use this method.<br>
	 * This method don't set raw sentence in sentence instance.<br>
	 * Thus you should not use except when you don't use raw sentence data.
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public synchronized Sentence request(URL url) throws Exception{
		this.getConnection(url);
		
		if(reader == null)
			return null;
		
		String str;
		String xml ="";
		while(null != (str = reader.readLine())){
			xml = xml + str;
		}
		
		reader.close();
		connection.disconnect();
		
		Sentence sentence = serializer.read(Sentence.class, xml);
		
		if(!sentence.getParseStatus().equals("success"))
			throw new ParseException(sentence);
			
		return sentence;
	}
	
	protected synchronized void getConnection(URL url) throws Exception{
		for(int count = 0; count < REPEAT_COUNT; count++){
			try{
				while(!EnjuController.getController().getObserver().getState())
					wait(1000);
				connection = (HttpURLConnection)url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("GET");
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				break;
			}catch(ConnectException e){
				if(count == REPEAT_COUNT - 1){
					log.error("Give up to connect. : "+e);
					log.error(url.toString());
					reader = null;
				}
				else{
					log.warn("Connect Error : "+e);
					log.warn(url.toString());
					log.warn("It will try connecting after Enju rebooting.");
				}
			}
		}
	}
}
