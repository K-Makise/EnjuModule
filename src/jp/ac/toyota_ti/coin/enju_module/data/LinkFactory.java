package jp.ac.toyota_ti.coin.enju_module.data;

import java.util.HashMap;
import java.util.Map;
/**
 * This manage Links of Enju args.
 * This class is designed based on Singleton and Flyweight pattern.
 * This class should be destroyed after create Sentence instance.
 * @author kota
 *
 */
public class LinkFactory {
	private static LinkFactory Factory;
	private static Map<String, Entry> Entry;
	
	private LinkFactory(){
		Factory = this;
		Entry = new HashMap<>();
	}
	
	public void add(String id, Entry entry){
		Entry.put(id, entry);
	}
	
	public boolean contains(String id){
		return Entry.containsKey(id);
	}
	public Entry get(String id){
		return Entry.get(id);
	}
	
	public static void clear(){
		Entry = new HashMap<>();
	}
	
	public static LinkFactory getInstance(){
		return Factory == null ? new LinkFactory() : Factory;
	}
}
