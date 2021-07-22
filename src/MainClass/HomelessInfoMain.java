

package MainClass;

import Controllers.HomelessInfoController;
import DataModel.*;
import OtherMethods.DataFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.LinkedList;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 25/05/2020
 * Version: 1.0
 * Class:HomelessInfoMain
 * This class for start up the system
 * */
public class HomelessInfoMain extends Application {
    public static DatabaseUtility databaseUtility ;
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param stage
     * */
    @Override
    public void start(Stage stage) {


        try {

            databaseUtility = new DatabaseUtility();
            LinkedList<DatabaseInterface> myCoonectionOfData = new LinkedList<>();

            FXMLLoader fileLoad;
            fileLoad = new FXMLLoader(getClass().getResource("/UserInterface/HomelessInfo.fxml"));
            Parent root = fileLoad.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Assessment 2 Homeless Information System");
            //stage.setResizable(false);
            ((HomelessInfoController)fileLoad.getController()).setDatabaseUtility(databaseUtility);
            stage.setOnCloseRequest(x -> {
                try {
                    databaseUtility.closeConnection();
                } catch (SQLException ex) {
                    DataFile.showException(ex);
                    //ex.printStackTrace();
                }
            });
            stage.setMaximized(true);
            stage.show();


        } catch (Exception ex) {
            DataFile.showException(ex);
            //System.err.println(ex.getMessage());
            //ex.printStackTrace();
        }


    }


}
