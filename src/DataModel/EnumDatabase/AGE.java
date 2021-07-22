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
 * Enum: AGE
 * This Enum for storing age category
 * */
public enum AGE implements DatabaseInterface {
    From50To54(1,"50-54",50,54),
    From55To59(2,"55-59",55,59),
    From60To64(3,"60-64",60,64),
    Over65(4,"over 65",65,2000);

    private final int keyID;
    private final String name;
    private final int fromAge;
    private final int toAge;
/**
 * this is constructor
 * */
    AGE(int keyID, String name, int fromAge, int toAge) {
        this.keyID = keyID;
        this.name = name;
        this.fromAge = fromAge;
        this.toAge = toAge;
    }
    /**
     * this is find age base on age range
     * @param ageRange age range
     * @return an enum of AGE
     * */
    public static AGE findAgeGroup(String ageRange)
    {
        for (AGE age:AGE.values()) {
            if(age.getName().equalsIgnoreCase(ageRange))
                return age;
        }
        return null;
    }
    /**
     * this is find age base on age range
     * @param ageID age range
     * @return an enum of AGE
     * */
    public static AGE findAgeGroup(int ageID)
    {
        for (AGE age:AGE.values()) {
            if(age.getKeyID()== ageID)
                return age;
        }
        return null;
    }
    /**
     * this method insert an enum to database
     * @param tableName table name in database
     * @param connection the current connection
     * @exception SQLException
     * */
    @Override
    public <T extends Connection> void selfInsert(T connection, String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(DatabaseUtility.generateSQLQueries(
                SQL_INSERT,
                tableName,
                Arrays.asList("AgeID","Name","AgeFrom","AgeTo"),
                Arrays.asList(this.keyID,this.name,this.fromAge,this.toAge),
                null));

    }


    /**
     * getter and setter
     * */
    public String getName() {
        return name;
    }
    public int getKeyID() {
        return keyID;
    }

    public int getFromAge() {
        return fromAge;
    }

    public int getToAge() {
        return toAge;
    }

}
