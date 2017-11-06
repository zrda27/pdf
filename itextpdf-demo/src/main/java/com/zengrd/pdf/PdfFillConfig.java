package com.zengrd.pdf;

import java.util.HashMap;
import java.util.Map;


public class PdfFillConfig {
	
	Map<String, PdfFillProcessor> map = new HashMap();
	
	public void registerValueProcessor(Class clazz, PdfFillProcessor processor){
		map.put(clazz.getName(), processor);
	}
	
	public Object config(Object value) {
		if(value == null)return value;
		PdfFillProcessor processor = map.get(value.getClass().getName());
		if(processor == null)return value;
		return processor.process(value);
	}

}
