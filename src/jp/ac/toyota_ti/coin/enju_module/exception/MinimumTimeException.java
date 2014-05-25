package jp.ac.toyota_ti.coin.enju_module.exception;

public class MinimumTimeException extends ProcessException{
	private static final String mainMessage =
			"The Enju process keeping time is under MINIMUM_TIME.";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MinimumTimeException(){
		super(mainMessage);
	}
	
	public MinimumTimeException(long time){
		super(mainMessage+"\tProcess Time : "+time);
	}
	
	public MinimumTimeException(String message){
		super(message+"\t"+mainMessage);
	}
	
	public MinimumTimeException(String message, Throwable cause){
		super(message+"\t"+mainMessage, cause);
	}
	
	public MinimumTimeException(Throwable cause){
		super(cause);
	}
}
