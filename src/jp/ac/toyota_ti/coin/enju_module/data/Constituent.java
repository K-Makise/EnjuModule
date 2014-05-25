package jp.ac.toyota_ti.coin.enju_module.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ac.toyota_ti.coin.enju_module.visit.Visitor;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.core.Commit;

/**
 * This class is data container for Enju XML format.
 * 
 * If constituent has schema, it have cons in entry and not have tok.
 * In contrast, if it has tok in entry, it does not have cons and schema.
 * @author kota
 *
 */
@Root(name="cons")
public class Constituent extends Entry{
	private static final Logger log = Logger.getLogger(Constituent.class);
	private static final String ESCAPE_CHARS = "(\\|\\|)|[\\.\\+\\-\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\*\\?\\/]";
	private static final Pattern PATTERN = Pattern.compile(ESCAPE_CHARS);
	private static final String REPLACEMENT_STRING = "\\\\$0";
	
	@Attribute(name="xcat")
	private String xcat;
	@Attribute(name="head")
	private String head;
	@Attribute(name="sem_head")
	private String sem_head;
	
	@Attribute(name="schema", required=false)
	private String schema;
	
	private List<Entry> entryList = new ArrayList<>();
	@ElementList(name="cons", inline=true, required=false)
	private List<Constituent> consList;
	
	@Element(name="tok", required=false)
	private Token tok;
	
	private Entry headLink;
	private Entry semHeadLink;
	
	@Commit
	public void commit(){
		if(consList != null)
			for(Constituent cons : consList)
				entryList.add(cons);
		
		if(tok != null)
			entryList.add(tok);
		
		LinkFactory factory = LinkFactory.getInstance();
		factory.add(id, this);
	}
	
	@Override
	protected void setLink(){
		LinkFactory factory = LinkFactory.getInstance();
		headLink = factory.get(head);
		semHeadLink = factory.get(sem_head);
		
		for(Entry entry : entryList)
			entry.setLink();
	}
	public Entry getHeadLink(){
		return headLink;
	}
	public Entry getSemHeadLink(){
		return semHeadLink;
	}
	
	public String getXcat(){
		return this.xcat;
	}
	public String getHead(){
		return this.head;
	}
	public String getSemHead(){
		return this.sem_head;
	}
	public String getSchema(){
		return this.schema;
	}
	public List<Entry> getEntryList(){
		return this.entryList;
	}

	@Override
	public Integer getTokenCount(){
		Integer count = 0;
		for(Iterator<Entry> ite = entryList.iterator();ite.hasNext();)
			count += ite.next().getTokenCount();
		
		return count;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
	
	@Override
	protected void setRoot(Sentence sentence){
		rootSentence = sentence;
		for(Entry entry : entryList)
			entry.setRoot(sentence);
	}
	
	@Override
	public Integer getEntryListSize(){
		int size = 0;
		size++;
		for(Entry entry : entryList)
			size += entry.getEntryListSize();
		
		return size;
	}
	@Override
	protected void setPreJunction(Constituent junction){
		preJunction = junction;
		for(Entry entry : entryList)
			entry.setPreJunction(this);
	}
	
	/**
	 * This method reconstruct the text that analyzed by enju using under this constituent.<br>
	 * ATTENTION<br>
	 * It is written by rule base.<br>
	 * Thus it may include bags.
	 */
	@Override
	public String getText(){
		String str = "";
		
		if(this.getRoot().getRawSentence() != null){
			for(Entry entry : entryList)
				str = str + "--space--" + entry.getBranchText();

			str = PATTERN.matcher(str).replaceAll(REPLACEMENT_STRING);
			str = str.replaceAll("(\\\\-\\\\-space\\\\-\\\\-)+", "\\\\s*");
			Matcher matcher = Pattern.compile(str).matcher(this.getRoot().getRawSentence());
			if(matcher.find())
				str = matcher.group();
			else{
				log.warn("Match Error : "+str);
				str = this.ruleBaseGetText();
			}

		}
		else
			str = this.ruleBaseGetText();
		
		str = str.trim();
		return str;
	}
	
	protected String ruleBaseGetText(){
		String str = "";
		
		for(Entry entry : entryList)
			str = str + entry.getBranchText();
		
		str = str.replaceAll("(--space--)+", " ");
		str = str.replaceAll("\\s+(--nospace--)+\\s+", "");
		str = str.replaceAll("\\s+(--nospace--)+", "");
		str = str.replaceAll("(--nospace--)+\\s+", "");
		str = str.replaceAll("(--nospace--)+", "");
		
		return str;
	}
	
	
	@Override
	protected String getBranchText(){
		String str = "";
		if(this.getRoot().getRawSentence() != null){
			for(Entry entry : entryList)
				str = str + "--space--" + entry.getBranchText();
		}
		else
			str = this.ruleBaseGetBranchText();

		return str;
	}
	
	protected String ruleBaseGetBranchText(){
		String str = "";
		
		for(Entry entry : entryList){
			if(entry.getCat().equals("DX") | entry.getCat().equals("PN")){
				if(entry.getClass().equals(Token.class)){
					if(((Token) entry).getBase().equals("-COMMA-") ||
						((Token) entry).getBase().equals("-RRB-") ||
						((Token) entry).getBase().equals("-RCB-"))
						str = str + "--nospace--" + entry.getBranchText();
					else if(((Token) entry).getBase().equals("-LRB-") ||
						((Token) entry).getBase().equals("-LCB-"))
						str = str + "--space--" + entry.getBranchText() + "--nospace--";
					else
						str = str + "--space--" + entry.getBranchText();
				}else
					str = str + "--space--" + entry.getBranchText();
			}
			else
				str = str + "--space--" + entry.getBranchText();
		}
		return str;
	}
}
