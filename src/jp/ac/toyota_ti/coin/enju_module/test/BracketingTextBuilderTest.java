package jp.ac.toyota_ti.coin.enju_module.test;

import jp.ac.toyota_ti.coin.enju_module.boot.EnjuController;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.request.Requester;
import jp.ac.toyota_ti.coin.enju_module.visit.BracketingTextBuilder;

public class BracketingTextBuilderTest {
	public static void main(String[] args) throws Exception{
//		Boot Enju.
		EnjuController controller = EnjuController.getController();
		controller.boot();
//		Will John marry Mary"
//		They walk, I run and you crawl. error
//		"The INSURE method, which consists of an intubation-surfactant-extubation " +
//		"sequence, is effective in reducing the need for mechanical ventilation (MV), " +
//		"the duration of respiratory support, and the need for surfactant replacement " +
//		"in preterm infants with respiratory distrees syndrome";
		try{
			String sentenceStr = "The INSURE method, which consists of an intubation-surfactant-extubation " +
					"sequence, is effective in reducing the need for mechanical ventilation (MV), " +
					"the duration of respiratory support, and the need for surfactant replacement " +
					"in preterm infants with respiratory distrees syndrome";
			
//		Send request and get sentence instance.
			Requester requester = new Requester();
			
			Sentence sentence = requester.request(sentenceStr);
			BracketingTextBuilder bracketingText = new BracketingTextBuilder();
			sentence.accept(bracketingText);
			System.out.println(bracketingText.get());
		}catch(Exception e){
			controller.shutdown();
			throw e;
		}


		
//	 	Shutdown Enju.
		controller.shutdown();
	}
}
