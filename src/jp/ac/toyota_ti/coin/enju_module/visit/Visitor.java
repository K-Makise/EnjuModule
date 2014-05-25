package jp.ac.toyota_ti.coin.enju_module.visit;

import jp.ac.toyota_ti.coin.enju_module.data.Constituent;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.data.Token;

/**
 * This class is designed based on Visitor pattern.<br>
 * It must be inherited by visitor for data structure of Enju.
 * @author kota
 *
 */
public interface Visitor {
	public void visit(Sentence sentence);
	
	public void visit(Constituent cons);
	
	public void visit(Token tok);
}
