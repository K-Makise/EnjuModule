package jp.ac.toyota_ti.coin.enju_module.boot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import jp.ac.toyota_ti.coin.utils.PropertiesProvider;

public class EnjuController {
	private static final Logger log = Logger.getLogger(EnjuController.class);
	private static EnjuController own = null;
	private static EnjuObserver observer = EnjuObserver.getObserver();
	private static boolean shutdownFlag;
	private static Process process;
	private static String ENJU_PATH;
	private static String ENJU_PORT;
	private static String ENJU_OPTIONS;
	private static Long ENJU_WAIT_TIME;
	private static List<String> command;
	
	private EnjuController() throws Exception{
		this((String) PropertiesProvider.getValue("enju.path"),
				(String) PropertiesProvider.getValue("enju.cgi.port"),
				(String) PropertiesProvider.getValue("enju.other.options"),
				Long.valueOf((String) PropertiesProvider.getValue("enju.wait.time")));
	}
	
	private EnjuController(String path, String port, String options) throws Exception{
		this(path, port, options,
				Long.valueOf((String) PropertiesProvider.getValue("enju.wait.time")));
	}
	
	private EnjuController(String path, String port, String options, Long time){
		ENJU_PATH = path;
		ENJU_PORT = port;
		ENJU_OPTIONS = options;
		ENJU_WAIT_TIME = time;
		
		command = new ArrayList<>();
		command.add(ENJU_PATH);
		command.add("-cgi");
		command.add(ENJU_PORT);
		
		log.info("Initialize Enju Controller.");
		log.info("ENJU_PATH : "+ENJU_PATH);
		log.info("ENJU_PORT : "+ENJU_PORT);
		if(!options.equals("")){
			log.info("ENJU_OPTIONS : "+ENJU_OPTIONS);
			command.add(ENJU_OPTIONS);
		}	
	}
	
	protected void notifyState(boolean state) throws InterruptedException{
		observer.updateState(state);
	}
	
	public void notifyAlive(){
		observer.notifyAlive();
	}
	
	public synchronized void boot() throws IOException, InterruptedException{		
		shutdownFlag = true;
		Thread enjuThread = new Thread ( new Runnable() {
			public void run(){
				while(shutdownFlag){
					try {
						processKeeper();
					} catch (InterruptedException | IOException e) {
						log.error("Process Keeper Error. : "+e);
					}
				}
			}
		});
		
		Thread reaperThread = new Thread ( new Runnable() {
			public void run(){
				while(shutdownFlag){
					long before = observer.getLastAlivedTime();
					long now = System.currentTimeMillis();
					
					if(now - before > ENJU_WAIT_TIME){
						log.error("Enju return no response. The Enju will reboot.");
						process.destroy();
						try {
							EnjuController.linuxProcessKiller();
						} catch (IOException | InterruptedException e) {
							log.error("Linux Process Killing Error.");
						}
						observer.notifyAlive();
					}
				}
			}
		});
		
		enjuThread.start();
		reaperThread.start();
	}
	
	public synchronized void shutdown(){
		try{
			shutdownFlag = false;
			process.destroy();
		}catch(Exception e){
			log.error("Process is not destroyed.");
		}
		try {
			EnjuController.linuxProcessKiller();
		} catch (IOException | InterruptedException e) {
			log.warn("Linux Process Killing Error.");
		}
	}
	
	public void processKeeper() throws InterruptedException, IOException{
		shutdownFlag = true;
		while(shutdownFlag){
			new ProcessBuilder();
			ProcessBuilder pb = new ProcessBuilder(command);
			ProcessChecker checker = ProcessChecker.getChecker(ENJU_PATH);
			checker.chechInAdvance();
			
			try {
				process = pb.start();
			} catch (IOException e) {
				log.fatal("Enju boot process building is failed."+e);
			}
			log.info("Enju is booting.");
			this.notifyState(true);
			ProcessChecker.processCheck();
			int ret = 	process.waitFor();
			process.destroy();
			this.notifyState(false);
			
			if(ret == 0 || shutdownFlag == false){
				log.info("Finish Enju Process Keeping.");
				break;
			}else{
				log.error("Reboot Enju.");
			}
		}	
	}
	protected static void linuxProcessKiller() throws IOException, InterruptedException{
		if(!ProcessChecker.checkDestroyed()){
			String[] killCommand = {"kill", ProcessChecker.getPid()};
			ProcessBuilder pb = new ProcessBuilder(killCommand);
			pb.start();
		}
	}
	
	public EnjuObserver getObserver(){
		return observer;
	}
	
	/**
	 * This method set ENJU_PATH and ENJU_PORT.
	 * And this instance is renewed.
	 * Thus this must be called only once except when resetting parameters.
	 * @param path
	 * @param port
	 * @return
	 * @throws Exception 
	 */
	public static EnjuController getController(String path, String port, String options) throws Exception{
		own = new EnjuController(path, port, options);
		
		return own;
	}
	public static EnjuController getController() throws Exception{
		if(own == null)
			own = new EnjuController();
		return own;
	}
}
