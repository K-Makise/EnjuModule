package jp.ac.toyota_ti.coin.enju_module.visit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.ac.toyota_ti.coin.enju_module.data.Constituent;
import jp.ac.toyota_ti.coin.enju_module.data.Entry;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.data.Token;

/**
 * This class extract constituent which have CAT you select.<br>
 * If you select plural cat, extracted constituent is not overlap.<br>
 * If this class find overlapping constituent, this choose lower constituent as result.
 * @author kota
 *
 */
public class CatExtractor implements Visitor{
	private List<Entry> npMap;
	private Set<String> catSet;
	
	public CatExtractor(){
		this.npMap = new ArrayList<>();
		this.catSet = new HashSet<>();
	}
	public CatExtractor(String cat){
		this();
		this.addCat(cat);
	}
	
	public CatExtractor(String[] cats){
		this();
		for(String cat : cats)
			this.addCat(cat);
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
		if(catSet.contains(cons.getCat()))
			if(!(cons.getEntryList().size() == 1 && catSet.contains(cons.getEntryList().get(0).getCat())))
				npMap.add(cons);
		
		for(Iterator<Entry> it = cons.getEntryList().iterator(); it.hasNext();){
			Entry entry = (Entry) it.next();
			entry.accept(this);
		}
	}

	@Override
	public void visit(Token tok) {
		if(catSet.contains(tok.getCat()))
			npMap.add(tok);
	}
	
	public List<Entry> get(){
		return this.npMap;
	}
	
	/**
	 * Add cat list which you want to extract.
	 * @param cat
	 */
	public void addCat(String cat){
		this.catSet.add(cat);
	}
}
