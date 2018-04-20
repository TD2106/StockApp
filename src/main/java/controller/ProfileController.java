package controller;

import dao.CommentDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Comment;
import model.Portfolio;
import model.User;
import model.UserStock;

import java.sql.SQLException;

public class ProfileController {

    @FXML
    private TableView<UserStock> portfolioTable;

    @FXML
    private TableColumn<UserStock, String> stockNameColumn;

    @FXML
    private TableColumn<UserStock, Double> priceColumn;

    @FXML
    private TableColumn<UserStock, Integer> quantityColumn;

    @FXML
    private TableColumn<UserStock, Double> valueColumn;

    @FXML
    private Label totalValueLabel;

    @FXML
    private TextField stockQuantity;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField stockToPredict;

    @FXML
    private Button predictButton;

    @FXML
    private TableView<Comment> commentTable;

    @FXML
    private TableColumn<Comment, String> userNameCommentColumn;

    @FXML
    private TableColumn<Comment, String> timeCommentColumn;

    @FXML
    private TableColumn<Comment, String> contentCommentColumn;

    @FXML
    private TextArea commentInput;

    @FXML
    private Button postButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField stockNameTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private Button addStockButton;

    private User user;

    private Portfolio portfolio;

    private ObservableList<UserStock> stocks = FXCollections.observableArrayList();

    private ObservableList<Comment> comments = FXCollections.observableArrayList();

    public void setUp() {
        stocks.addAll(portfolio.getStocks());
        try {
            comments.addAll(CommentDAO.getAllComments());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        portfolioTable.setItems(stocks);
        commentTable.setItems(comments);
        stockNameColumn.setCellValueFactory(rowData -> new SimpleStringProperty(rowData.getValue().getStockName()));
        priceColumn.setCellValueFactory(rowData -> new SimpleDoubleProperty(rowData.getValue().getPrice()).asObject());
        quantityColumn.setCellValueFactory(rowData -> new SimpleIntegerProperty(rowData.getValue().getQuantity()).asObject());
        valueColumn.setCellValueFactory(rowData -> new SimpleDoubleProperty(rowData.getValue().getTotalValue()).asObject());

    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    @FXML
    void deleteStock(ActionEvent event) {

    }

    @FXML
    void login(ActionEvent event) {

    }

    @FXML
    void postComment(ActionEvent event) {

    }

    @FXML
    void predict(ActionEvent event) {

    }

    @FXML
    void refreshComment(ActionEvent event) {

    }

    @FXML
    void updateStock(ActionEvent event) {

    }


}
