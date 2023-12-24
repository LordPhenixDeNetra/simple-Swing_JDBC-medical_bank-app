package org.drame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

public class Application extends JFrame{
    private JPanel panelMain;
    private JTextField textFieldPremon;
    private JTextField textFieldNom;
    private JTextField textFieldSolde;
    private JTextField textFieldDecouvert;
    private JTextField textFieldNumeroCompte;
    private JButton buttonHistoriqueCompte;
    private JButton buttonCreerCompte;
    private JTable tableCompte;
    private JTextField textFieldNumero;
    private JTextField textFieldRetrait;
    private JTextField textFieldDepot;
    private JButton buttonDepot;
    private JButton buttonRetrait;

    private boolean isHistorique = false;

    public void initializeComponent(){
        this.setContentPane(this.panelMain);
        this.setTitle("User panel");
        this.setSize(800, 600);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void queryPrintTable(String query, Connection conn, String print) throws SQLException {

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        DefaultTableModel defaultTableModel = (DefaultTableModel) tableCompte.getModel();

        int columns = resultSetMetaData.getColumnCount();
        String [] columnName = new String[columns];

        for (int i = 0; i < columns; i++) {
            columnName[i] = resultSetMetaData.getColumnName(i + 1);
        }

        defaultTableModel.setColumnIdentifiers(columnName);

        if (print.equals("client")){
            printRowClient(resultSet, defaultTableModel);

        } else if (print.equals("historique")) {
            printRowHistorique(resultSet, defaultTableModel);
        }

        statement.close();
        conn.close();
    }
    public void printRowHistorique(ResultSet resultSet, DefaultTableModel defaultTableModel) throws SQLException {
        String typeOperation;
        int idOperation, numeroCompte;
        double montant;

        while (resultSet.next()){
            idOperation = resultSet.getInt(1);
            typeOperation = resultSet.getString(2);
            montant = resultSet.getDouble(3);
            numeroCompte = resultSet.getInt(4);

            Object [] row = {idOperation, typeOperation, montant, numeroCompte};
            defaultTableModel.addRow(row);
        }
    }
    public void printRowClient(ResultSet resultSet, DefaultTableModel defaultTableModel) throws SQLException {
        String prenom, nom;
        int numeroCompte;
        double sole, decouvert;

        while (resultSet.next()){
            numeroCompte = resultSet.getInt(1);
            sole = resultSet.getDouble(2);
            prenom = resultSet.getString(3);
            nom = resultSet.getString(4);
            decouvert = resultSet.getDouble(5);

            Object [] row = {numeroCompte, sole, prenom, nom, decouvert};
            defaultTableModel.addRow(row);
        }
    }

    public Compte creerCompte() throws SQLException {

        Compte compte = new Compte();

        compte.setPrenom(textFieldPremon.getText());
        compte.setNom(textFieldNom.getText());
        compte.setSolde(Double.parseDouble(textFieldSolde.getText()));
        compte.setDecouvert(Double.parseDouble(textFieldDecouvert.getText()));
        compte.setNumeroCompte(Integer.parseInt(textFieldNumeroCompte.getText()));

        PreparedStatement preparedStatement =
                connectionDB().prepareStatement("INSERT into client (numeroCompte, solde, prenom, nom, decouvert) VALUES (?,?, ?, ?, ?)");

        preparedStatement.setInt(1, compte.getNumeroCompte());
        preparedStatement.setDouble(2, compte.getSolde());
        preparedStatement.setString(3, compte.getPrenom());
        preparedStatement.setString(4, compte.getNom());
        preparedStatement.setDouble(5, compte.getDecouvert());

        preparedStatement.executeUpdate();

        return compte;
    }

    public String retraitSolde() throws SQLException {

        PreparedStatement preparedStatement;

        preparedStatement = connectionDB().prepareStatement("UPDATE client set solde = solde - ? WHERE numeroCompte = ?");
        preparedStatement.setDouble(1, Double.parseDouble(textFieldRetrait.getText()));
        preparedStatement.setInt(2, Integer.parseInt(textFieldNumero.getText()));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connectionDB().close();

        preparedStatement = connectionDB().prepareStatement("INSERT INTO historique (typeOperation, numeroCompte, montant) VALUES (?, ?, ?)");
        preparedStatement.setString(1, "RETRAIT");
        preparedStatement.setInt(2, Integer.parseInt(textFieldNumero.getText()));
        preparedStatement.setDouble(3, Double.parseDouble(textFieldRetrait.getText()));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connectionDB().close();

        textFieldDepot.setText("");
        textFieldNumero.setText("");
        textFieldRetrait.setText("");

        return "Retarit effectuer";
    }

    public String depotSolde() throws SQLException {

        PreparedStatement preparedStatement;

        preparedStatement = connectionDB().prepareStatement("UPDATE client set solde = solde + ? WHERE numeroCompte = ?");
        preparedStatement.setDouble(1, Double.parseDouble(textFieldDepot.getText()));
        preparedStatement.setInt(2, Integer.parseInt(textFieldNumero.getText()));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connectionDB().close();

        preparedStatement = connectionDB().prepareStatement("INSERT INTO historique (typeOperation, numeroCompte, montant) VALUES (?, ?, ?)");
        preparedStatement.setString(1, "DEPOT");
        preparedStatement.setInt(2, Integer.parseInt(textFieldNumero.getText()));
        preparedStatement.setDouble(3, Double.parseDouble(textFieldDepot.getText()));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connectionDB().close();

        textFieldDepot.setText("");
        textFieldNumero.setText("");
        textFieldRetrait.setText("");

        return "Depot effectuer";
    }

    public  Application() throws SQLException {

        initializeComponent();
        queryPrintTable("Select * from client;", connectionDB(), "client");

        buttonCreerCompte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Compte compte = creerCompte();
                    JOptionPane.showMessageDialog(buttonCreerCompte, "Compte : " + compte.toString());
                    tableCompte.setModel(new DefaultTableModel());
                    queryPrintTable("Select * from client;", connectionDB(), "client");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonHistoriqueCompte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isHistorique){
                    isHistorique = true;
                } else {
                    isHistorique = false;
                }

                try {
                    if(isHistorique){
                        tableCompte.setModel(new DefaultTableModel());
                        queryPrintTable("Select * from historique;", connectionDB(), "historique");
                    }else {
                        tableCompte.setModel(new DefaultTableModel());
                        queryPrintTable("Select * from client;", connectionDB(), "client");
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonDepot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String depot;
                try {
                    depot = depotSolde();
                    tableCompte.setModel(new DefaultTableModel());
                    queryPrintTable("Select * from client;", connectionDB(), "client");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(buttonCreerCompte, depot);
            }
        });
        buttonRetrait.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String retrait;
                try {
                    retrait = retraitSolde();
                    tableCompte.setModel(new DefaultTableModel());
                    queryPrintTable("Select * from client;", connectionDB(), "client");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(buttonCreerCompte, retrait);
            }
        });
    }

    public Connection connectionDB(){
        Connection conn = null;

        try{
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/compte_db", "root", "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
