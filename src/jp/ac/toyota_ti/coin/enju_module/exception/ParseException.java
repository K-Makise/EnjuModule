package jp.ac.toyota_ti.coin.enju_module.exception;

import jp.ac.toyota_ti.coin.enju_module.data.Sentence;

public class ParseException extends RuntimeException{
	private Sentence sentence;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParseException(){
	}
	
	public ParseException(Sentence sentence){
		this.sentence = sentence;
	}
	
	public ParseException(String msg){
		super(msg);
	}
	
	public String getStatus(){
		return this.sentence.getParseStatus();
	}
	
	public Sentence getSentence(){
		return this.sentence;
	}
}
