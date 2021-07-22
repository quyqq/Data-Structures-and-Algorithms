package OtherMethods;

import DataModel.*;
import DataModel.EnumDatabase.AGE;
import DataModel.EnumDatabase.GENDER;
import DataModel.EnumDatabase.INCOME_SOURCE;
import DataModel.EnumDatabase.WEEKLY_INCOME;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author Group work (Miklos Lissa (12123973) - Quyet Quang Quuy (12118217))
 * Date: 25/05/2020
 * Version: 1.0
 * Class:DataFile
 * This class for loading file and processing input data
 * */
public class DataFile {

    //Show error dialog
    /**
     * this method is to show an alert dialog
     * @param details the detail of the msg
     * @param textMsg text of msg
     * @param tile the title of window dialog
     * @param types type of alert window dialong
     * */
    private static void showDialog(String tile, String textMsg, String details, Alert.AlertType types)
    {
        Alert alert = new Alert(types);
        alert.setTitle(tile);
        alert.initStyle(StageStyle.UTILITY);// style dialog no minimum and maximum button on header bar
        alert.setHeaderText(textMsg);
        alert.setContentText(details);
        alert.showAndWait();
    }
    /**
     * this method is show the error
     * @param ex any kind of error
     * */
    public static void showException(Throwable ex)
    {
        String details = "";
        //for (StackTraceElement elementData :ex.getStackTrace())
        //{
        //details+= String.format("%s  %s  %s %s\n",elementData.getFileName(),elementData.getClassName(),elementData.getMethodName(),elementData.getLineNumber());
        //}
        showDialog("Error",ex.getMessage(),details,Alert.AlertType.ERROR);
    }


    /**
     * this method is show file picker dialog
     * @param databaseUtility the current data object
     * @param controlIn the current controller
     * @param title the title of dialog window
     * @exception Exception
     * */
    public static void showWindowDialog( String title, Control controlIn,DatabaseUtility databaseUtility) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(//add filter
                new FileChooser.ExtensionFilter("CSV files","*.csv")
        );
        loadDataFromTextFormat(fileChooser.showOpenDialog(controlIn.getScene().getWindow()),databaseUtility);

    }


    /**
     * this method is to process data from CSV file, load to the linkedList and then insert the data to database
     * @param databaseUtility the current data object
     * @param cellsArrayIn2D the list of list string which is from the file
     * @exception Exception
     * */
    private static void processDataFromFile(List <List<String>> cellsArrayIn2D, DatabaseUtility databaseUtility) throws Exception
    {


        List <List<String>> headers=cellsArrayIn2D.subList(0,3);

        List <List<String>> locationList = cellsArrayIn2D.subList(3,cellsArrayIn2D.size()).stream().map(strings -> strings.subList(0,2)).distinct().collect(Collectors.toList());
        List<String> ageGroupList = cellsArrayIn2D.get(1).subList(3,cellsArrayIn2D.get(1).size()).stream().distinct().collect(Collectors.toList());
        List <String> weeklyIncomeList = cellsArrayIn2D.subList(3,cellsArrayIn2D.size()).stream().map(strings -> strings.get(2)).distinct().collect(Collectors.toList());
        LinkedList<DatabaseInterface> riskyPersonList = new LinkedList<>();


        riskyPersonList.addAll(locationList.stream().map(strings -> new Location(strings.get(0),strings.get(1))).collect(Collectors.toList()));
        riskyPersonList.addAll(ageGroupList.stream().map(AGE::findAgeGroup).collect(Collectors.toList()));
        riskyPersonList.addAll(weeklyIncomeList.stream().map(WEEKLY_INCOME::findWeeklyIncome).collect(Collectors.toList()));
        cellsArrayIn2D.subList(3,cellsArrayIn2D.size()).forEach(strings ->
        {
            for (int i=0;i<strings.size()-3;i++)
            {
                riskyPersonList.add(new RiskyPersons(
                        new SA3TenantCategory(
                                0,
                                new Location(strings.get(0),strings.get(1)),
                                WEEKLY_INCOME.findWeeklyIncome(strings.get(2)),
                                INCOME_SOURCE.findIncomeSource(headers.get(2).get(3+i)),
                                AGE.findAgeGroup(headers.get(1).get(3+i)),
                                GENDER.findGender(headers.get(0).get(3+i))
                        ),
                        Integer.parseInt(strings.get(3+i))
                ));
            }
        });
        databaseUtility.insertData(riskyPersonList);
        System.out.println("insert data from file to database, is successfully.");
        databaseUtility.loadDatabase();


    }

    /**
     * this method reading data from file
     * @param databaseUtility the current data object
     * @param openedFile  the opened file
     * @exception Exception
     * */
    private static void loadDataFromTextFormat(File openedFile,DatabaseUtility databaseUtility) throws Exception {
        if (openedFile != null)
            try {


                Scanner readScanner = new Scanner(openedFile);
                List<List<String>> cellsArrayIn2D = new LinkedList<>();


                if (readScanner.hasNext()) {
                    while (readScanner.hasNext()) {
                        cellsArrayIn2D.add(Arrays.stream(readScanner.nextLine().trim().split(",")).map(String::trim).collect(Collectors.toList()));// read next line

                    }
                    processDataFromFile(cellsArrayIn2D, databaseUtility);
                } else
                    throw new NoSuchElementException(openedFile.getName() + " is not correct formed or empty .");


            } catch (NoSuchElementException ex) {

                //ex.printStackTrace();
                throw new NoSuchElementException(openedFile.getName() + " is not correct formed.");
            } catch (Exception ex) {

                //ex.printStackTrace();
                throw new Exception(String.format("The system tried to read %s but it was not successfully." +
                        "\n Please check the file is correct format. \n%s", openedFile.getName(), ex.getMessage()));

            }
    }


    /**
     * this method create new window
     * @param fileLoad the opened file
     * @param tile the title of window
     * @return new stage
     * @exception Exception
     * */
    public static Stage newStage(FXMLLoader fileLoad, String tile) throws IOException {
        Stage addandUpdateLocation = new Stage();
        Parent root = fileLoad.load();
        addandUpdateLocation.setScene(new Scene(root));
        addandUpdateLocation.setTitle(tile);
        addandUpdateLocation.setResizable(false);
        addandUpdateLocation.initModality(Modality.APPLICATION_MODAL);// not showing window on taskbar
        addandUpdateLocation.initStyle(StageStyle.UTILITY);

        return addandUpdateLocation;
    }
    //
    /**
     * this method load data from FXML file
     * @param path the path of the file FXML
     * @return the opened file
     * @exception IOException
     * */
    public static FXMLLoader fileLoadFromFXML(String path) throws IOException {

        FXMLLoader fileLoad = new FXMLLoader(DataFile.class.getResource(path));
        return fileLoad;
    }
}
