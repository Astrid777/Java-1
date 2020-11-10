package com.company;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main
{

    public static void main(String[] args) throws SQLException, TransformerException, IOException, URISyntaxException
    {
        var sqlServer = new WorkWithSQL("localhost:1433", "TestBd", "sa", "123");

        Connection connection = sqlServer.ConnectDb();
        sqlServer.FillTable(connection,1000000);


        WorkWithXML.Create1xml("1.xml", connection);
        WorkWithXML.TransformXLST("1.xml", "transform.xslt", "2.xml");

        ArrayList<Integer> numbers = new ArrayList<>();
        numbers = WorkWithXML.ParserXML("2.xml");

        int Sum=0;
        for (Integer item : numbers)
        {
            Sum=Sum + item;
        }

        System.out.println("Сумма = " + Sum);

        connection.close();
    }
}
