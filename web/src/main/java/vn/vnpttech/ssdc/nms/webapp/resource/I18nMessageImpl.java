package vn.vnpttech.ssdc.nms.webapp.resource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class I18nMessageImpl implements I18nMessage{
	private String resourceBundlePath;
	private Map<String, ResourceBundle> mapBundle = new HashMap<String, ResourceBundle>();

	public String getResourceBundlePath() {
		return resourceBundlePath;
	}

	public void setResourceBundlePath(String resourceBundlePath) {
		this.resourceBundlePath = resourceBundlePath;
	}

	@Override
	public String getText(String key, Locale locale) {
		if (locale != null) {
			ResourceBundle bundle = mapBundle.get(locale.toString());
			if (bundle == null) {
				bundle = ResourceBundle.getBundle(resourceBundlePath, locale);
				mapBundle.put(locale.getCountry(), bundle);
			}
			
			return bundle.getString(key);
		}
		
		return null;
	}
	
}
