
package Controllers;

import DataModel.DatabaseUtility;
import DataModel.EnumDatabase.GENDER;
import DataModel.Location;
import DataModel.RiskyPersons;
import OtherMethods.DataFile;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 15/04/2020
 * Version: 1.0
 * Controller: ReprotsController
 * It for handle actions from user
 * */

public class ReprotsController implements Initializable {
    @FXML
    private ComboBox<Location> combLocation;

    @FXML
    private CheckBox checkBoxLocation;

    @FXML
    private CheckBox checkBoxGender;

    @FXML
    private ComboBox<GENDER> combGender;

    @FXML
    private Button buttonReport;

    @FXML
    private TableView<RiskyPersons> tableReport;

    @FXML
    private TableColumn<RiskyPersons, String> cloumnPostCode;

    @FXML
    private TableColumn<RiskyPersons, String> columnLocationName;

    @FXML
    private TableColumn<RiskyPersons, String> columnIncomeSource;

    @FXML
    private TableColumn<RiskyPersons, String> columnWeeklyIncoe;

    @FXML
    private TableColumn<RiskyPersons, String> columnAgeCategory;

    @FXML
    private TableColumn<RiskyPersons, String> columnGender;

    @FXML
    private TableColumn<RiskyPersons, Integer> columnNumberOfPeople;

    @FXML
    private TextField textTotalLocation;

    @FXML
    private TextField textTotalPeople;

    /**
     * this method handling report from user
     * */
    @FXML
    void buttonReportAction(ActionEvent event) {

        List<RiskyPersons> reportList =  new LinkedList<>();
        long totalLocation = 0;// variable to store totals
        long totalPeople = 0;

        String megs = "";

        if (combLocation.getSelectionModel().getSelectedItem() == null &&
                checkBoxLocation.isSelected())
            megs += "Please select Location.\n";

        if (combGender.getSelectionModel().getSelectedItem() == null &&
                checkBoxGender.isSelected())
            megs += "Please select Gender.";

        if (megs.length() > 0) {// check megs length if is >0  that mean it has error
            DataFile.showException(new Exception(megs));
            tableReport.getItems().clear();
        } else {

            reportList= (LinkedList) DatabaseUtility.getRiskyPersons().clone();
            if (checkBoxLocation.isSelected() && checkBoxGender.isSelected()) {// if the report for a specific gender and location


                reportList = reportList.stream().filter(persons -> {
                                                        if(persons.getSA3Category().getLocation().compareLocation(
                                                                combLocation.getSelectionModel().getSelectedItem()
                                                        )&&
                                                            persons.getSA3Category().getGenderCategory().equals(
                                                                    combGender.getSelectionModel().getSelectedItem())
                                                        )
                                                            return true;
                                                        else
                                                            return false;
                                                        })
                                                        .sorted((persons, t1) ->
                                                                t1.getNumberPeople()-persons.getNumberPeople())
                                                        .collect(Collectors.toList());

                //System.out.println("G L");

            } else if (checkBoxGender.isSelected() || checkBoxLocation.isSelected()) {
                if (checkBoxLocation.isSelected()) {// for a specific location

                    reportList =  reportList.stream().filter(persons ->
                                                            persons.getSA3Category().
                                                            getLocation().compareLocation(
                                                                    combLocation.getSelectionModel().getSelectedItem()))
                            .sorted((persons, t1) -> t1.getNumberPeople()-persons.getNumberPeople())
                            .collect(Collectors.toList());
                    //System.out.println("L");
                } else {//for a gender
                    reportList = reportList.stream().filter(
                                                            persons -> persons.getSA3Category().getGenderCategory().equals(
                                                                    combGender.getSelectionModel().getSelectedItem())
                                                            ).sorted((persons, t1) ->t1.getNumberPeople()-persons.getNumberPeople())
                                                            .collect(Collectors.toList());
                    //System.out.println("G");
                }
            } else {// for all location

                reportList= reportList.stream().sorted((persons, t1) -> t1.getNumberPeople()-persons.getNumberPeople()).collect(Collectors.toList());
                //System.out.println("ALL");

            }


        }
        totalLocation = reportList.stream().map(persons -> persons.getSA3Category().getLocation().
                                                getSA3code()).distinct().count();
        totalPeople = reportList.stream().mapToLong(RiskyPersons::getNumberPeople).sum();
        tableReport.setItems(FXCollections.observableArrayList(reportList));
        textTotalPeople.setText("Total homelessness: " + totalPeople);
        textTotalLocation.setText("Total Locations: " + totalLocation);
    }



    /**
     * this method is enable or disable combobox gender
     * */
    @FXML
    void checkBoxGenderAction(ActionEvent event) {
        combGender.setDisable(!checkBoxGender.isSelected());

    }
    /**
     * this method is enable or disable combobox location
     * */
    @FXML
    void checkBoxLocationAction(ActionEvent event) {

        combLocation.setDisable(!checkBoxLocation.isSelected());

    }
    /**
     * this method is initialize and setting up
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        combLocation.setItems(FXCollections.observableArrayList(DatabaseUtility.getLocations()));
        combGender.setItems(FXCollections.observableArrayList(GENDER.values()));
        cloumnPostCode.setCellValueFactory(item->new ReadOnlyObjectWrapper<String>
                                            (item.getValue().getSA3Category().getLocation().getSA3code()));
        columnLocationName.setCellValueFactory(item->new ReadOnlyObjectWrapper<String>
                                            (item.getValue().getSA3Category().getLocation().getName()));
        columnIncomeSource.setCellValueFactory(item->new ReadOnlyObjectWrapper<String>
                                            (item.getValue().getSA3Category().getIncomeSource().getName()));
        columnWeeklyIncoe.setCellValueFactory(item->new ReadOnlyObjectWrapper<String>
                                            (item.getValue().getSA3Category().getIncomeCategory().getName()));
        columnAgeCategory.setCellValueFactory(item->new ReadOnlyObjectWrapper<String>
                                            (item.getValue().getSA3Category().getAgeCategory().getName()));
        columnGender.setCellValueFactory(item->new ReadOnlyObjectWrapper<String>
                                            (item.getValue().getSA3Category().getGenderCategory().getName()));
        columnNumberOfPeople.setCellValueFactory(item->new ReadOnlyObjectWrapper<Integer>
                                            (item.getValue().getNumberPeople()));


    }
}
