package GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
/**
 *An auxiliary department to create a table of all the queries
 */
public class Item {
    private SimpleStringProperty queryTitle ;
    private Button button;
    private ArrayList<String> allDocForEachQ;
    private String path;

    public Item(String title,ArrayList<String> allDocForEachQ, String path ){
        queryTitle = new SimpleStringProperty(title);
        button = new Button("Show relevant documents");

        button.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                buttonAct();
            }
        });
        if(allDocForEachQ!=null && !allDocForEachQ.isEmpty()){
            this.allDocForEachQ = allDocForEachQ;
        }
        this.path = path;
       // identifyEntityInDocument.getAllEntities();
    }

    public String getQueryTitle() {
        return queryTitle.get();
    }

    public SimpleStringProperty queryTitleProperty() {
        return queryTitle;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    /**
     * When you press the button to see all the relevant documents, this function will be activated
     */
    private void buttonAct(){
        if(allDocForEachQ!=null &&allDocForEachQ.size()>0) {
            Stage stageAllQueries = new Stage();
            stageAllQueries.setTitle("All Document:");
            TableView tableView = new TableView();
            TableColumn<String, Item> column1 = new TableColumn<>("Document ID");
            column1.setCellValueFactory(new PropertyValueFactory<>("docTitle"));
            TableColumn<String, Item> column2 = new TableColumn<>("5 common entities");
            column2.setCellValueFactory(new PropertyValueFactory<>("button"));
            tableView.getColumns().add(column1);
            tableView.getColumns().add(column2);
            for (String docId : allDocForEachQ) {
                ItemDoc itemDoc = new ItemDoc(docId, this.path);
                tableView.getItems().add(itemDoc);
            }
            VBox vbox = new VBox(tableView);

            Scene scene = new Scene(vbox);

            stageAllQueries.setScene(scene);

            stageAllQueries.show();
        }
        else{
            showAlert("No documents found");

        }
    }


    /**
     * @param title - The title of the error message
     */
    private void showAlert(String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle(title);
        alert.setContentText("Sorry, no documents found");
        alert.show();
    }



}
