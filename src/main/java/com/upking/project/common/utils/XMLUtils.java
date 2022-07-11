package com.upking.project.common.utils;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author king
 * @version 1.0
 * @className XMLUtils
 * @description TODO
 * @date 2022/6/24
 */
public class XMLUtils {

    private static Logger logger = LoggerFactory.getLogger(XMLUtils.class);

    public static <T> T XML2Object(Class<T> clazz, String xml) throws Exception {
        try {
            if (xml == null) {
                return null;
            }
            JAXBContext ctx = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();

            StringReader reader = new StringReader(xml);
            SAXParserFactory sax = SAXParserFactory.newInstance();
            /**
             * 应用工程使用XML解析器解析外部传入的XM2L文件，如果没有做特殊处理，大多会解析XML文件中的外部实体。 如果外部实体包含URI,那么XML解析器会访问URI指定的资源，例如本地或者远程系统上的文件。 该攻击可用于未经授权访问本地计算机上的文件，扫描远程系统，或者导致本地系统拒绝服务。
             * 通过配置XML解析器，禁止加载外部实体
             * sax.setFeature(“http://apache.org/xml/features/disallow-doctype-decl“,true);
             * sax.setFeature(“http://xml.org/sax/features/external-general-entities“, false);
             * sax.setFeature(“http://xml.org/sax/features/external-parameter-entities“, false);
             */
            sax.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            //忽略命名空间
            sax.setNamespaceAware(false);
            XMLReader xmlReader = sax.newSAXParser().getXMLReader();
            Source source = new SAXSource(xmlReader, new InputSource(reader));
            Object result = unmarshaller.unmarshal(source);
            return (T) result;
        } catch (Exception e) {
            logger.error("XML2Object error:" + xml);
            throw new Exception("XML convert to Object Exception", e);
        }
    }

    public static <T> String object2XML(T t, String charset) throws JAXBException, IOException {
        String result = null;
        StringWriter writer = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(t.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, charset);
            OutputFormat format = new OutputFormat();
            format.setEncoding(charset);

            format.setIndent(false);
            format.setNewlines(false);
            format.setNewLineAfterDeclaration(false);
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.setEscapeText(false);

            marshaller.marshal(t,xmlWriter);
            result = writer.toString();
        } finally {
            writer.close();
        }
        return result;
    }

    /**
     * XML格式字符串转换为Map
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            /**
             * 安全扫描修改，SAX或者DOM解析器会尝试访问在SYSTEM属性中标识的URL，
             * 这意味着它将读取本地/dev/tty文件的内容。
             * 在POSIX系统中，读取这个文件会导致程序阻塞，直到可以通过计算机控制台得到输入数据为止。
             * 这样，攻击者可以使用这个恶意的XML文件来导致系统挂起，程序会受到XML外部实体注入攻击。
             */
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            logger.warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}",
                    ex.getMessage(), strXML);
            throw ex;
        }

    }

    /**
     * 将Map转换为XML格式的字符串
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); // .replaceAll("\n|\r",
        // "");
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

}
