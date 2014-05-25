package jp.ac.toyota_ti.coin.enju_module.analysis;

import jp.ac.toyota_ti.coin.enju_module.data.Constituent;
import jp.ac.toyota_ti.coin.enju_module.data.Entry;
import jp.ac.toyota_ti.coin.enju_module.data.Token;

/**
 * This class extract destination of semhead.
 * @author kota
 *
 */
public class SemHeadAnalyzer {
	/**
	 * Return destination token of semhead. 
	 * @param entry
	 * @return
	 */
	public static Token getEndOfSemHead(Entry entry){
		Token retToken = null;
		if(entry.getClass().equals(Constituent.class)){
			Constituent cons = (Constituent) entry;
			retToken = getEndOfSemHead(cons.getSemHeadLink());
		}else{
			retToken = (Token) entry;
		}
		return retToken;
	}
	
	/**
	 * This method trace 2 upper junction first.
	 * And return destination token of the junction's semhead. 
	 * @param tok
	 * @return
	 */
	public static Token getEndOfSemHead(Token tok){
		Constituent startCons = tok.getPreJunction().getPreJunction();
		return getEndOfSemHead(startCons);
	}
}
