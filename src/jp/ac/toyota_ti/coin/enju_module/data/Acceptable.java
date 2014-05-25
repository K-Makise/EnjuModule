package jp.ac.toyota_ti.coin.enju_module.data;

import jp.ac.toyota_ti.coin.enju_module.visit.Visitor;

/**
 * If this class is implemented by a class, it is acceptable visitor.
 * @author kota
 *
 */
public interface Acceptable {
	public void accept(Visitor v);
}
