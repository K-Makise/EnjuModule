package jp.ac.toyota_ti.coin.enju_module.data;

import java.util.Iterator;
import java.util.List;
import jp.ac.toyota_ti.coin.enju_module.visit.Visitor;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.core.Commit;

@Root(name="sentence")
public class Sentence implements Acceptable, TokenCountable{
	@Attribute(name="id")
	private String id;
	@Attribute(name="parse_status")
	private String parse_status;
//	Fom is a figure-of-merit.
	@Attribute(name="fom", required=false)
	private Double fom;
	@ElementList(name="cons", inline=true, required=false)
	private List<Constituent> consList;
	
	private String rawSentence;
	
	public String getId(){
		return id;
	}
	public String getParseStatus(){
		return parse_status;
	}
	public Double getFom(){
		return fom;
	}
	public List<Constituent> getConsList(){
		return consList;
	}
	
	@Commit
	public void Commit(){
		if(consList != null)
			for(Constituent cons : consList){
				cons.setRoot(this);
				cons.setPreJunction(null);
				cons.setLink();
			}
		LinkFactory.clear();
	}
	
	/**
	 * Set raw sentence.<br>
	 * @param sentenceStr
	 */
	public void setRawSentence(String sentenceStr){
		this.rawSentence = sentenceStr;
	}
	
	public String getRawSentence(){
		return this.rawSentence;
	}
	
	@Override
	public Integer getTokenCount(){
		Integer count = 0;
		for(Iterator<Constituent> ite = consList.iterator();ite.hasNext();)
			count += ite.next().getTokenCount();
		
		return count;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
