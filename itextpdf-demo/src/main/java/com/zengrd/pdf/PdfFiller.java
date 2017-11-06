package com.zengrd.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;



public class PdfFiller {
	
	public static Object getFieldInObj(Map obj, String fieldName){
		if(fieldName == null)return null;
		try {
			int endIndex;
			if(fieldName.indexOf(".") > 0){
				endIndex = fieldName.indexOf(".");
			}else{
				endIndex = fieldName.length();
			}
			
			Object dObj = obj.get(fieldName.substring(0, endIndex));
			if(fieldName.indexOf(".") == -1){
				return dObj;
			}else{
				fieldName = fieldName.substring(fieldName.indexOf(".")+1);
				return getFieldInObj((Map)dObj, fieldName);
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void fillPdfForm(Map infos, InputStream pdfTemplateStream, OutputStream os) throws Exception{
		fillPdfForm(infos, null, pdfTemplateStream, os, null);
	}
	
	public static void fillPdfForm(Map infos, InputStream pdfTemplateStream, OutputStream os, PdfFillConfig config) throws Exception{
		fillPdfForm(infos, null, pdfTemplateStream, os, config);
	}
	
	public static void fillPdfForm(Map infos, Map<String, String> imgPathMap, InputStream pdfTemplateStream, OutputStream os, PdfFillConfig config) throws  Exception{
		Map<String,Image> imgMap = new HashMap<String,Image>();
		if(imgPathMap != null && !imgPathMap.isEmpty()) {
			for(Entry<String, String> set : imgPathMap.entrySet()){
				String fileName = set.getKey();
				if(imgPathMap != null && imgPathMap.get(fileName) != null){
					Image image = Image.getInstance(set.getValue());
					imgMap.put(fileName, image);
				}
			}
		}
		fillPdfFormWithImage(infos, imgMap, pdfTemplateStream, os, config);
	}
	
	// infos --> Map
	public static void fillPdfFormWithImage(Map infos, Map<String, Image> imgPathMap, InputStream pdfTemplateStream, OutputStream os, PdfFillConfig config) throws Exception{
		PdfReader reader = null;
		PdfStamper stamper = null;
		AcroFields sfs = null;
		reader = new PdfReader(pdfTemplateStream);
		stamper = new PdfStamper(reader, os);
		sfs = stamper.getAcroFields();
		for (Iterator it = sfs.getFields().keySet().iterator(); it.hasNext();) {
			String fileName = it.next().toString();
			if(fileName.startsWith("img.")){
				if(imgPathMap != null && imgPathMap.get(fileName) != null){
					addImg(reader, stamper, fileName, imgPathMap.get(fileName));
				}
			}else{
				Object value = getFieldInObj(infos, fileName);
				if(config != null){
					value = config.config(value);
				}
				if(value != null){
					sfs.setField(fileName, value.toString());
				}
			}
		}
		if(imgPathMap != null){
			for(String key: imgPathMap.keySet()){
				sfs.removeField(key);
			}
		}
		
		stamper.addJavaScript("this.getField('action').value=1;");
		//stamper.setFormFlattening(true);
		stamper.close();
	}
	
	/**
	 * 根据imgFieldName的位置、大小给pdf增加图片
	 * @param reader
	 * @param stamper
	 * @param imgFieldName
	 * @param imgPath
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static void addImg(PdfReader reader, PdfStamper stamper, String imgFieldName, Image image) throws DocumentException, MalformedURLException, IOException{
		Rectangle rectangle = reader.getAcroFields().getFieldPositions(imgFieldName).get(0).position;
        int pageIndex = stamper.getAcroFields().getFieldItem(imgFieldName).getPage(0);
        
        PdfContentByte over = stamper.getOverContent(pageIndex);
        image.setAlignment(Image.ALIGN_CENTER);  
        image.scaleAbsolute(rectangle.getWidth(), rectangle.getHeight());// 控制图片大小  
        image.setAbsolutePosition(rectangle.getLeft(), rectangle.getTop()-rectangle.getHeight());
        over.addImage(image);
	}
	
}
