package mmaa.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Multi-method authentication architecture Server configuration.
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public class MmaaServerConfiguration {

    private static final String configFile = "config/MmaaConfig.xml";
    private static final XPath xPath = XPathFactory.newInstance().newXPath();

    /**
     * Check is user can create temporary account.
     *
     * @param username the unique id of the user.
     * @return true if the user can create a temporary in this host.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    public static boolean getAuthorization(String username) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String expression = "/config/authorized/user";
        Document document = getConfigDocument();
        NodeList users = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < users.getLength(); i++) {
            if (users.item(i).getTextContent().compareTo(username) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check the demanded PIN length at this host.
     *
     * @return the pin length.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    public static int getPinLength() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String expression = "/config/pin/length";
        Document document = getConfigDocument();
        int length = Integer.parseInt(xPath.compile(expression).evaluate(document));
        return length;
    }

    /**
     * Check the expiration date of a temporary account to be created.
     *
     * @return Calendar with the expiration date.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws DatatypeConfigurationException
     * @throws XPathExpressionException
     * @throws ParseException
     */
    public static Calendar getExpirationDate() throws ParserConfigurationException, SAXException, IOException, DatatypeConfigurationException, XPathExpressionException, ParseException {
        Calendar calendar = new GregorianCalendar();
        Document document = getConfigDocument();
        int weeks = Integer.parseInt(xPath.compile("/config/pin/duration/weeks").evaluate(document));
        int days = Integer.parseInt(xPath.compile("/config/pin/duration/days").evaluate(document));
        calendar.add(Calendar.DAY_OF_MONTH, weeks * 7 + days);
        return calendar;
    }

    /**
     * Parses the XML configuration document.
     *
     * @return Parsed XML configuration document.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static Document getConfigDocument() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(configFile));
        return document;
    }
}
