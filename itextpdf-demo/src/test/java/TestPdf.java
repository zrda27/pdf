import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.zengrd.pdf.PdfFiller;

public class TestPdf {
	@Test
	public void test() throws FileNotFoundException, Exception{
		Map infos = new HashMap();
		infos.put("xingming", "䶮");
		PdfFiller.fillPdfForm(infos , TestPdf.class.getResourceAsStream("/test.pdf"), new FileOutputStream("D://tes.pdf"));
	}
}
