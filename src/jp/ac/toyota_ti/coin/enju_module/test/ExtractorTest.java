package jp.ac.toyota_ti.coin.enju_module.test;

import java.net.URL;
import java.util.List;

import jp.ac.toyota_ti.coin.enju_module.boot.EnjuController;
import jp.ac.toyota_ti.coin.enju_module.data.Entry;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.data.Token;
import jp.ac.toyota_ti.coin.enju_module.request.Requester;
import jp.ac.toyota_ti.coin.enju_module.request.UrlBuilder;
import jp.ac.toyota_ti.coin.enju_module.visit.BaseExtractor;
import jp.ac.toyota_ti.coin.enju_module.visit.CatExtractor;
import jp.ac.toyota_ti.coin.enju_module.visit.FirstTokenExtractor;

public class ExtractorTest {
	public static void main(String[] args) throws Exception{
//		Boot Enju.
		EnjuController controller = EnjuController.getController();
		controller.boot();
		
		String sentenceStr = "Reduced by-product formation and modified oxygen availability improve itaconic acid production in Aspergillus niger";
		
		try{
//		Build URL for request to Enju cgi.
			UrlBuilder builder = new UrlBuilder();
			builder.setSentence(sentenceStr);
			URL url = builder.buildURL();
		
//		Send request and get sentence instance.
			Requester requester = new Requester();
			
			Sentence sentence = requester.request(url);

			String[] cats = {"NP","NX","N"};
			CatExtractor extractor = new CatExtractor(cats);
			sentence.accept(extractor);
			List<Entry> test = extractor.get();
			
			for(Entry entry : test){
				System.out.println(entry.getText());
			}
			
			FirstTokenExtractor tokenExtractor = new FirstTokenExtractor();
			sentence.accept(tokenExtractor);
			Token firstToken= tokenExtractor.get();
			System.out.println("First Token : "+firstToken.getText());
			
			BaseExtractor baseExtractor = new BaseExtractor("formation");
			sentence.accept(baseExtractor);
			System.out.println("Base Token : "+baseExtractor.get().size());
		}catch(Exception e){
			controller.shutdown();
			throw e;
		}



		
//	 	Shutdown Enju.
		controller.shutdown();
	}
}
