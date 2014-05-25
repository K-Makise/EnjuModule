package jp.ac.toyota_ti.coin.enju_module.data;

import org.simpleframework.xml.Attribute;

public abstract class Entry implements Acceptable, TokenCountable{
	@Attribute(name="id")
	protected String id;
	@Attribute(name="cat")
	protected String cat;
	
	protected Sentence rootSentence;
	
	protected Constituent preJunction;
	
	public String getId(){
		return id;
	}
	public String getCat(){
		return cat;
	}
	
	public abstract String getText();
	
	protected abstract String getBranchText();
	/**
	 * Return sum of EntryList size under the Constituent.
	 * If this method in Token class is called, return 1; 
	 * @return
	 */
	public Integer getEntryListSize(){
		return 1;
	}
	
	protected abstract void setRoot(Sentence sentence);
	
	public Sentence getRoot(){
		return this.rootSentence;
	}
	
	protected abstract void setLink();
	
	protected abstract void setPreJunction(Constituent junction);
	
	public Constituent getPreJunction(){
		return this.preJunction;
	}
}
