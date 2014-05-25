package jp.ac.toyota_ti.coin.enju_module.test;

import jp.ac.toyota_ti.coin.enju_module.boot.EnjuController;
import jp.ac.toyota_ti.coin.enju_module.data.Constituent;
import jp.ac.toyota_ti.coin.enju_module.data.Sentence;
import jp.ac.toyota_ti.coin.enju_module.exception.ParseException;
import jp.ac.toyota_ti.coin.enju_module.request.Requester;

public class RequestTest {
	public static void main(String[] args) throws Exception{
//		Boot Enju.
		EnjuController controller = EnjuController.getController();
		controller.boot();
		
		String sentenceStr = "This is a pen.";		
//		The Oncotype-DX Breast Cancer Assay (Genomic Health, Redwood City, CA) quantifies gene expression for 21 genes in breast cancer tissue by performing reverse transcription polymerase chain reaction (RT-PCR) on formalin-fixed paraffin-embedded (FFPE) tumour blocks that are obtained during initial surgery (lumpectomy, mastectomy, or core biopsy) of women with early breast cancer that is newly diagnosed.";
//		Send request and get sentence instance.
		Requester requester = new Requester();
		try{
			for(int i = 0; i < 10; i++){
				Sentence sentence = requester.request(sentenceStr);
				if(sentence != null)
						System.out.println("Success.");
				for(Constituent cons : sentence.getConsList())
					System.out.println(cons.getText());
			}
		}catch(ParseException e){
			System.out.println(e.getStatus());
		}catch(Exception e){
			e.printStackTrace();
			controller.shutdown();
		}



		
//	 	Shutdown Enju.
		controller.shutdown();
	}
}
