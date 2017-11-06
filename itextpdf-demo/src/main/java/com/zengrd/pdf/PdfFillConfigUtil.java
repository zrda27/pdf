package com.zengrd.pdf;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfFillConfigUtil {
	/**
	 * 0
	 */
	public static final int SqlDateProcessor = 0;
	
	/**
	 * 根据参数formats指定json字段类型转换的规则
	 * 同一种java数据类型的字段重复注册不同的处理器，同字段数据类型后设置的处理器会覆盖前设置的处理器
	 * @param formats
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static PdfFillConfig getPdfFillConfig(int[] formats){
		PdfFillConfig config = new PdfFillConfig();
		if(formats != null){
			for(int format : formats){
				PdfFillProcessor p = null;
				if(format == SqlDateProcessor){
					p = new PdfFillSqlDateProcessor();
					config.registerValueProcessor(Timestamp.class, p);
				}
			}
		}
        return config;
	}
	
}

class PdfFillSqlDateProcessor implements PdfFillProcessor{

	@Override
	public String process(Object value) {
		Date date = (Date)value;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(date);
	}
	
}

