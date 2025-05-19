/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilisateurs;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Maxime Leukam
 */
public class JavaConnect {
     public static Connection connectDB(){
        try
        {
            Class.forName("org.sqlite.JDBC");
                Connection conn = DriverManager.getConnection("jdbc:sqlite:database/biblio_poo.db");
            return conn;
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Connexion à la base de données impossible: "+e);
            return null;
        }
        
    }
}
