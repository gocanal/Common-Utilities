/**
 * 
 */
package clx.util.test;

import clx.util.string.CharsetDetector;
import clx.util.web.DetectWebLanguage;

/**
 * @author chulx
 *
 */
public class TestDetector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		System.out.println (CharsetDetector.getInstance().detect("hello world"));
		System.out.println (CharsetDetector.getInstance().detect("你好"));
		System.out.println (CharsetDetector.getInstance().detect("Яндекс"));
		// this is detected as KO 0.86 and zh-tw 0.14
		System.out.println (CharsetDetector.getInstance().detect ("專為消費者及工商機構而設的一站式綜合指南網站。網上黃頁被譽為本地頂尖的商業指南，包括飲食娛樂、購物旅遊, 食品糧油、餐飲業設備, 家居用品及服務、寵物, 醫療美容、健康護理, 婚禮服務, 教育、藝術、康體運動, 建造、裝修、環保工程, 銀行金融、地產保險, 商業及專業服務, 社團、宗教、公共事業, 禮品、花卉、珠寶、玩具, 服裝及配飾、紡織品, 印刷、辦公室用品, 電腦及資訊科技, 電子零件及設備, 運輸、物流, 五金、機械、儀器, 塑膠、石油、化工。"));
		*/
		System.out.println (DetectWebLanguage.INSTANCE.detectLanguageWithLangDetectorSite("hello world"));
		System.out.println (DetectWebLanguage.INSTANCE.detectLanguageWithLangDetectorSite("你好"));
		// detected as Cyrillic Script
		System.out.println (DetectWebLanguage.INSTANCE.detectLanguageWithLangDetectorSite("Яндекс"));
		System.out.println (DetectWebLanguage.INSTANCE.detectLanguageWithLangDetectorSite("專為消費者及工商機構而設的一站式綜合指南網站。網上黃頁被譽為本地頂尖的商業指南，包括飲食娛樂、購物旅遊, 食品糧油、餐飲業設備, 家居用品及服務、寵物, 醫療美容、健康護理, 婚禮服務, 教育、藝術、康體運動, 建造、裝修、環保工程, 銀行金融、地產保險, 商業及專業服務, 社團、宗教、公共事業, 禮品、花卉、珠寶、玩具, 服裝及配飾、紡織品, 印刷、辦公室用品, 電腦及資訊科技, 電子零件及設備, 運輸、物流, 五金、機械、儀器, 塑膠、石油、化工。"));
	
	}

}
