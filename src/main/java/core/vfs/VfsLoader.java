package core.vfs;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.Base64;

public class VfsLoader {

    public static VfsDirectory loadFromXml(File xmlFile) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);

        Element root = doc.getDocumentElement();
        NodeList dirs = root.getElementsByTagName("dir");
        Element top = null;
        for (int i = 0; i < dirs.getLength(); i++) {
            Element d = (Element) dirs.item(i);
            if ("/".equals(d.getAttribute("name"))) {
                top = d;
                break;
            }
        }
        if (top == null) {
            throw new IllegalArgumentException("VFS XML must contain a <dir name=\"/\"> root element");
        }
        VfsDirectory rootDir = new VfsDirectory("/");
        parseDir(top, rootDir);
        return rootDir;
    }

    private static void parseDir(Element dirEl, VfsDirectory dirNode) {
        NodeList children = dirEl.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) continue;
            Element el = (Element) n;
            if ("dir".equals(el.getTagName())) {
                String name = el.getAttribute("name");
                VfsDirectory sub = new VfsDirectory(name);
                dirNode.addChild(sub);
                parseDir(el, sub);
            } else if ("file".equals(el.getTagName())) {
                String name = el.getAttribute("name");
                String enc = el.getAttribute("encoding");
                String text = el.getTextContent().trim();
                byte[] data;
                if (text.isEmpty()) {
                    data = new byte[0];
                } else {
                    data = Base64.getDecoder().decode(text);
                }
                VfsFile file = new VfsFile(name, data);
                dirNode.addChild(file);
            }
        }
    }
}
