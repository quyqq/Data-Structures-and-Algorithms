
package DataModel.EnumDatabase;

import DataModel.DatabaseInterface;
import DataModel.DatabaseUtility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;

import static DataModel.DatabaseUtility.SQL_INSERT;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 25/05/2020
 * Version: 1.0
 * Enum: WEEKLY_INCOME
 * This enum for storing data
 * */
public enum WEEKLY_INCOME implements DatabaseInterface {
    NilIncome(1,"Negative/Nil income",-2000,0),
    Below$400(2,"$1-$399",1,399),
    Below$600(3,"$400-$599",400,599),
    Below$1000(4,"$600-$999",600,999);
    private final int keyID;
    private final String name;
    private final int incomeFrom;
    private final int incomeTo;
    /**
     * this is constructor
     * */
    WEEKLY_INCOME(int keyID, String name, int incomeFrom, int incomeTo) {
        this.keyID = keyID;
        this.name = name;
        this.incomeFrom = incomeFrom;
        this.incomeTo = incomeTo;
    }

/**
 * this method find weekly income base on income rage
 * @param incomeRage income rage
 * @return an enum of Weekly Income
 * */
    public static WEEKLY_INCOME findWeeklyIncome(String incomeRage)
    {
        for (WEEKLY_INCOME weekly_income:WEEKLY_INCOME.values()) {
            if(weekly_income.getName().equalsIgnoreCase(incomeRage))
                return weekly_income;
        }
        return null;
    }
    /**
     * this method find weekly income base on income id
     * @param incomeID income rage
     * @return an enum of Weekly Income
     * */
    public static WEEKLY_INCOME findWeeklyIncome(int incomeID)
    {
        for (WEEKLY_INCOME weekly_income:WEEKLY_INCOME.values()) {
            if(weekly_income.getKeyID()==incomeID)
                return weekly_income;
        }
        return null;
    }
    /**
     * getter and setter
     * */
    public int getKeyID() {
        return keyID;
    }

    public String getName() {
        return name;
    }

    public int getIncomeFrom() {
        return incomeFrom;
    }

    public int getIncomeTo() {
        return incomeTo;
    }

    /**
 * this generic method insert weekly income to database
 * @param connection the current connection
 * */
    @Override
    public <T extends Connection> void selfInsert(T connection,String tableName) throws SQLException {

        Statement statement = connection.createStatement();
        statement.executeUpdate(DatabaseUtility.generateSQLQueries(
                SQL_INSERT,
                tableName,
                Arrays.asList("IncomeID","Name","IncomeFrom","IncomeTo"),
                Arrays.asList(this.keyID,this.name,this.incomeFrom,this.incomeTo),
                null));

    }



}
