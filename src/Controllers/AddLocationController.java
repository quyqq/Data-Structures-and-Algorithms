

package Controllers;

import DataModel.*;
import DataModel.EnumDatabase.AGE;
import DataModel.EnumDatabase.GENDER;
import DataModel.EnumDatabase.INCOME_SOURCE;
import DataModel.EnumDatabase.WEEKLY_INCOME;
import OtherMethods.DataFile;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 25/05/2020
 * Version: 1.0
 * Controller: AddLocationController
 * It for handle actions from user
 * */
public class AddLocationController implements Initializable {

    private DatabaseUtility databaseUtility;

    @FXML
    private TabPane tabControl;

    @FXML
    private TextField textSA3Code;

    @FXML
    private TextField textName;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buittonNExt;

    @FXML
    private TextField textNumberPeople;

    @FXML
    private ComboBox<Location> combLocation;

    @FXML
    private ComboBox<AGE> combAge;

    @FXML
    private ComboBox<GENDER> combGender;

    @FXML
    private ComboBox<INCOME_SOURCE> combIncomeSource;

    @FXML
    private ComboBox<WEEKLY_INCOME> combIncomeCategory;

    @FXML
    private Button buttonSave;


/**
 * this method is to get database
 * @param databaseUtility
 * */
    public void setDatabaseUtility(DatabaseUtility databaseUtility) {
        this.databaseUtility = databaseUtility;
    }

    /**load location to combobox
     * @param newLocation list of locations LinkedList
     * */
    private void loadCobLocations(Location newLocation)
    {
        combLocation.setItems(FXCollections.observableArrayList(DatabaseUtility.getLocations()));
        combLocation.getSelectionModel().select(newLocation);
        //System.out.println(Database.getLocations().size());
    }
    /**
     * go to next tab if it meet the requirement
     * */
    @FXML
    void buittonNextAction(ActionEvent event)  {

        try {
            LinkedList<DatabaseInterface> newLocation = new LinkedList<>();
            newLocation.add( new Location(textSA3Code.getText().trim(),textName.getText().trim()));

            databaseUtility.insertData(newLocation);
            loadCobLocations((Location) newLocation.get(0));
            tabControl.getSelectionModel().select(1);
        }catch (Exception ex)
        {
            //System.out.println(ex.getMessage());
            DataFile.showException(ex);
        }



    }


/**
 * close the current stage
 * */
    @FXML
    void buttonCancelAction(ActionEvent event) {
        ((Stage)buttonCancel.getScene().getWindow()).close();
    }
/**
 * check and save data to database and add in the linkedList
 * */
    @FXML
    void buttonSaveAction(ActionEvent event) {

        try {
            //check input data from user
            String errorMgs = "";
            if (combIncomeCategory.getSelectionModel().getSelectedItem() == null) {
                errorMgs += "Income category\n";
            }
            if (combIncomeSource.getSelectionModel().getSelectedItem() == null) {
                errorMgs += "Income source\n";
            }
            if (combGender.getSelectionModel().getSelectedItem() == null) {
                errorMgs += "Gender\n";
            }
            if (combAge.getSelectionModel().getSelectedItem() == null) {
                errorMgs += "Age category\n";
            }


            if (errorMgs.length() > 0) {// have word in errorMgs that mean have errors
                DataFile.showException(new NullPointerException("Please select :\n" + errorMgs));
            } else {


                LinkedList<DatabaseInterface> newRiskyPeron = new LinkedList<>();
                newRiskyPeron.add(
                        new RiskyPersons(
                                new SA3TenantCategory(
                                        0,
                                        combLocation.getSelectionModel().getSelectedItem(),
                                        combIncomeCategory.getSelectionModel().getSelectedItem(),
                                        combIncomeSource.getSelectionModel().getSelectedItem(),
                                        combAge.getSelectionModel().getSelectedItem(),
                                        combGender.getSelectionModel().getSelectedItem()
                                ),
                                Integer.parseInt(textNumberPeople.getText().trim())
                        ));

                databaseUtility.insertData(newRiskyPeron);
                ((Stage) buttonCancel.getScene().getWindow()).close();

            }
        } catch (NumberFormatException ex) {
            DataFile.showException(new NumberFormatException("Please enter correct format of people number."));
        } catch (Exception ex) {
            DataFile.showException(ex);

        }

    }
    /**
     * load data in the first step
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        combAge.setItems(FXCollections.observableArrayList(AGE.values()));
        combGender.setItems(FXCollections.observableArrayList(GENDER.values()));
        combIncomeSource.setItems(FXCollections.observableArrayList(INCOME_SOURCE.values()));
        combIncomeCategory.setItems(FXCollections.observableArrayList(WEEKLY_INCOME.values()));


    }
}
