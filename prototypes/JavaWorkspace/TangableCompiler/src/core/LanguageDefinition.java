package core;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ast.Command;
import ast.Keyword;
import ast.LiteralNumber;
import ast.Statement;

/**
 * Responsible for parsing and generating a list of all tokens for use as part of
 * the current language. The language consists of the following token types:
 * 		-> NumberToken
 * 		-> KeywordToken
 * 		-> CommandToken
 * 
 * @author Paul Hickman
 * @version1.0
 */
public class LanguageDefinition {


	/**
	 * Map of all valid token currently available within the language
	 */
	public final Map<Integer, Statement> tokens = 
			new HashMap<Integer, Statement>();

	/**
	 * Static instance to facilitate the Singleton Pattern
	 */
	private static LanguageDefinition instance;

	/**
	 * Called in place of a constructor, returns the static LanguageDefinition 
	 * reference
	 * @return instance
	 */
	public static LanguageDefinition getInstance(){
		if(instance == null)
			instance = new LanguageDefinition();

		return instance;
	}

	
	/**
	 * Setup a new language definition based on the supplied file
	 * @param langDefinitionFileName
	 * @return false is language file failed to parse, otherwise true
	 */
	public boolean SetupLanguage(String langDefinitionFileName){

		try {
			File xmlFile = new File(langDefinitionFileName);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize();


			// Command Token Nodes
			NodeList commandNodes = doc.getElementsByTagName("CommandToken");

			for (int temp = 0; temp < commandNodes.getLength(); temp++) {

				Node nNode = commandNodes.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					tokens.put(Integer.parseInt(eElement.getAttribute("tokenId")),
							new Command(eElement.getAttribute("name"), 
									Integer.parseInt(eElement.getAttribute("tokenId")), 
									Integer.parseInt(eElement.getAttribute("outputId")),
									Integer.parseInt(eElement.getAttribute("waitTime"))));
				}
			}

			// Keyword Token Nodes
			NodeList keywordNodes = doc.getElementsByTagName("KeywordToken");

			for (int temp = 0; temp < keywordNodes.getLength(); temp++) {

				Node nNode = keywordNodes.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					tokens.put(Integer.parseInt(eElement.getAttribute("tokenId")),
							new Keyword(eElement.getAttribute("name"), 
									Integer.parseInt(eElement.getAttribute("tokenId")), 
									Integer.parseInt(eElement.getAttribute("outputId"))));
				}
			}

			// Number Token Nodes
			NodeList numberToken = doc.getElementsByTagName("NumberToken");

			for (int temp = 0; temp < numberToken.getLength(); temp++) {

				Node nNode = numberToken.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					tokens.put(Integer.parseInt(eElement.getAttribute("tokenId")),
							new LiteralNumber(eElement.getAttribute("name"), 
									Integer.parseInt(eElement.getAttribute("tokenId")), 
									Integer.parseInt(eElement.getAttribute("outputId")),
									Integer.parseInt(eElement.getAttribute("value"))));
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
