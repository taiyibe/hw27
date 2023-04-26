package org.example;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Config {
    public String saveFile;
    public FileFormat saveFormat;
    public boolean saveEnable;
    public String loadFile;
    public FileFormat loadFormat;
    public boolean loadEnable;
    public String logFile;
    public boolean logEnable;

    public Config(File conf) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(conf);

        Element root = doc.getDocumentElement();

        Element loadSettings = (Element) root.getElementsByTagName("load").item(0);
        Element saveSettings = (Element) root.getElementsByTagName("save").item(0);
        Element logSettings = (Element) root.getElementsByTagName("log").item(0);

        loadEnable = Boolean.parseBoolean(loadSettings.getElementsByTagName("enabled").item(0).getTextContent());
        loadFile = loadSettings.getElementsByTagName("fileName").item(0).getTextContent();
        if (Objects.equals(loadSettings.getElementsByTagName("fileName").item(0).getTextContent(), "json"))
            loadFormat = FileFormat.JSON;
        else loadFormat = FileFormat.TEXT;

        saveEnable = Boolean.parseBoolean(saveSettings.getElementsByTagName("enabled").item(0).getTextContent());
        saveFile = saveSettings.getElementsByTagName("fileName").item(0).getTextContent();
        if (Objects.equals(saveSettings.getElementsByTagName("fileName").item(0).getTextContent(), "json"))
            saveFormat = FileFormat.JSON;
        else saveFormat = FileFormat.TEXT;

        logEnable = Boolean.parseBoolean(logSettings.getElementsByTagName("enabled").item(0).getTextContent());
        logFile = logSettings.getElementsByTagName("fileName").item(0).getTextContent();
    }
}
