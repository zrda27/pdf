package com.zengrd.pdf;

import java.io.OutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * pdf创建器
 * @author zrd
 *
 */
public class PdfCreater {
	public static class ImageItem{
		private String title;
		private String imgPath;
		public ImageItem(String title, String imgPath){
			this.title = title;
			this.imgPath = imgPath;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getImgPath() {
			return imgPath;
		}
		public void setImgPath(String imgPath) {
			this.imgPath = imgPath;
		}
	}
	
	/**
	 * 根据多张图片创建pdf
	 * @param imageItems
	 * @param os
	 * @throws Exception
	 */
	public static void create(List<ImageItem> imageItems, OutputStream os) throws Exception{
		// 中文字体  
        BaseFont bfChinese = BaseFont.createFont("STSong-Light",  
                "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);  
        Font fontChinese = new Font(bfChinese, 12, Font.BOLD);
        
		// 创建文Pdf文挡50, 50, 50,50左右上下距离  
        Document document = new Document(PageSize.A4);
        // 使用PDFWriter进行写文件操作  
        PdfWriter.getInstance(document, os);  
        document.open();
        
        for(ImageItem imageItem: imageItems){
        	document.newPage();
        	Paragraph pageTitle = new Paragraph(imageItem.getTitle(), fontChinese);
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(pageTitle);
            Image image = Image.getInstance(imageItem.getImgPath());// 选择图片  
            image.setAlignment(Image.MIDDLE);  
            image.scaleToFit(450, 750);// 控制图片大小  
            document.add(image);
        }
        
        document.close();
	}
}
