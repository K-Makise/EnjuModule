package jp.ac.toyota_ti.coin.enju_module.data;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jp.ac.toyota_ti.coin.enju_module.visit.Visitor;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.core.Commit;

@Root(name="tok")
public class Token extends Entry{
	private static final Pattern predPattern = Pattern.compile("([1-4]{1,4})$");
	private static final Pattern idPattern = Pattern.compile("^t(\\d+)");
	
	@Attribute(name="pos")
	private String pos;
	@Attribute(name="base")
	private String base;
	@Attribute(name="lexentry")
	private String lexentry;
	@Attribute(name="pred")
	private String pred;
	
	@Attribute(name="type", required=false)
	private String type;

	private Map<Integer, String> argMap;

	@Attribute(name="arg1", required=false)
	private String arg1;
	@Attribute(name="arg2", required=false)
	private String arg2;
	@Attribute(name="arg3", required=false)
	private String arg3;
	@Attribute(name="arg4", required=false)
	private String arg4;

	
	@Attribute(name="tense", required=false)
	private String tense;
	@Attribute(name="aspect", required=false)
	private String aspect;
	@Attribute(name="voice", required=false)
	private String voice;
	@Attribute(name="aux", required=false)
	private String aux;
	@Attribute(name="mod", required=false)
	private String mod;
	
	@Text
	private String rawToken;
	private Token beforeToken;
	
	private Entry arg1Link;
	private Entry arg2Link;
	private Entry arg3Link;
	private Entry arg4Link;
	
	
	@Commit
	public void commit(){
		if(this.pred != null){
			this.argMap = new HashMap<>();
			Matcher m = predPattern.matcher(this.pred);
			if(m.find()){
				char[] charArray = m.group(1).toCharArray();
				
				for(char c : charArray){
					int i = Integer.parseInt(""+c);
					switch(i){
						case 1: this.argMap.put(1, this.arg1);
						case 2: this.argMap.put(2, this.arg2);
						case 3: this.argMap.put(3, this.arg3);
						case 4: this.argMap.put(4, this.arg4);
					}
				}
			}
		}
		
		LinkFactory factory = LinkFactory.getInstance();
		factory.add(id, this);
		
		Matcher m = idPattern.matcher(id);
		Integer idNum;
		if(m.find())
			idNum = Integer.valueOf(m.group(1));
		else
			throw new IllegalStateException("No match found.");
		

		String beforeId = "t"+(idNum-1);
		if(factory.contains(beforeId))
			this.beforeToken = (Token) factory.get(beforeId);
	}
	
	@Override 
	protected void setLink(){
		LinkFactory factory = LinkFactory.getInstance();
		if(this.pred != null){
			Matcher m = predPattern.matcher(this.pred);
			if(m.find()){
				char[] charArray = m.group(1).toCharArray();
				
				for(char c : charArray){
					int i = Integer.parseInt(""+c);
					switch(i){
						case 1: this.arg1Link = factory.get(this.arg1);
						case 2: this.arg2Link = factory.get(this.arg2);
						case 3: this.arg3Link = factory.get(this.arg3);
						case 4: this.arg4Link = factory.get(this.arg4);
					}
				}
			}
		}
	}
	
	public Entry getArg1Link(){
		return this.arg1Link;
	}
	public Entry getArg2Link(){
		return this.arg2Link;
	}
	public Entry getArg3Link(){
		return this.arg3Link;
	}
	public Entry getArg4Link(){
		return this.arg4Link;
	}
	
	public String getPos(){
		return this.pos;
	}
	public String getBase(){
		return this.base;
	}
	public String getLexentry(){
		return this.lexentry;
	}
	public String getPred(){
		return this.pred;
	}
	public String getType(){
		return this.type;
	}
	public Map<Integer, String> getArgMap(){
		return this.argMap;
	}
	public String getTense(){
		return this.tense;
	}
	public String getAspect(){
		return this.aspect;
	}
	public String getVoice(){
		return this.voice;
	}
	public String getAux(){
		return this.aux;
	}
	public String getMod(){
		return this.mod;
	}
	public Token getBeforeToken(){
		return this.beforeToken;
	}
	
	@Override
	protected void setRoot(Sentence sentence){
		rootSentence = sentence;
	}
	@Override
	public String getText(){
		return this.rawToken;
	}
	@Override
	protected String getBranchText(){
		return this.rawToken;
	}
	@Override
	protected void setPreJunction(Constituent junction){
		preJunction = junction;
	}
	@Override
	public Integer getTokenCount(){
		return 1;
	}
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
