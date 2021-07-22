package Controllers;


import DataModel.*;
import DataModel.EnumDatabase.AGE;
import DataModel.EnumDatabase.GENDER;
import DataModel.EnumDatabase.INCOME_SOURCE;
import DataModel.EnumDatabase.WEEKLY_INCOME;
import OtherMethods.DataFile;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 25/05/2020
 * Version: 1.0
 * Controller: AddAndUpdateTenantController
 * It for handle actions from user
 * */
public class AddAndUpdateTenantController implements Initializable {

    private DatabaseUtility databaseUtility;

    @FXML
    private Label labelCurrentNumber;

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

    @FXML
    private Button buttonCancel;

    public void setDatabaseUtility(DatabaseUtility databaseUtility) {
        this.databaseUtility = databaseUtility;
    }

    @FXML
    void buttonCancelAction(ActionEvent event) {
        ((Stage)buttonCancel.getScene().getWindow()).close();
    }//close the stage

    @FXML
    void categorySelectionAction(ActionEvent event)// showing the current number of tenant if all combobox it selected
    {

        labelCurrentNumber.setVisible(true);
        if (
                combIncomeSource.getSelectionModel().getSelectedIndex() >= 0 &&
                        combIncomeCategory.getSelectionModel().getSelectedIndex() >= 0 &&
                        combGender.getSelectionModel().getSelectedIndex() >= 0 &&
                        combAge.getSelectionModel().getSelectedIndex() >= 0 &&
                        combLocation.getSelectionModel().getSelectedIndex() >= 0
        ) {
            labelCurrentNumber.setVisible(true);

            try {
                //System.out.println(my);
                List<RiskyPersons> existingRiskyPerson = DatabaseUtility.findRiskyPerson(new RiskyPersons(
                        new SA3TenantCategory(
                                0,
                                combLocation.getSelectionModel().getSelectedItem(),
                                combIncomeCategory.getSelectionModel().getSelectedItem(),
                                combIncomeSource.getSelectionModel().getSelectedItem(),
                                combAge.getSelectionModel().getSelectedItem(),
                                combGender.getSelectionModel().getSelectedItem()
                        ),
                       0
                ));
                // if it is existed so can show the current number if it is not so it is adding new record
                if (existingRiskyPerson.size() > 0)
                {
                    labelCurrentNumber.setText("Current number: " +
                                                existingRiskyPerson.get(0).getNumberPeople());
                } else
                    labelCurrentNumber.setText("Adding new record.");
            } catch (Exception ex) {
                DataFile.showException(ex);
            }

        } else {
            labelCurrentNumber.setVisible(false);
        }
    }



    @FXML
    void buttonSaveAction(ActionEvent event) {
        try {
            //checking all data is entered or not
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
            if (errorMgs.length() > 0) {
                DataFile.showException(new NullPointerException("Please select :\n" + errorMgs));
                return;// stop here
            }

            RiskyPersons tmp = new RiskyPersons(
                    new SA3TenantCategory(
                            0,
                            combLocation.getSelectionModel().getSelectedItem(),
                            combIncomeCategory.getSelectionModel().getSelectedItem(),
                            combIncomeSource.getSelectionModel().getSelectedItem(),
                            combAge.getSelectionModel().getSelectedItem(),
                            combGender.getSelectionModel().getSelectedItem()
                    ),
                    Integer.parseInt(textNumberPeople.getText().trim())
            );
            List<RiskyPersons> existingRiskyPerson = DatabaseUtility.findRiskyPerson(tmp);
            // if it is existed so can show the current number if it is not so it is adding new record
            if (existingRiskyPerson.size() > 0)
            {
                databaseUtility.addOrIncreaseRiskyPerson(existingRiskyPerson.get(0),
                                                        Integer.parseInt(textNumberPeople.getText().trim()));
                System.out.println("Update data is successfully.");
            } else
            {
                LinkedList<DatabaseInterface> dataInsert = new LinkedList<>();
                dataInsert.add(tmp);
                databaseUtility.insertData(dataInsert);
                System.out.println("Insert data is successfully.");
            }




            buttonCancelAction(event);
        } catch (Exception ex)
        {
            DataFile.showException(ex);
        }

    }

// load data to the combobox
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        combLocation.setItems(FXCollections.observableArrayList(DatabaseUtility.getLocations()));
        combAge.setItems(FXCollections.observableArrayList(AGE.values()));
        combGender.setItems(FXCollections.observableArrayList(GENDER.values()));
        combIncomeSource.setItems(FXCollections.observableArrayList(INCOME_SOURCE.values()));
        combIncomeCategory.setItems(FXCollections.observableArrayList(WEEKLY_INCOME.values()));
    }
}
