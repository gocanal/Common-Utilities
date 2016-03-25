/**
 * 
 */
package clx.util.string;

import java.util.List;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;

/**
 * @author chulx
 *
 * 
 */
public class CharsetDetector {
	private static CharsetDetector instance;
	
	static {
		try {
			instance = new CharsetDetector();
		} catch (LangDetectException e) {
			// TODO - need to handle anything ?
			e.printStackTrace();
		}		
	};
	
	private CharsetDetector () throws LangDetectException {
		DetectorFactory.clear();
		DetectorFactory.loadProfile("./profiles");		
	}
			
	public static CharsetDetector getInstance() {
        return instance;
    }
	
	/**
	 * supported languages:
	 * 
	 * 	af	Afrikaans
		ar	Arabic
		bg	Bulgarian
		bn	Bengali
		cs	Czech
		da	Danish
		de	German
		el	Greek
		en	English
		es	Spanish
		et	Estonian
		fa	Persian
		fi	Finnish
		fr	French
		gu	Gujarati
		he	Hebrew
		hi	Hindi
		hr	Croatian
		hu	Hungarian
		id	Indonesian
		it	Italian
		ja	Japanese
		kn	Kannada
		ko	Korean
		lt	Lithuanian
		lv	Latvian
		mk	Macedonian
		ml	Malayalam
		mr	Marathi
		ne	Nepali
		nl	Dutch
		no	Norwegian
		pa	Punjabi
		pl	Polish
		pt	Portuguese
		ro	Romanian
		ru	Russian
		sk	Slovak
		sl	Slovene
		so	Somali
		sq	Albanian
		sv	Swedish
		sw	Swahili
		ta	Tamil
		te	Telugu
		th	Thai
		tl	Tagalog
		tr	Turkish
		uk	Ukrainian
		ur	Urdu
		vi	Vietnamese
		zh-cn	Simplified Chinese
		zh-tw	Traditional Chinese

	 * @param s
	 * @return "en" (we can return <"en", 0.9999987517035696> also)
	 */
	public String detect (String s) {
		if (s != null && !s.isEmpty()) {
			try {
				Detector detector = DetectorFactory.create();
				detector.append(s);
				//
				List<Language> languages = detector.getProbabilities();
				for (Language lang : languages)
					System.out.println (lang.lang + " : " + lang.prob);
				//
				return detector.detect();
			} catch (LangDetectException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
