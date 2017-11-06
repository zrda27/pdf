package com.zengrd.pdf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

/**
 * pdf合并器
 * @author zrd
 *
 */
public class PdfMerger {
	/**
	 * 将pdf文件流合并成一个
	 * @param inputStreams
	 * @param os
	 * @throws Exception
	 */
	public static void merge(List<InputStream> inputStreams, OutputStream os) throws Exception{
		Document document = new Document(PageSize.A4);
        PdfCopy copy = new PdfCopy(document, os);
        document.open();
        for(InputStream is: inputStreams){
        	PdfReader reader = new PdfReader(is);
            int pageCount = reader.getNumberOfPages();
            for(int i=0; i<pageCount; i++){
            	document.newPage();
            	copy.addPage(copy.getImportedPage(reader, i+1));
            }
        }
        document.close();
        copy.close();
	}
}
