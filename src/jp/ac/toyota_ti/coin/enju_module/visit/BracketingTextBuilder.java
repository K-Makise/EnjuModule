package jp.ac.toyota_ti.coin.enju_module.visit;

import java.util.Iterator;
import jp.ac.toyota_ti.coin.enju_module.data.Constituent;
import jp.ac.toyota_ti.coin.enju_module.data.Entry;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.data.Token;

public class BracketingTextBuilder implements Visitor{
	private StringBuffer output;
	private int depth;
	private int preDepth;
	
	public BracketingTextBuilder(){
		this.output = new StringBuffer();
		this.depth = 0;
		this.preDepth = 0;
	}
	@Override
	public void visit(Sentence sentence){
		if(sentence.getConsList() != null){
			for(Iterator<Constituent> it = sentence.getConsList().iterator(); it.hasNext();){
				Constituent cons = (Constituent) it.next();
				cons.accept(this);
				depth = 0;
				preDepth = 0;
			}
			
			System.out.println();
			System.out.println();
		}
	}
	
	@Override
	public void visit(Constituent cons) {
		if(preDepth == depth && depth != 0 && cons.getCat().equals("NX")){
			System.out.println();
			for(int current = 0; current < depth; current++)
				System.out.print("\t");
		}else if(cons.getCat().equals("NX") && cons.getEntryListSize() >= 3){
			for(int current = 0; current < depth; current++)
				System.out.print("\t");
		}else if(preDepth == depth ){
			System.out.println();
			for(int current = 0; current < depth; current++)
				System.out.print("\t");
		}else if(!cons.getCat().equals("NX")){
			for(int current = 0; current < depth; current++)
				System.out.print("\t");
		}else{
			System.out.print(" ");
		}

		System.out.print("("+cons.getCat());
		if(!cons.getXcat().equals(""))
			System.out.print("-"+cons.getXcat());
		
		System.out.print("[id="+cons.getId()+"]");
		
		if(cons.getEntryListSize() > 3)
			System.out.println();

		for(Iterator<Entry> it = cons.getEntryList().iterator(); it.hasNext();){
			depth++;
			Entry entry = (Entry) it.next();
			entry.accept(this);
			preDepth = depth;
			depth--;
		}
		System.out.print(")");
	}

	@Override
	public void visit(Token tok) {
		System.out.print(" "+tok.getText());
		
		if(tok.getPred() != null){
			System.out.print("[pred="+tok.getPred());
			
			
			for(Integer key : tok.getArgMap().keySet()){
				if(tok.getArgMap().get(key) != null)
					System.out.print(" arg"+key+"="+tok.getArgMap().get(key));
			}
			System.out.print("]");
		}
	}
	
	public String get(){
		return output.toString();
	}
	
}
