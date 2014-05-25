package jp.ac.toyota_ti.coin.enju_module.visit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.ac.toyota_ti.coin.enju_module.data.Constituent;
import jp.ac.toyota_ti.coin.enju_module.data.Entry;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.data.Token;

public class BaseExtractor implements Visitor{
	private List<Token> tokenList;
	private String base;
	
	public BaseExtractor(String base){
		this.tokenList = new ArrayList<>();
		this.base = base;
	}
	@Override
	public void visit(Sentence sentence) {
		if(sentence.getConsList() == null)
			return;
		
		for(Iterator<Constituent> it = sentence.getConsList().iterator(); it.hasNext();){
			Constituent cons = (Constituent) it.next();
			cons.accept(this);
		}
	}

	@Override
	public void visit(Constituent cons) {
		for(Iterator<Entry> it = cons.getEntryList().iterator(); it.hasNext();){
			Entry entry = (Entry) it.next();
			entry.accept(this);
		}
	}

	@Override
	public void visit(Token tok) {
		if(tok.getBase().equals(base))
			tokenList.add(tok);
	}
	
	public List<Token> get(){
		return this.tokenList;
	}
}
