package ru.itis.services;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.codec.Base64;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.util.Map;
import java.util.UUID;

public class PdfMaker {
    private final TemplateResolver templateResolver;

    public PdfMaker() {
        templateResolver = new TemplateResolverImpl();
    }

    public void makePdf(String template, Map<String, String> map) {
        try {
            String htmlString = templateResolver.process(template, map);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlString, null);
            renderer.layout();
            String fileNameWithPath = "output/" + UUID.randomUUID().toString() + ".pdf";
            FileOutputStream os = new FileOutputStream(fileNameWithPath);
            renderer.createPDF(os);
            os.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
