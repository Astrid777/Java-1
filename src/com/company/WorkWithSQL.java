package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class WorkWithSQL
{
    private String server;
    private String dataBase;
    private String user;
    private String password;

    public WorkWithSQL(String server, String dataBase, String user, String password)
    {
        this.server = server;
        this.dataBase = dataBase;
        this.user = user;
        this.password = password;
    }

    public String getServer()
    {
        return server;
    }

    public String getDataBase()
    {
        return dataBase;
    }

    public String getUser()
    {
        return user;
    }

    public String getPassword()
    {
        return password;
    }

    public void FillTable(Connection connection, int N) throws SQLException
    {
        Statement statement = connection.createStatement();

        //очистить таблицу
        String truncateSQL = "TRUNCATE TABLE [Test]";
        statement.execute(truncateSQL);

        //добавление в базу данных
        String insertSql;

        for (int i=0; i<N; i++)
        {
            insertSql = "INSERT into [Test](Field) values (" + i + ")";
            statement.execute(insertSql);
        }

        System.out.println("Таблицу Test заполнили");
        System.out.println("Таблица заполнена " + N + " значениями");

    }


    public Connection ConnectDb()
    {
        //подключаемся к таблице
        Connection connection=null;


        String connectionUrl =
                "jdbc:sqlserver://" + server + ";"
                        + "database=" + dataBase + ";"
                        + "user=" + user + ";"
                        + "password=" + password + ";"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        //пытаемся установить соединение
        try
        {
            connection = DriverManager.getConnection(connectionUrl);

            System.out.println("Соединение с СУБД выполнено.");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return connection;
    }
}
