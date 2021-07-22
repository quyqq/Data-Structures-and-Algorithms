
package DataModel;

import DataModel.EnumDatabase.AGE;
import DataModel.EnumDatabase.GENDER;
import DataModel.EnumDatabase.INCOME_SOURCE;
import DataModel.EnumDatabase.WEEKLY_INCOME;
import OtherMethods.DataFile;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 *This class helps to connect to MySQL server, check and create database, insert, delete, update to the tables
 *
 *
 * */
public class DatabaseUtility {

    public final static int SQL_INSERT = 0;
    public final static int SQL_UPDATE = 1;
    public final static int SQL_DELETE = 2;
    public final static int SQL_SELECT = 3;
    private static LinkedList<Location> locations;
    private static LinkedList<RiskyPersons> riskyPersons;

    private final String DATABASE_URL_NAME = "jdbc:mysql://localhost:3306/%s?useSSL=false";
    private final String SQL_CONNECTION = "MiklesLissa_12123973_QuyetQuangQuy_12118217";
    private final String LOCATION_TABLE = "LOCATION";
    private final String WEEKLY_INCOME_TABLE= "WEEKLY_INCOME";
    private final String AGE_TABLE ="AGE";
    private final String SA3TENANTCATEGORY_TABLE = "SA3TENANTCATEGORY";
    private final String USERNAME = "super";
    private final String PASSWORD = "123456";
    private final String CREATE_DATABASE = "CREATE DATABASE " + SQL_CONNECTION;
    private final String CREATE_LOCATION_TABLE =
                    "create table "+LOCATION_TABLE+" (\n" +
                    "SA3code nvarchar(5) not null primary key,\n" +
                    "Name nvarchar(200) not null\n" +
                    ");";
    private final String CREATE_WEEKLY_INCOME_TABLE =
                    "create table "+WEEKLY_INCOME_TABLE+"(\n" +
                    "IncomeID bigint primary key ,\n" +
                    "Name nvarchar(100) not null unique,\n" +
                    "IncomeFrom decimal(12,2) not null unique,\n" +
                    "IncomeTo decimal(12,2) not null unique\n" +
                    ");";
    private final String CREATE_AGE_TABLE =
                    "create table "+AGE_TABLE+"(\n" +
                    "AgeID bigint primary key ,\n" +
                    "Name nvarchar(100) not null unique,\n" +
                    "AgeFrom int not null unique,\n" +
                    "AgeTo int not null unique\n" +
                    ");";
    private final String CREATE_SA3TENANTCATEGORY_TABLE =
                    "create table "+SA3TENANTCATEGORY_TABLE+"\n" +
                    "(\n" +
                    "SA3TenantCategoryID bigint primary key auto_increment,\n" +
                    "IncomeSource nvarchar(20) not null,\n" +
                    "IncomeID bigint not null,\n" +
                    "SA3code nvarchar(5) not null,\n" +
                    "AgeID bigint not null,\n" +
                    "NumberPeople bigint not null,\n" +
                    "Gender nvarchar(10) not null,\n" +
                    "constraint FK_SA3TenantCategory_WeeklyIncome foreign key (IncomeID) \n" +
                    "references weekly_income(IncomeID),\n" +
                    "constraint FK_SA3TenantCategory_Location foreign key(SA3code)\n" +
                    "references location(SA3code),\n" +
                    "constraint FK_SA3TenantCategory_Age foreign key(AgeID)\n" +
                    "references Age(AgeID)\n" +
                    ");";

    private Connection connection;
    private ResultSet resultSet;

/**
 * this is the default constructor
 * @exception SQLException
 * */
    public DatabaseUtility() throws SQLException {

        locations= new LinkedList<>();
        riskyPersons= new LinkedList<>();
        openConnection();
        createDatabase();
        createTables();
        loadDatabase();
    }
    /**
     * this constructor will add data to the linkedList(s) and also make sure that list is unique
     * @param locations list of locations
     * @param riskyPersons list of riskyPersons
     * */
    public DatabaseUtility(LinkedList<Location> locations, LinkedList<RiskyPersons> riskyPersons) {
        this.locations = locations.stream().distinct().collect(Collectors.toCollection(LinkedList::new));
        this.riskyPersons = riskyPersons.stream().distinct().collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * this method gets list of location
     * @return List of locations
     *
     * */
    public static LinkedList<Location> getLocations() {
        return locations;
    }

    /**
     * this method gets list of riskyPerson
     * @return LinkedList of riskyPerson LinkedList<RiskyPersons>
     * */
    public static LinkedList<RiskyPersons> getRiskyPersons() {
        return riskyPersons;
    }

    /**
     * this method finds riskyPerson in the linkedList
     * @param persons a riskyPerson data need to be found
     * @return List of risky person
     *
     * */
    public static List<RiskyPersons> findRiskyPerson(RiskyPersons persons) {
        return DatabaseUtility.getRiskyPersons().stream().
                filter(currentPersons ->
                        persons.getSA3Category().getLocation().
                                compareLocation(currentPersons.getSA3Category().getLocation()) &&
                        persons.getSA3Category().getIncomeCategory().
                                equals(currentPersons.getSA3Category().getIncomeCategory()) &&
                        persons.getSA3Category().getIncomeSource().
                                equals(currentPersons.getSA3Category().getIncomeSource()) &&
                        persons.getSA3Category().getAgeCategory().
                                equals(currentPersons.getSA3Category().getAgeCategory()) &&
                        persons.getSA3Category().getGenderCategory().
                                equals(currentPersons.getSA3Category().getGenderCategory())).
                collect(Collectors.toList());
    }

    /**
     * this method generate SQL code for insert delete update into tables
     * @param type this parameter is type of query such as Insert,Delete,Update
     * @param tableName : name of table will be impacted
     * @param listOfColumnName: list of columns has in the table
     * @param listOfValues: list of values will update to the table
     * @param condition: condition for update and delete
     * @return String: SQL code
     * */
    public static <T> String generateSQLQueries(int type, String tableName, List<String> listOfColumnName,
                                                List<T> listOfValues, String condition)
    {

        String sqlCode = "";
        if (type == SQL_INSERT) {


            sqlCode = String.format("insert into %s (%s) VALUES (%s)",
                    tableName,
                    listOfColumnName.stream().collect(Collectors.joining(", ")),
                    listOfValues.stream().map(DatabaseUtility::generateCorrectFormat).
                                                collect(Collectors.joining(",")));
        }
        if (type == SQL_UPDATE) {


            AtomicInteger counter = new AtomicInteger();


            sqlCode = String.format("update %s set %s where %s",
                    tableName,
                    listOfColumnName.stream().map(s -> {

                        if (counter.get() < listOfValues.size()-1)
                            return s + " = " + generateCorrectFormat(listOfValues.get(counter.getAndIncrement())) + ", ";
                        else
                            return s + " = " + generateCorrectFormat(listOfValues.get(counter.getAndIncrement()));
                    }).collect(Collectors.joining()),
                    condition);
        }
        if (type == SQL_DELETE) {

            sqlCode = String.format("delete from %s where %s", tableName, condition);
        }

        if(type==SQL_SELECT)
        {
            sqlCode = String.format("Select %s from %s %s",
                    listOfColumnName.stream().collect(Collectors.joining(", ")),
                    tableName,
                    condition
            );
        }

        return sqlCode;

    }


    /**
     * this method will load data from database to LinkedList<Location> and LinkedList<RiskyPersons>
     * @exception SQLException
     * */
    public void loadDatabase() throws SQLException {
        riskyPersons.clear();
        locations.clear();
        Statement statement = connection.createStatement();
        resultSet = statement.executeQuery(generateSQLQueries(
                                            SQL_SELECT,
                                            LOCATION_TABLE,
                                            Arrays.asList("SA3Code","Name"),
                                            null,
                                            ""));
        while (resultSet.next())
        {
            locations.add(new Location(resultSet.getString(1),resultSet.getString(2)));
        }
        if(locations.size()>0)
            System.out.println("Location form database is loaded.");
        //else
           // System.out.println("Location form database is empty");

        resultSet = statement.executeQuery(generateSQLQueries(
                SQL_SELECT,
                SA3TENANTCATEGORY_TABLE,
                Arrays.asList(  "SA3TenantCategoryID",
                                "IncomeSource",
                                "IncomeID",
                                "SA3code",
                                "AgeID",
                                "NumberPeople",
                                "Gender"
                        ),
                null,
                ""
        ));

        while (resultSet.next())
        {

            riskyPersons.add(new RiskyPersons(
                    new SA3TenantCategory(
                            resultSet.getInt(1),
                            locations.stream().filter(location -> {
                                try {
                                    if(location.getSA3code().equalsIgnoreCase(resultSet.getString(4)))
                                        return true;
                                } catch (SQLException e) {
                                    //System.out.println(e.getMessage());
                                    //e.printStackTrace();
                                    DataFile.showException(e);
                                }
                                return false;
                            }).findFirst().get(),
                            WEEKLY_INCOME.findWeeklyIncome(resultSet.getInt(3)),
                            INCOME_SOURCE.findIncomeSource(resultSet.getString(2)),
                            AGE.findAgeGroup(resultSet.getInt(5)),
                            GENDER.findGender(resultSet.getString(7))
                    ),
                    Integer.parseInt(resultSet.getObject(6).toString())
            ));
        }
        if(riskyPersons.size()>0)
            System.out.println("RiskyPerson from the database is loaded.");
        //else
            //System.out.println("RiskyPerson in database is empty");

        statement.close();


    }




    /**
     * this method create correct format for SQL Code such as 1 -> 1; ABC -> 'ABC'
     * @param dataIn any kind of Serializable such as number or string
     * @return String a correct format
     * */
    private static  <T>  String  generateCorrectFormat(T dataIn) {
        if (!(dataIn instanceof Number))
            return "'" + dataIn.toString() + "'";
        else
            return dataIn.toString();
    }



    /**
     * this method creates new connection if it cannot connect to the database
     * that means the database needs to be created others should be error
     * @throws SQLException
     * */

    private void openConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(String.format(DATABASE_URL_NAME,
                                                                    SQL_CONNECTION), USERNAME, PASSWORD);
            System.out.println("Server is Connected");
        } catch (SQLException ex) {
            connection = DriverManager.getConnection(String.format(DATABASE_URL_NAME, ""),
                                                        USERNAME, PASSWORD);
            System.out.println("Server is Connected");
        }


    }

    /**
     * Create database if the database is not existed
     *Insert data to two tables : AGE and WEAKLY_INCOME
     * @throws SQLException
     */
    private void createDatabase() throws SQLException {

        boolean databaseExisted = false;
        Statement statement = connection.createStatement();
        resultSet = connection.getMetaData().getCatalogs();
        while (resultSet.next()) {

            if (resultSet.getObject(resultSet.getMetaData().getColumnCount()).
                    toString().equalsIgnoreCase(SQL_CONNECTION)) {
                databaseExisted = true;
                break;
            }

        }
        if (!databaseExisted) {
            statement.executeUpdate(CREATE_DATABASE);
            System.out.println("Database is created.");
        }
        statement.close();
        closeConnection();
        openConnection();

    }
    /**
     * this method create table if not existing in the sever
     * @exception SQLException
     * */
    private void createTables() throws SQLException {
        Statement statement = connection.createStatement();


        if (checkTableIsExist(LOCATION_TABLE,statement)) {
            statement.executeUpdate(CREATE_LOCATION_TABLE);
            System.out.println(LOCATION_TABLE + " is created.");
        }
        if (checkTableIsExist(WEEKLY_INCOME_TABLE,statement)) {
            statement.executeUpdate(CREATE_WEEKLY_INCOME_TABLE);
            System.out.println(WEEKLY_INCOME_TABLE + " is created.");

            //WEEKLY_INCOME.values()[WEEKLY_INCOME.values().length-1].selfInsert(connection,WEEKLY_INCOME_TABLE);
            //System.out.println(WEEKLY_INCOME_TABLE + " has data inserted.");


        }
        if (checkTableIsExist(AGE_TABLE,statement)) {
            statement.executeUpdate(CREATE_AGE_TABLE);
            System.out.println(AGE_TABLE + " is created.");

            //AGE.values()[AGE.values().length-1].selfInsert(connection,AGE_TABLE);
            //System.out.println(AGE_TABLE + " has data inserted.");

        }
        if (checkTableIsExist(SA3TENANTCATEGORY_TABLE,statement)) {
            statement.executeUpdate(CREATE_SA3TENANTCATEGORY_TABLE);
            System.out.println(SA3TENANTCATEGORY_TABLE + " is created.");
        }
        statement.close();
    }
    /**
     * this method checks the passing table is existed  and return result
     * @param  tableName name of the table that need to be checked in the database.
     * @param  statement this is the statement from current connection
     * @throws SQLException
     * @return boolean if it is true that means the table is not existed or the opposite
     *
     * */
    private boolean checkTableIsExist(String tableName,Statement statement) throws SQLException {

        resultSet = statement.executeQuery("Show tables");

        while (resultSet.next())
        {
            if(resultSet.getObject(resultSet.getMetaData().getColumnCount()).
                    toString().equalsIgnoreCase(tableName))
                return false;
        }
        return true;
    }
    /**
     * this method increase the number of RiskyPerson if it is existing
     * @param persons the new data in
     * @param increaseTo the number need to bee increase by
     * @exception SQLException
     *
     * */
    public void addOrIncreaseRiskyPerson(RiskyPersons persons,int increaseTo) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate(generateSQLQueries(
                SQL_UPDATE,
                SA3TENANTCATEGORY_TABLE,
                Collections.singletonList("NumberPeople"),
                Collections.singletonList(persons.getNumberPeople()+increaseTo),
                "SA3TenantCategoryID = " + persons.getSA3Category().getSA3TenantCategoryID()

        ));

        persons.setNumberPeople(persons.getNumberPeople()+increaseTo);
    }


    /**
     * this generic method will handles all insert from user to the database
     * @param newElements is LinkedList of interface DatabaseInterface which is implemented on RiskyPersons and Location
     *                    in class RiskyPerson implemented method it's own insertData
     *                    in class Location implemented method it's own insertData
     *                    with the line <T>.element.selfInsert will active the insertData in the actual class
     *
     * */
    public <T extends LinkedList<DatabaseInterface>> void insertData(T newElements) throws SQLException {

        for (DatabaseInterface element : newElements) {


            if(element instanceof Location)
            {
                element.selfInsert(connection,LOCATION_TABLE);
                locations.add((Location) element);

            }
            else if(element instanceof RiskyPersons)
            {
                element.selfInsert(connection,SA3TENANTCATEGORY_TABLE);
                riskyPersons.add((RiskyPersons) element);
            }
            else if(element instanceof AGE)
            {
                element.selfInsert(connection,AGE_TABLE);
            }
            else if(element instanceof WEEKLY_INCOME)
            {
                element.selfInsert(connection,WEEKLY_INCOME_TABLE);
            }
        }

    }

    /**
     * this method closes the current connection
     * @exception SQLException
     * */
    public void closeConnection() throws SQLException {
        if (connection!=null) {
            connection.close();
            System.out.println("Server is Disconnected");

        }
    }

}
