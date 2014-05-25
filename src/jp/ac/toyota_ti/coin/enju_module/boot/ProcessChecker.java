package jp.ac.toyota_ti.coin.enju_module.boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import jp.ac.toyota_ti.coin.enju_module.exception.ProcessException;

public class ProcessChecker {
	private static ProcessChecker own = null;
	private static String ENJU_PATH = null;
	private static String enjuPid = null;
	private static Set<String> beforePid;
	
	private ProcessChecker(){
		
	}
	
	private Set<String> check() throws IOException{
		Set<String> pidSet = new HashSet<>();
		String enjuPath = ENJU_PATH.replaceFirst("enju$", "bin/enju");
		String[] pidCommand = {"pidof", enjuPath};
		ProcessBuilder pb = new ProcessBuilder(pidCommand);
		Process process = pb.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String str = br.readLine();
		if(str != null){
			String[] pidArray = str.split(" ");
			for(String pid : pidArray)
				pidSet.add(pid);
		}
		
		return pidSet;
	}
	
	public void chechInAdvance() throws IOException{
		beforePid = check();
	}
	
	public static boolean processCheck() throws IOException, ProcessException{
		Set<String> afterPid = own.check();
		for(String pid : afterPid)
			if(!beforePid.contains(pid))
				enjuPid = pid;
		
		if(enjuPid == null)
			throw new ProcessException();
		return true;
	}
	
	/**
	 * Check the enju process has been destroyed or not.
	 * If the process is destroyed, it return true.
	 * @return
	 * @throws IOException
	 */
	public static boolean checkDestroyed() throws IOException{
		Set<String> alivePid = own.check();
		
		return alivePid.contains(enjuPid) | enjuPid != null ? false : true;
	}
	
	public static String getPid(){
		return enjuPid;
	}
	
	public static ProcessChecker getChecker(String path){
		if(own == null)
			own = new ProcessChecker();
		
		ENJU_PATH = path;
		
		return own;
	}
	
	public static ProcessChecker getChecker(){
		if(own == null)
			own = new ProcessChecker();
		
		return own;
	}
}
