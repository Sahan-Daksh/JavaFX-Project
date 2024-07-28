//package com.insitute.iimanage.controller;
//
//import com.insitute.iimanage.db.DBConnection;
//import com.insitute.iimanage.db.Database;
//import com.insitute.iimanage.model.Intake;
//import com.insitute.iimanage.model.Tm.IntakeTm;
//import com.insitute.iimanage.model.Tm.StudentTm;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//public class IntakeFormController {
//    public AnchorPane context;
//    public TextField txtIntakeID;
//    public TextField txtName;
//    public ComboBox cmbCourse;
//    public DatePicker txtDate;
//    public Button btnSave;
//    public TextField txtSearch;
//
//    public TableView<IntakeTm> tblIntake;
//    public TableColumn<IntakeTm, String> colId;
//    public TableColumn<IntakeTm, String>  colName;
//    public TableColumn<IntakeTm, String>  colDate;
//    public TableColumn<IntakeTm, String>  colCourse;
//    public TableColumn<IntakeTm, String>  colStatus;
//    public TableColumn<IntakeTm, Button>  colOption;
//
//    String searchText = "";
//
//    public void initialize() {
//
//        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
//        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
//        colDate.setCellValueFactory(new PropertyValueFactory<>("dob"));
//        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
//        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
//        colOption.setCellValueFactory(new PropertyValueFactory<>("button"));
//
//        genarateIntakeID();
//        setTableData(searchText);
//
//        tblIntake.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (null != newValue) {
//                setTableDataValue(newValue);
//            }
//        });
//
//        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
//            searchText = newValue;
//            setTableData(searchText);
//        });
//
//    }
//
//
//    public void newIntakeOnAction(ActionEvent actionEvent) {
//
//        genarateIntakeID();
//        setTableData(searchText);
//        clear();
//        btnSave.setText("Save Intake");
//
//    }
//
//    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
//        setUI("Dashboard");
//    }
//
//    public void saveIntakeOnAction(ActionEvent actionEvent) {
//
//        if (btnSave.getText().equalsIgnoreCase("Save Intake")) {
//            Intake intake = new Intake(
//                    txtIntakeID.getText(),
//                    txtName.getText(),
//                    Date.from(txtDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
//                    cmbCourse.getValue(),
//
//            );
//
//            //connect mysql database
//            try {
//                if (saveIntake(intake)) {
//                    genarateIntakeID();
//                    clear();
//                    setTableData(searchText);
//                    new Alert(Alert.AlertType.INFORMATION, "Intake has been Saved...!").show();
//                } else {
//                    new Alert(Alert.AlertType.ERROR, "Something went wrong...!").show();
//                }
//            } catch (ClassNotFoundException | SQLException e) {
//                e.printStackTrace();
//            }
//
//            Database.intakeTable.add(intake);
//
//            System.out.println(intake.toString());
//        } else {
//
//            //connect with mysql database
//            Intake intake = new Intake();
//            intake.setId(txtIntakeID.getText());
//            intake.setCourse((String) cmbCourse.getValue());
//            intake.setName(txtName.getText());
//            intake.setStatus();
//            intake.setDate(Date.from(txtDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//            try {
//                if(updateIntake(intake)){
//                    setTableData(searchText);
//                    clear();
//                    genarateIntakeID();
//                    new Alert(Alert.AlertType.INFORMATION, "Intake has been updated...!").show();
//                    btnSave.setText("Save Intake");
//                    return;
//                }else {
//                    new Alert(Alert.AlertType.INFORMATION, "Something went wrong...!").show();
//                }
//            }catch (ClassNotFoundException | SQLException e){
//                e.printStackTrace();
//            }
//
//           /* for (Intake intake : Database.intakeTable) {
//
//                if (intake.getId().equals(txtIntakeID.getText())) {
//                    intake.setAddress(txtAddress.getText());
//                    intake.setName(txtFullName.getText());
//                    intake.setDob(Date.from(txtDob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//
//                    setTableData(searchText);
//                    clear();
//                    genarateIntakeID();
//                    new Alert(Alert.AlertType.INFORMATION, "Intake has been updated...!").show();
//                    btnSaveStudnet.setText("Save Intake");
//                    return;
//                }
//            }*/
//
//        }
//    }
//
//    private void setUI(String location) throws IOException {
//        Stage stage = (Stage) context.getScene().getWindow();
//        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
//        stage.show();
//        stage.centerOnScreen();
//    }
//
//    private void genarateIntakeID() {
//
//       /* if (!Database.intakeTable.isEmpty()) {
//
//            Intake lastIntake = Database.intakeTable.get(Database.intakeTable.size() - 1);
//            String stringId = lastIntake.getId();
//            String[] split = stringId.split("-");
//            String lastIdAsString = split[1];
//            int lastIdAsInteger = Integer.parseInt(lastIdAsString);
//            lastIdAsInteger++;
//            String newId = "S-" + lastIdAsInteger;
//            txtIntakeID.setText(newId);
//
//        } else {
//            txtIntakeID.setText("S-1");
//        }*/
//        try {
//            String stringId = getLastId();
//            if (stringId != null) {
//                String[] split = stringId.split("-");
//                String lastIdAsString = split[1];
//                int lastIdAsInteger = Integer.parseInt(lastIdAsString);
//                lastIdAsInteger++;
//                String newId = "I-" + lastIdAsInteger;
//                txtIntakeID.setText(newId);
//            } else {
//                txtIntakeID.setText("I-1");
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void clear() {
//        txtName.clear();
//        txtDate.setValue(null);
//        cmbCourse.setValue(null);
//    }
//
//    private void setTableData(String name) {
//
//        ObservableList<IntakeTm> oblist = FXCollections.observableArrayList();
//
//        //connect with mysql database;
//
//        try {
//            List<Intake> intakeList = searchIntake(name);
//
//            for (Intake intake:intakeList) {
//                Button button = new Button("Delete");
//
//                oblist.add(new IntakeTm(
//                        intake.getId(),
//                        intake.getName(),
//                        new SimpleDateFormat("yyyy-MM-dd").format(intake.getDate()),
//                        intake.getCourse(),
//                        button
//                ));
//
//                button.setOnAction(event -> {
//
//                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you Sure...?", ButtonType.NO, ButtonType.YES);
//                    Optional<ButtonType> buttonType = alert.showAndWait();
//
//                    if (buttonType.get().equals(ButtonType.YES)) {
//                        // Database.intakeTable.remove(intake);
//                        try{
//                            if(deleteIntake(intake.getId())){
//                                new Alert(Alert.AlertType.INFORMATION, "Intake has Been Deleted...!");
//                                setTableData(searchText);
//                            }else {
//                                new Alert(Alert.AlertType.INFORMATION, "Something went wrong...!");
//                            }
//                        }catch (ClassNotFoundException | SQLException e){
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                });
//            }
//
//
//
//        }catch (ClassNotFoundException | SQLException  e){
//            e.printStackTrace();
//        }
//
//        tblIntake.setItems(oblist);
//
//    }
//
//    private void setTableDataValue(IntakeTm intakeTm) {
//        txtIntakeID.setText(intakeTm.getId());
//        txtName.setText(intakeTm.getName());
//        cmbCourse.setItems(intakeTm.getCourse());
//        txtDate.setValue(LocalDate.parse(intakeTm.getDob()));
//        btnSave.setText("Update Intake");
//    }
//
//    private boolean saveIntake(Intake intake) throws ClassNotFoundException, SQLException {
//
//       /* Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/iitmanage", "root", "1234");*/
//        Connection connection = DBConnection.getInstance().getConnection();
//
//        String sql = "INSERT INTO intake VALUES (?,?,?,?)";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//
//        preparedStatement.setString(1, intake.getId());
//        preparedStatement.setString(2, intake.getName());
//        preparedStatement.setObject(3, intake.getDate());
//        preparedStatement.setString(4, intake.getCourse());
//
//        return preparedStatement.executeUpdate() > 0;
//
//    }
//
//    private String getLastId() throws ClassNotFoundException, SQLException {
//        /*Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/iitmanage", "root", "1234");*/
//        Connection connection = DBConnection.getInstance().getConnection();
//
//        String sql = "SELECT intake_id FROM intake  ORDER BY " +
//                "CAST(SUBSTRING(intake_id,3) AS UNSIGNED ) DESC LIMIT 1";
//
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//        if (resultSet.next()) {
//            return resultSet.getString(1);
//        }
//
//        return null;
//    }
//
//    private List<Intake> searchIntake(String text) throws ClassNotFoundException, SQLException {
//
//        text = "%" + text + "%";
//        /*Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql://127.0.0.1:3306/iitmanage", "root", "1234");*/
//        Connection connection = DBConnection.getInstance().getConnection();
//        String sql = "SELECT * FROM intake WHERE full_name LIKE ? OR address LIKE ?";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        preparedStatement.setString(1, text);
//        preparedStatement.setString(2, text);
//
//        ResultSet resultSet = preparedStatement.executeQuery();
//        List<Intake> list = new ArrayList<>();
//
//        while (resultSet.next()) {
//
//            list.add(new Intake(
//                            resultSet.getString(1),
//                            resultSet.getString(2),
//                            resultSet.getDate(3),
//                            resultSet.getString(4)
//                    )
//            );
//        }
//
//        return list;
//    }
//
//    private boolean deleteIntake(String id) throws ClassNotFoundException, SQLException {
//        /*Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql://127.0.0.1:3306/iitmanage", "root", "1234");*/
//        Connection connection = DBConnection.getInstance().getConnection();
//        String sql ="DELETE FROM intake WHERE intake_id = ?";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        preparedStatement.setString(1,id);
//        return preparedStatement.executeUpdate()>0;
//    }
//
//    private boolean updateIntake(Intake intake) throws ClassNotFoundException, SQLException {
//        /*Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql://127.0.0.1:3306/iitmanage", "root", "1234");*/
//        Connection connection = DBConnection.getInstance().getConnection();
//
//        String sql = "UPDATE intake SET full_name=?,dob=?,address=? WHERE intake_id=?";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        preparedStatement.setString(1,intake.getName());
//        preparedStatement.setObject(2,intake.getDate());
//        preparedStatement.setString(3,intake.getCourse());
//        preparedStatement.setString(4,intake.getId());
//        return preparedStatement.executeUpdate()>0;
//    }
//
//    //////Not Completed. Have to fix signatures and stuff
//}
