package com.insitute.iimanage.controller;

import com.insitute.iimanage.db.DBConnection;
import com.insitute.iimanage.db.Database;
import com.insitute.iimanage.model.Student;
import com.insitute.iimanage.model.Teacher;
import com.insitute.iimanage.model.Tm.StudentTm;
import com.insitute.iimanage.model.Tm.TeacherTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TeacherFormController {
    public AnchorPane context;
    public TextField textTeacherID;
    public TextField txtFullName;
    public TextField txtAddress;
    public Button btnSaveTeacher;
    public TextField txtSearch;
    public TableView<TeacherTm> tblTeacher;
    public TableColumn<TeacherTm,String> colTeacherId;
    public TableColumn<TeacherTm,String> colFullName;
    public TableColumn<TeacherTm,String> colAddress;
    public TableColumn<TeacherTm,String> colContact;
    public TableColumn<TeacherTm,Button> colOption;
    public TextField txtContact;

    private String searchText="";

    public void initialize() {

        colTeacherId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("button"));

        generateTeacherID();
        setTableData(searchText);

        tblTeacher.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setTableDataValue(newValue);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            setTableData(searchText);
        });
    }

    public void newTeacherOnAction(ActionEvent actionEvent) {
        generateTeacherID();
        setTableData(searchText);
        clear();
        btnSaveTeacher.setText("Save Teacher");
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("Dashboard");
    }

    public void saveTeacherOnAction(ActionEvent actionEvent) {

        if (btnSaveTeacher.getText().equalsIgnoreCase("Save Teacher")) {
            Teacher teacher = new Teacher(
                    textTeacherID.getText(),
                    txtFullName.getText(),
                    txtAddress.getText(),
                    txtContact.getText()
            );

            //connect mysql database
            try {
                if (saveTeacher(teacher)) {
                    generateTeacherID();
                    clear();
                    setTableData(searchText);
                    new Alert(Alert.AlertType.INFORMATION, "Teacher has been Saved...!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong...!").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            Database.teacherTable.add(teacher);

            System.out.println(teacher.toString());
        } else {

            //connect with mysql database
            Teacher teacher = new Teacher();
            teacher.setTeacherId(textTeacherID.getText());
            teacher.setName(txtFullName.getText());
            teacher.setAddress(txtAddress.getText());
            teacher.setContact(txtContact.getText());;
            try {
                if(updateTeacher(teacher)){
                    setTableData(searchText);
                    clear();
                    generateTeacherID();
                    new Alert(Alert.AlertType.INFORMATION, "Teacher has been updated...!").show();
                    btnSaveTeacher.setText("Save Teacher");
                    return;
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "Something went wrong...!").show();
                }
            }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }

        }

    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
        stage.show();
        stage.centerOnScreen();
    }

    private void generateTeacherID() {

        /*if (!Database.teacherTable.isEmpty()) {

            Teacher lastTeacher = Database.teacherTable.get(Database.teacherTable.size() - 1);
            String stringId = lastTeacher.getTeacherId();
            String[] split = stringId.split("-");
            String lastIdAsString = split[1];
            int lastIdAsInteger = Integer.parseInt(lastIdAsString);
            lastIdAsInteger++;
            String newId = "T-" + lastIdAsInteger;
            textTeacherID.setText(newId);

        } else {
            textTeacherID.setText("T-1");
        }*/

        try {
            String stringId = getLastId();
            if (stringId != null) {
                String[] split = stringId.split("-");
                String lastIdAsString = split[1];
                int lastIdAsInteger = Integer.parseInt(lastIdAsString);
                lastIdAsInteger++;
                String newId = "T-" + lastIdAsInteger;
                textTeacherID.setText(newId);
            } else {
                textTeacherID.setText("T-1");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
    private void clear(){
        txtContact.clear();
        txtAddress.clear();
        txtFullName.clear();
    }

    private void setTableData(String name) {

        ObservableList<TeacherTm> oblist = FXCollections.observableArrayList();

        //connect with mysql database;

        try {
            List<Teacher> teacherList = searchTeacher(name);

            for (Teacher teacher:teacherList) {
                Button button = new Button("Delete");

                oblist.add(new TeacherTm(
                        teacher.getTeacherId(),
                        teacher.getName(),
                        teacher.getAddress(),
                        teacher.getContact(),
                        button
                ));

                button.setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you Sure...?", ButtonType.NO, ButtonType.YES);
                    Optional<ButtonType> buttonType = alert.showAndWait();

                    if (buttonType.get().equals(ButtonType.YES)) {
                        try{
                            if(deleteTeacher(teacher.getTeacherId())){
                                new Alert(Alert.AlertType.INFORMATION, "Teacher has Been Deleted...!");
                                setTableData(searchText);
                            }else {
                                new Alert(Alert.AlertType.INFORMATION, "Something went wrong...!");
                            }
                        }catch (ClassNotFoundException | SQLException e){
                            e.printStackTrace();
                        }

                    }

                });
            }



        }catch (ClassNotFoundException | SQLException  e){
            e.printStackTrace();
        }
        tblTeacher.setItems(oblist);

    }

    private void setTableDataValue(TeacherTm teacherTm) {
        textTeacherID.setText(teacherTm.getId());
        txtFullName.setText(teacherTm.getName());
        txtAddress.setText(teacherTm.getAddress());
        txtContact.setText(teacherTm.getContact());
        btnSaveTeacher.setText("Update Teacher");
    }

    private boolean saveTeacher(Teacher teacher) throws ClassNotFoundException, SQLException {

       /* Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/iitmanage", "root", "1234");*/
        Connection connection = DBConnection.getInstance().getConnection();

        String sql = "INSERT INTO teacher VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, teacher.getTeacherId());
        preparedStatement.setString(2, teacher.getName());
        preparedStatement.setObject(3, teacher.getAddress());
        preparedStatement.setString(4, teacher.getContact());

        return preparedStatement.executeUpdate() > 0;

    }

    private String getLastId() throws ClassNotFoundException, SQLException {
        /*Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/iitmanage", "root", "1234");*/
        Connection connection = DBConnection.getInstance().getConnection();

        String sql = "SELECT teacher_id FROM teacher  ORDER BY " +
                "CAST(SUBSTRING(teacher_id,3) AS UNSIGNED ) DESC LIMIT 1";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }

        return null;
    }

    private List<Teacher> searchTeacher(String text) throws ClassNotFoundException, SQLException {

        text = "%" + text + "%";
        /*Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/iitmanage", "root", "1234");*/
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM teacher WHERE name LIKE ? OR address LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, text);
        preparedStatement.setString(2, text);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Teacher> list = new ArrayList<>();

        while (resultSet.next()) {

            list.add(new Teacher(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }

        return list;
    }

    private boolean deleteTeacher(String id) throws ClassNotFoundException, SQLException {
        /*Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/iitmanage", "root", "1234");*/
        Connection connection = DBConnection.getInstance().getConnection();
        String sql ="DELETE FROM teacher WHERE teacher_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,id);
        return preparedStatement.executeUpdate()>0;
    }

    private boolean updateTeacher(Teacher teacher) throws ClassNotFoundException, SQLException {
        /*Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/iitmanage", "root", "1234");*/
        Connection connection = DBConnection.getInstance().getConnection();

        String sql = "UPDATE teacher SET name=?,address=?,contact=? WHERE teacher_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,teacher.getName());
        preparedStatement.setObject(2,teacher.getAddress());
        preparedStatement.setString(3,teacher.getContact());
        preparedStatement.setString(4,teacher.getTeacherId());
        return preparedStatement.executeUpdate()>0;
    }

}
