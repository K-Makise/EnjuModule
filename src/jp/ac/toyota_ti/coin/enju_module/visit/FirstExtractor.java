package jp.ac.toyota_ti.coin.enju_module.visit;

import java.util.Iterator;
import jp.ac.toyota_ti.coin.enju_module.data.Constituent;
import jp.ac.toyota_ti.coin.enju_module.data.Entry;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.data.Token;

/**
 * This abstract class is to extract Token instance which appear first.
 * @author kota
 *
 */

public abstract class FirstExtractor implements Visitor{
	protected Token firstToken;
	protected boolean findFlag;
	
	public FirstExtractor(){
		findFlag = false;
	}
	
	@Override
	public void visit(Sentence sentence) {
		for(Iterator<Constituent> it = sentence.getConsList().iterator(); it.hasNext() && !findFlag;){
		Constituent cons = (Constituent) it.next();
		cons.accept(this);
		}
	}

	@Override
	public void visit(Constituent cons) {
		for(Iterator<Entry> it = cons.getEntryList().iterator(); it.hasNext() && !findFlag;){
			Entry entry = (Entry) it.next();
			entry.accept(this);
		}
	}

	public Token get(){
		return firstToken;
	}
	
	public boolean findFlag(){
		return findFlag;
	}
}
