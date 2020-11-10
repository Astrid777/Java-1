package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class WorkWithXML
{
    //Создаем 1.xml
    public static void Create1xml(String Xml1FilePath, Connection connection) throws SQLException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        Statement statement = connection.createStatement();

        String selectSql="select * FROM [Test]";
        statement.execute(selectSql);

        ResultSet resultSet = statement.executeQuery(selectSql);

        try
        {
            builder = factory.newDocumentBuilder();

            // создаем пустой объект Document, в котором будем создавать наш xml-файл
            Document doc = builder.newDocument();

            // создаем корневой элемент
            Element rootElement = doc.createElement( "entries");

            // добавляем корневой элемент в объект Document
            doc.appendChild(rootElement);


            while (resultSet.next())
            {
                rootElement.appendChild(getEntry(doc, resultSet.getString(1)));
            }

            //создаем объект TransformerFactory для печати в консоль
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // для красивого вывода в консоль
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            //печатаем в файл
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File(Xml1FilePath));

            //записываем данные
            transformer.transform(source, file);
            System.out.println("1.xml создан");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // метод для создания нового узла XML-файла
    private static Node getEntry(Document doc, String value)
    {
        Element entry = doc.createElement("entry");

        // создаем элемент fiend
        entry.appendChild(getField(doc, "field", value));

        return entry;
    }

    // утилитный метод для создание нового узла XML-файла
    private static Node getField(Document doc,  String name, String value)
    {
        Element field = doc.createElement(name);
        field.appendChild(doc.createTextNode(value));

        return field;
    }

    //Преобразуем 1.xml в 2.xml
    public static void TransformXLST(String pathInputXML, String templateXLST, String pathOutputXML) throws IOException, URISyntaxException, TransformerException
    {
        Source text = new StreamSource(new File(pathInputXML));
        Source xslt = new StreamSource(new File(templateXLST));

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xslt);

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        transformer.transform(text, new StreamResult(new File(pathOutputXML)));

        System.out.println("2.xml создан");
    }

    //Парсер
    public static ArrayList<Integer> ParserXML(String pathParseXML)
    {
        ArrayList<Integer> fieldsList = new ArrayList<Integer>();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            //Анонимный класс, расширяющий класс DefaultHandler
            DefaultHandler handler = new DefaultHandler()
            {
                // Поле для указания, что тэг NAME начался
                boolean name = false;

                // Метод вызывается когда SAXParser "натыкается" на начало тэга
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
                {
                    // Если тэг имеет имя NAME, то мы этот момент отмечаем - начался тэг NAME

                    if (qName.equalsIgnoreCase("entry"))
                    {
                        name = true;

                        fieldsList.add(Integer.parseInt(attributes.getValue("field")));
                    }
                }
            };

            // Стартуем разбор методом parse, которому передаем наследника от DefaultHandler, который будет вызываться в нужные моменты
            saxParser.parse(pathParseXML, handler);

            System.out.println("Разбор 2.xml закончен");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return fieldsList;
    }
}
