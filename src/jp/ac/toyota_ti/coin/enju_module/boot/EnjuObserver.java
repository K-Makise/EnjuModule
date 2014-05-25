package jp.ac.toyota_ti.coin.enju_module.boot;

import jp.ac.toyota_ti.coin.enju_module.exception.MinimumTimeException;
import org.apache.log4j.Logger;

/**
 * This class is an observer of Enju state.
 * When switch the state to true, this wait 8000 msec.
 * Because Enju starting completely take about 8000 msec.
 * 
 * If Enju process keeping real time is under MINIMUM_PROCESS_TIME, it throw MinmumTimeException().
 * Default value is 5.
 * @author kota
 *
 */
public class EnjuObserver {
	private static final Logger log = Logger.getLogger(EnjuObserver.class);
	private static EnjuObserver own = null;
	private static long MINIMUM_PROCESS_TIME = 5;
	private static long enjuStartTime;
	private static long enjuStopTime;
	private static Long lastAlivedTime;
	private static boolean state = false;
	
	private EnjuObserver(){}
	
	public synchronized void updateState(boolean update) throws InterruptedException{
		if(update){
			wait(8000);
			enjuStartTime = System.currentTimeMillis();
			log.info("Enju is running.");
		}
		else{
			enjuStopTime = System.currentTimeMillis();
			log.info("Enju is down.");
			
			if(enjuStopTime - enjuStartTime < MINIMUM_PROCESS_TIME)
				throw new MinimumTimeException(enjuStopTime - enjuStartTime);
		}
		
		state = update;
	}
	
	public void setMinimumTime(long time){
		MINIMUM_PROCESS_TIME = time;
	}
	public synchronized boolean getState(){
		return state;
	}
	
	public synchronized void notifyAlive(){
		lastAlivedTime = System.currentTimeMillis();
	}
	
	public synchronized Long getLastAlivedTime(){
		if(lastAlivedTime == null)
			lastAlivedTime = System.currentTimeMillis();
		
		return lastAlivedTime;
	}
	
	public static synchronized EnjuObserver getObserver(){
		if(own == null)
			own = new EnjuObserver();
		
		return own;
	}
}
