

package Controllers;

import DataModel.*;
import OtherMethods.DataFile;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 15/04/2020
 * Version: 1.0
 * Controller: HomelessInfoController
 * It for handle actions from user
 * */
public class HomelessInfoController implements Initializable{
    @FXML
    private SplitPane mainSpliter;

    @FXML
    private Button buttonOpenFile;

    @FXML
    private Button buttonLocation;

    @FXML
    private Button buttonTenant;

    @FXML
    private Button buttonReport;

    @FXML
    private TextField textKeyWord;

    @FXML
    private Button buttonSearch;

    @FXML
    private TableView<RiskyPersons> tableViewListSearched;

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

    private DatabaseUtility databaseUtility;
/**
 * search and show data from database on the table view
 * */
    @FXML
    void buttonSearchAction(ActionEvent event) {


        if(textKeyWord.getText().trim().length()>0) {

            tableViewListSearched.setItems(FXCollections.observableArrayList(
                                            DatabaseUtility.getRiskyPersons().parallelStream()
                    .filter(f -> {
                        if (String.valueOf(f.getNumberPeople()).toUpperCase().
                                            contains(textKeyWord.getText().trim().toUpperCase()) ||
                                f.getSA3Category().getLocation().getSA3code().toUpperCase().
                                            contains(textKeyWord.getText().trim().toUpperCase()) ||
                                f.getSA3Category().getLocation().getName().toUpperCase().
                                            contains(textKeyWord.getText().trim().toUpperCase()) ||
                                f.getSA3Category().getGenderCategory().getName().toUpperCase().
                                            contains(textKeyWord.getText().trim().toUpperCase()) ||
                                f.getSA3Category().getAgeCategory().getName().toUpperCase().
                                            contains(textKeyWord.getText().trim().toUpperCase()) ||
                                f.getSA3Category().getIncomeSource().getName().toUpperCase().
                                            contains(textKeyWord.getText().trim().toUpperCase()) ||
                                f.getSA3Category().getIncomeCategory().getName().toUpperCase().
                                            contains(textKeyWord.getText().trim().toUpperCase())

                                    )
                                        return true;
                                    else
                                        return false;
                                }
                            ).collect(Collectors.toList())));
        }
        else
            tableViewListSearched.getItems().clear();
    }
    /**
     * set current database from main method
     * @param databaseUtility current database object
     * */
    public void setDatabaseUtility(DatabaseUtility databaseUtility) {
        this.databaseUtility = databaseUtility;
    }
    /**
     * search and show data from database on the table view
     * */
    @FXML
    public void textKeywordTyped(KeyEvent event) {
        buttonSearchAction(null);
    }

    /**
     * open file
     * */
    @FXML
    void buttonOpenFileAction(ActionEvent event)  {
        try {
            DataFile.showWindowDialog("Open file",buttonOpenFile,databaseUtility);
        }catch (Exception ex)
        {
           // ex.printStackTrace();
            //System.err.println(ex.getMessage());
            DataFile.showException(ex);
        }



    }
//save data to file
    @FXML
    void setButtonSaveFileAction(ActionEvent event){

    }
    //edit selected row
    @FXML
    void buttonEdtAction(ActionEvent event) throws IOException {



    }

//showing dialog of add new location
    @FXML
    void buttonLocationAction(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = DataFile.fileLoadFromFXML("/UserInterface/AddLocation.fxml");
        Stage stage= DataFile.newStage(fxmlLoader,"Location");
        ((AddLocationController)fxmlLoader.getController()).setDatabaseUtility(databaseUtility);
        stage.showAndWait();

    }
//show dialog of add new tanent of edit
    @FXML
    void buttonTenantAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = DataFile.fileLoadFromFXML("/UserInterface/AddAndUpdateTenant.fxml");
        Stage stage= DataFile.newStage(fxmlLoader,"Tenant");
        ((AddAndUpdateTenantController)fxmlLoader.getController()).setDatabaseUtility(databaseUtility);
        stage.showAndWait();
    }
//show dialog of report
    @FXML
    void buttonReportAction(ActionEvent event) throws IOException {

        DataFile.newStage(DataFile.fileLoadFromFXML("/UserInterface/Reprots.fxml"),"Report").showAndWait();
    }
//delete a data
    @FXML
    void buttonDeleteAction(ActionEvent event)
    {

    }
//create an event to check the row is selected or not
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
