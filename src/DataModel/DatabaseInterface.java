
package DataModel;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 *This class is the interface which will be implemented AGE, Location, RiskyPersons and WEEKLY_INCOME class (enum)
 * because it will force to implement the method, that will help a lot to reduce coding in the future.
 * when new objects are created, it can call the method selfInsert to insert them in the database.
 *
 *
 *
 * */
@FunctionalInterface
public interface DatabaseInterface {

     <T extends Connection> void selfInsert(T connection , String tableName) throws SQLException;

}
