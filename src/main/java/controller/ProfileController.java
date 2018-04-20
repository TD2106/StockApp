package controller;

import dao.CommentDAO;
import dao.PortfolioDAO;
import dao.UserDAO;
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
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
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

    private UserStock currentStock = null;
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
        userNameCommentColumn.setCellValueFactory(rowData -> {
            try {
                return new SimpleStringProperty(UserDAO.getUserName(rowData.getValue().getUserID()));
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("");
            }
        });
        timeCommentColumn.setCellValueFactory(rowData -> new SimpleStringProperty(rowData.getValue().getDateTime()));
        contentCommentColumn.setCellValueFactory(rowData -> new SimpleStringProperty(rowData.getValue().getContent()));
        portfolioTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentStock = newValue;
                stockQuantity.setText(Integer.toString(currentStock.getQuantity()));
            }
        });
        totalValueLabel.textProperty().bind(new SimpleDoubleProperty(portfolio.totalWorth()).asString());
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    @FXML
    void deleteStock(ActionEvent event) {
        if (currentStock == null) {
            showError("You need to select stock to delete");
            return;
        } else {
            portfolio.deleteStock(currentStock.getStockName());
            try {
                PortfolioDAO.deleteStock(user.getUserID(), currentStock.getStockName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            currentStock = null;
            stockQuantity.setText("");
            resetPortfolioTable();
        }
    }

    @FXML
    void addStock(ActionEvent event) {
        Stock stock = null;
        if (portfolio.isStockExist(stockNameTextField.getText().toUpperCase())) {
            showError("Stock already exists in portfolio");
            return;
        }
        try {
            stock = YahooFinance.get(stockNameTextField.getText().toUpperCase());
        } catch (IOException e) {
            e.printStackTrace();
            showError("Something wrong happened try again");
            return;
        }
        if (stock == null) {
            showError("Invalid stock name");
            return;
        }
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityTextField.getText());
        } catch (NumberFormatException e) {
            showError("Wrong format for input");
            return;
        }
        if (quantity <= 0) {
            showError("Input needs to be greater than 0");
            return;
        }
        try {
            PortfolioDAO.addNewStock(user.getUserID(), stockNameTextField.getText().toUpperCase(), quantity);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Something wrong happen try again");
            return;
        }
        portfolio.addStock(new UserStock(stockNameTextField.getText().toUpperCase(), quantity));
        resetPortfolioTable();
        showInformation("Success");
    }

    @FXML
    void postComment(ActionEvent event) {
        if (commentInput.getText().equals("") || commentInput.getText() == null) {
            showError("You need to input something");
            return;
        }
        try {
            CommentDAO.addComment(commentInput.getText(), user.getUserID());
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Something wrong happened");
            return;
        }
        resetCommentTable();
    }

    @FXML
    void predict(ActionEvent event) {

    }

    @FXML
    void refreshComment(ActionEvent event) {
        resetCommentTable();
    }

    @FXML
    void updateStock(ActionEvent event) {
        if (currentStock == null || stockQuantity.getText() == null || stockQuantity.getText().equals("")) {
            showError("You need to select stock or input quantity");
            return;
        }
        int newQuantity = 0;
        try {
            newQuantity = Integer.parseInt(stockQuantity.getText());
        } catch (NumberFormatException e) {
            showError("Wrong format for input");
            return;
        }
        if (newQuantity <= 0) {
            showError("Input needs to be greater than 0");
            return;
        }
        try {
            PortfolioDAO.editStock(user.getUserID(), currentStock.getStockName(), newQuantity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        portfolio.editStock(currentStock.getStockName(), newQuantity);
        resetPortfolioTable();
        showInformation("Success");
    }

    public void resetPortfolioTable() {
        stocks.clear();
        stocks.addAll(portfolio.getStocks());
        totalValueLabel.textProperty().bind(new SimpleDoubleProperty(portfolio.totalWorth()).asString());
    }

    public void showError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR, s);
        alert.show();
    }

    public void resetCommentTable() {
        comments.clear();
        try {
            comments.addAll(CommentDAO.getAllComments());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showInformation(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
        alert.show();
    }
}
