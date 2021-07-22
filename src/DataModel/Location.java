
package DataModel;

import OtherMethods.Regexs;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

import static DataModel.DatabaseUtility.*;


/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 26/05/2020
 * Version: 1.1
 * Class: Location
 * This class is to read the given file and store the locations in an ArrayList.
 * A Location has a SA3code which is a five digit number.
 * Each state has a range for the SA3code and for Western Australia all SA3codes start with digit ‘5’.
 * The Location has a name which can be more than one word separated by ‘- ‘and a space before ‘ ‘ and space ‘ ‘ after (eg: “Augusta - Margaret River – Busselton”.
 * The name should be validated so that it contains only alphabets other than the ‘-‘ and space characters
 * */
public class Location implements DatabaseInterface {


    public static final String[] columnsName = {"SA3code","Name"};

    private String SA3code;// will be a primary key in the database
    private String name;


    /**
     * this method a parameter constructor
     * @param SA3code post code
     * @param name nam of the location
     * */
    public Location( String SA3code, String name) {
        this.SA3code = checkSA3code(SA3code);
        this.name = checkLocationName(name);
    }
    /**
     * this method default constructor
     * */
    public Location() {
        this.SA3code = "";
        this.name="";
    }
    /**
     * this method check name for new location
     * @param locationName new location name
     * @return correct format location name
     * @exception InputMismatchException
     * */
    public static String checkLocationName(String locationName) throws InputMismatchException
    {

        if(locationName.trim().length()>0)
        {
            String listOfWords[] = locationName.trim().split(" ");
            String message = "";
            String finalWords="";
            boolean errorAppear = false;
            for (String word:listOfWords) {
                if(!word.matches(Regexs.CHECK_CORRECT_LOCATION_NAME_WORD)&&
                        !word.matches(Regexs.CHECK_MINUS_CHARACTER))
                {
                    if(word.length()==0)
                        continue;
                    message+=word+" ";
                    errorAppear=true;
                }
                else
                {
                    if(word.matches(Regexs.CHECK_CORRECT_LOCATION_NAME_WORD))
                        finalWords+=word+" ";
                }
            }
            if(errorAppear)
            {
                throw new InputMismatchException(String.format("Those words : [%s] contain character which is not alphabet.\nPlease check location name again.",message.trim()));
            }
            else
                return finalWords.trim().replaceAll(" "," - ");

        }
        else
        {
            throw new InputMismatchException("Name of location can not be blank");
        }
    }
    /**
     * this method set name method
     * @param name location name
     * */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * this method get postcode
     * @return post code of location
     * */
    public String getSA3code() {
        return SA3code;
    }
    /**
     * this method get name of location
     * @return name of locaiton
     * */
    public String getName() {
        return name;
    }
    /**
     * this method help to compare between 2 locations
     * @param location the comparing location
     * @return true if is equal and the opposite
     * */
    public boolean compareLocation(Location location)
    {
        return location.name.trim().equalsIgnoreCase(this.name.trim()) &&
                location.SA3code.trim().equalsIgnoreCase(this.SA3code.trim());
    }

    /**
     * this method check post code for new location
     * @param sa3code Post code
     * @return the correct format code
     * @exception InputMismatchException
     * */
     private String checkSA3code(String sa3code) throws InputMismatchException
     {

         if(sa3code.trim().matches(Regexs.CHECK_CORRECT_SA3_CODE))
             return sa3code.trim();
         else
         {
             if(sa3code.trim().matches(Regexs.CHECK_NOT_5_DIGITS))
             {
                 throw new InputMismatchException("SA3 code has to start with digit ‘5’. Please check the code again.");
             }
             else
             {
                 throw new InputMismatchException("SA3 code has to have 5 digits. Please heck the code again.");
             }


         }
     }


    /**
     * this method will insert itself to the database
     * @param connection the connect to the database
     * @param tableName the name of the table to store
     * @exception SQLException
     * */
    @Override
    public void selfInsert(Connection connection,String tableName) throws SQLException
    {

        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseUtility.generateSQLQueries(
                SQL_INSERT,
                tableName,
                Arrays.asList(columnsName),
                Arrays.asList(this.SA3code,this.name),
                null));
        preparedStatement.executeUpdate();


    }
    /**
     * this method override method
     * @return string
     * */
    @Override
    public String toString() {
        return String.format(" [%s] [%s]",this.SA3code,this.name);
    }


}
