

package DataModel;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

import static DataModel.DatabaseUtility.*;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 15/04/2020
 * Version: 1.0
 * Class:RiskyPersons
 * This class is for recording and displaying the statistics of people at the risk of homelessness.
 * */
public class RiskyPersons implements DatabaseInterface {

    private final String[] LIST_COLUMNS ={
                                        "SA3TenantCategoryID",
                                        "IncomeSource",
                                        "IncomeID",
                                        "SA3code",
                                        "AgeID",
                                        "NumberPeople",
                                        "Gender"};

    private SA3TenantCategory SA3Category;
    private int numberPeople;

/**
 * this is parameter constructor
 * @param SA3Category the class contain "SA3TenantCategoryID",
 *                                         "IncomeSource",
 *                                         "IncomeID",
 *                                         "SA3code",
 *                                         "AgeID",
 *                                         "NumberPeople",
 *                                         "Gender"
 * @param numberPeople the number of people in this area
 * */
    public RiskyPersons( SA3TenantCategory SA3Category, int numberPeople) {
        this.SA3Category = SA3Category;
        this.numberPeople = checkNumberOfPeople(numberPeople);
    }
    /**
     * this is default constructor
     * */
    public RiskyPersons() {
    }
/**
 * getter and setter
 * */
    public SA3TenantCategory getSA3Category() {
        return SA3Category;
    }

    public void setSA3Category(SA3TenantCategory SA3Category) {
        this.SA3Category = SA3Category;
    }

    public int getNumberPeople() {
        return numberPeople;
    }

    public void setNumberPeople(int numberPeople) {
        this.numberPeople = numberPeople;
    }
    /**
     * check number of people
     * @param number input number
     * @return a correct number
     * */
    private int checkNumberOfPeople(int number)
    {
        if(number>=0)
            return number;
        else
            throw new InputMismatchException("Number ["+number+"] should be greater than 0.");
    }
    @Override
    public String toString() {
        return String.format("[%s] [%d]",this.SA3Category,this.numberPeople);
    }

    /**this method will insert riskyPersons in the database and
     * get the primary key back to set keyID in SA3Category class
     * @param connection the current connection
     * @param tableName table name
     * @exception SQLException
     *
     * */
    @Override
    public <T extends Connection> void selfInsert(T connection,String tableName) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(generateSQLQueries(
                SQL_INSERT,
                tableName,
                Arrays.asList(LIST_COLUMNS),
                Arrays.asList(this.SA3Category.getSA3TenantCategoryID(),
                        this.SA3Category.getIncomeSource().getName(),
                        this.SA3Category.getIncomeCategory().getKeyID(),
                        this.SA3Category.getLocation().getSA3code(),
                        this.SA3Category.getAgeCategory().getKeyID(),
                        this.numberPeople,
                        this.SA3Category.getGenderCategory().getName()),
                null), Statement.RETURN_GENERATED_KEYS);

        preparedStatement.executeUpdate();
        ResultSet listOfPrimaryKey = preparedStatement.getGeneratedKeys();

        while (listOfPrimaryKey.next())
        {
            this.SA3Category.setSA3TenantCategoryID(Integer.parseInt(listOfPrimaryKey.getObject(1).toString()));
            //System.out.println(this.SA3Category.getSA3TenantCategoryID());
        }
    }
}
