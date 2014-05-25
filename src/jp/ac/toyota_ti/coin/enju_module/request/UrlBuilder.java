package jp.ac.toyota_ti.coin.enju_module.request;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import jp.ac.toyota_ti.coin.utils.PropertiesProvider;
/**
 * This class generate URL for request to ENJU cgi.<br>
 * You have to write<br>
 * enju.cgi.port<br>
 * in your properties file.<br>
 * Or you have to instantiate this class by another constructor.<br>
 * <br>
 * Default properties file is<br> 
 * ./conf/properties.conf<br>
 * If you want to read specific properties file,<br>
 * you need to reload properties file by PropertiesProvider class.
 * @author kota
 *
 */
public class UrlBuilder {
	private static String ENJU_PORT;
	private String sentence;
	
	public UrlBuilder() throws Exception{
		this((String) PropertiesProvider.getValue("enju.cgi.port"));
	}
	public UrlBuilder(String port){
		ENJU_PORT = port;
	}
	
	public URL buildURL() throws IOException{
		StringBuffer query = new StringBuffer();
		query.append("http://localhost:"+ENJU_PORT+"/cgi-lilfes/enju?sentence=");
		sentence = URLEncoder.encode(sentence, "UTF-8");
		query.append(sentence);
		query.append("&parse=Parse");
		
		return new URL(query.toString());
	}
	public void setSentence(String sentence){
		this.sentence = sentence;
	}
}
