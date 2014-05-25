package jp.ac.toyota_ti.coin.enju_module.exception;

public class EntryTreatmentException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntryTreatmentException(){
	}
	
	public EntryTreatmentException(String msg){
		super(msg);
	}
}
