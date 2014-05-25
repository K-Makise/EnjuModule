package jp.ac.toyota_ti.coin.enju_module.visit;

import jp.ac.toyota_ti.coin.enju_module.data.Token;

/**
 * This class extract Token which have pos tag of "WDT".
 * @author kota
 *
 */
public class FirstWDTExtractor extends FirstExtractor{

	@Override
	public void visit(Token tok) {
		if(tok.getPos().equals("WDT")){
			firstToken = tok;
			findFlag = true;
		}
	}
}
