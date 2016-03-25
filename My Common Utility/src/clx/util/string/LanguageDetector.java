/**
 * 
 */
package clx.util.string;

import com.carrotsearch.labs.langid.DetectedLanguage;
import com.carrotsearch.labs.langid.LangIdV3;


/**
 * @author chulx
 *
 */
public class LanguageDetector {
	private static LanguageDetector instance;
	
	private static LangIdV3 langid;
	
	static {
		instance = new LanguageDetector();
	};
	
	private LanguageDetector () {
		langid = new LangIdV3();
	}
			
	public static LanguageDetector getInstance() {
        return instance;
    }

	public String detect (String s) {
		if (s != null && !s.isEmpty()) {
			 DetectedLanguage result = langid.classify(s, true);
			 return result.langCode;
		}
		
		return null;
	}
}
