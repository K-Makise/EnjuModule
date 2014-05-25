package jp.ac.toyota_ti.coin.enju_module.visit;

import jp.ac.toyota_ti.coin.enju_module.data.Token;
/**
 * This class extract Token which appear first in the sentence.
 * @author kota
 *
 */
public class FirstTokenExtractor extends FirstExtractor{

	@Override
	public void visit(Token tok) {
		this.firstToken = tok;
		this.findFlag = true;
	}
}
