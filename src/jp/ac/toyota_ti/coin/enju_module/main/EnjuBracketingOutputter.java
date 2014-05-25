package jp.ac.toyota_ti.coin.enju_module.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import org.apache.log4j.Logger;
import jp.ac.toyota_ti.coin.enju_module.arguments.EnjuBracketingOutputterArguments;
import jp.ac.toyota_ti.coin.enju_module.boot.EnjuController;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.request.Requester;
import jp.ac.toyota_ti.coin.enju_module.test.EnjuBootTest;
import jp.ac.toyota_ti.coin.enju_module.visit.BracketingTextBuilder;
import jp.ac.toyota_ti.coin.utils.ArgumentHandler;
import jp.ac.toyota_ti.coin.utils.LoggerConfigurator;
import jp.ac.toyota_ti.coin.utils.PropertiesProvider;

/**
 * Halfway 
 * @author kota
 *
 */
public class EnjuBracketingOutputter {
	private static final Logger log = Logger.getLogger(EnjuBootTest.class);
	private static ArgumentHandler<EnjuBracketingOutputterArguments> Handler = new ArgumentHandler<>();
	private static EnjuBracketingOutputterArguments Arguments = new EnjuBracketingOutputterArguments();
	private static EnjuController controller;
	private static String ENJU_PATH;
	private static String ENJU_PORT;
	private static String ENJU_OPTIONS;
	private static File INPUT_FILE;
	
	public static void main(String[] args) throws Exception{
		Handler.setArgumentsContainer(args, Arguments);
		LoggerConfigurator.configure();
		
		INPUT_FILE = (File) Arguments.getInputFile();
		log.info("Input File : "+INPUT_FILE);
		
		if(Arguments.getEnjuPath() == null)
			ENJU_PATH = (String) PropertiesProvider.getValue("enju.path");
		else
			ENJU_PATH = Arguments.getEnjuPath();

		if(Arguments.getPortNumber() == null)
			ENJU_PORT = (String) PropertiesProvider.getValue("enju.cgi.port");
		else
			ENJU_PORT = String.valueOf(Arguments.getPortNumber());
		
		if(Arguments.getOptions() == null)
			ENJU_OPTIONS = (String) PropertiesProvider.getValue("enju.other.options");
		else
			ENJU_OPTIONS = String.valueOf(Arguments.getOptions());
		
		controller = EnjuController.getController(ENJU_PATH, ENJU_PORT, ENJU_OPTIONS);
		controller.boot();
		try{
			Requester requester = new Requester();
			Reader in = new FileReader(INPUT_FILE);
			BufferedReader reader = new BufferedReader(in);
		    String line;
		    while((line = reader.readLine()) != null){
		    	System.out.println(line);
				Sentence sentence = requester.request(line);
				BracketingTextBuilder bracketingText = new BracketingTextBuilder();
				sentence.accept(bracketingText);
				System.out.println(bracketingText.get());
		    }
	
			reader.close();
			in.close();
		}catch(Exception e){
			e.printStackTrace();
			controller.shutdown();
		}
		
		controller.shutdown();
	}
}
