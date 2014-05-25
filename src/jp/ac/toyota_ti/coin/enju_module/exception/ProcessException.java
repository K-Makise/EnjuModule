package jp.ac.toyota_ti.coin.enju_module.exception;

import jp.ac.toyota_ti.coin.enju_module.boot.EnjuController;

public class ProcessException extends RuntimeException{
	private static EnjuController controller;
	private static final String mainMessage =
			"This system's enju process may already exist. Check your PC's process.";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessException(){
		super(mainMessage);
		shutdownSystem();
	}
	
	public ProcessException(String message){
		super(message+"\n"+mainMessage);
		shutdownSystem();
	}
	
	public ProcessException(String message, Throwable cause){
		super(message+"\n"+mainMessage, cause);
		shutdownSystem();
	}
	
	public ProcessException(Throwable cause){
		super(cause);
		shutdownSystem();
	}
	
	private void shutdownSystem(){
		try {
			controller = EnjuController.getController();
		} catch (Exception e) {
			System.err.println("Unknown Error.");
			e.printStackTrace();
		}
		controller.shutdown();
	}
}
