package jp.ac.toyota_ti.coin.enju_module.test;

import jp.ac.toyota_ti.coin.enju_module.arguments.EnjuProcessKeeperArguments;
import jp.ac.toyota_ti.coin.enju_module.boot.EnjuController;
import jp.ac.toyota_ti.coin.utils.ArgumentHandler;
import jp.ac.toyota_ti.coin.utils.LoggerConfigurator;
import jp.ac.toyota_ti.coin.utils.PropertiesProvider;
import org.apache.log4j.Logger;


public class EnjuBootTest {
	private static final Logger log = Logger.getLogger(EnjuBootTest.class);
	private static ArgumentHandler<EnjuProcessKeeperArguments> Handler = new ArgumentHandler<>();
	private static EnjuProcessKeeperArguments Arguments = new EnjuProcessKeeperArguments();
	private static String ENJU_PATH;
	private static String ENJU_PORT;
	private static String ENJU_OPTIONS;
	
	public static void main(String[] args) throws Exception{
		Handler.setArgumentsContainer(args, Arguments);
		LoggerConfigurator.configure();
		
		log.info("Enju Process Keeper is starting.");
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
		
		EnjuController controller = EnjuController.getController(ENJU_PATH, ENJU_PORT, ENJU_OPTIONS);
		controller.boot();
//		controller.shutdown();
	}
}
