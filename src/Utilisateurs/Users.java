/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Utilisateurs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Date;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Maxime Leukam
 */
public class Users extends javax.swing.JFrame {

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    private DefaultTableModel tableModel;

    public Users() {
        super("Users");
        initComponents();
        conn = JavaConnect.connectDB(); 
        
        tableModel = (DefaultTableModel) tbUsers.getModel();
        tbUsers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbUsers.getColumnModel().getColumn(0).setPreferredWidth(80);
        tbUsers.getColumnModel().getColumn(1).setPreferredWidth(210);
        tbUsers.getColumnModel().getColumn(2).setPreferredWidth(190);
        tbUsers.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbUsers.getColumnModel().getColumn(4).setPreferredWidth(120);
        tbUsers.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbUsers.getColumnModel().getColumn(6).setPreferredWidth(230);

        populateTable();
        
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM Adherent";

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                Object[] rowData = new Object[columnCount + 1]; //+1 pour la colonne action
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                rowData[columnCount] = "Action"; //Placeholder
                tableModel.addRow(rowData);
            }
            addActionButtons();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des données : " + ex.getMessage(), "Erreur de la base de données", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }/*finally{
            try{
                if(rs != null) rs.close();
                if(pst != null) pst.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }*/
    }

    private void addActionButtons() {
        TableColumn actionColumn = tbUsers.getColumnModel().getColumn(tbUsers.getColumnCount() - 1);
        actionColumn.setPreferredWidth(210);
        actionColumn.setCellRenderer(new ActionCellRenderer());
        actionColumn.setCellEditor(new ActionCellEditor(tbUsers));
    }

    //Renderer pour afficher les boutons
    class ActionCellRenderer extends JPanel implements TableCellRenderer {

        public ActionCellRenderer() {
            //JButton btnEdit = new JButton("Modifier");
            JButton btnDelete = new JButton("Supprimer");
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            //btnEdit.setPreferredSize(new Dimension(80, 25));
            btnDelete.setPreferredSize(new Dimension(90, 25));
                    //add(btnEdit);
            add(btnDelete);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    //Editeur de cellule pour gérer les clics sur les boutons
    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {

        JPanel panel;
        JButton btnEdit, btnDelete;
        JTable table;

        public ActionCellEditor(JTable table) {
            this.table = table;
            panel = new JPanel(new FlowLayout());
            //btnEdit = new JButton("Modifier");
            //btnEdit.setPreferredSize(new Dimension(80, 25));
            btnDelete = new JButton("Supprimer");
            btnDelete.setPreferredSize(new Dimension(90, 25));

            /*btnEdit.addActionListener(e -> {
                int row = table.getEditingRow();
                Object id = table.getValueAt(row, 0); //On suppose que l'id est en colonne 0

                //Logique de modification
                JOptionPane.showMessageDialog(table, "Modifier ID: " + id);
                fireEditingStopped();
            });*/

            btnDelete.addActionListener(e -> {
                int row = table.getEditingRow();
                Object id = table.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(table, "Supprimer l'entrée ID: " + id + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        String sql = "DELETE FROM Adherent WHERE id_adherent = ?";
                        PreparedStatement pst = conn.prepareStatement(sql);
                        pst.setObject(1, id);
                        pst.executeUpdate();

                        ((DefaultTableModel) table.getModel()).removeRow(row);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                fireEditingStopped();
            });

            //panel.add(btnEdit);
            panel.add(btnDelete);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        public Object getCellEditorValue() {
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsers = new javax.swing.JTable();
        btnModif = new javax.swing.JButton();
        btnBorrow = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(215, 229, 243));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 51));
        jLabel1.setText("Users");

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Manage users and permissions");

        jButton1.setBackground(new java.awt.Color(105, 125, 242));
        jButton1.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("+ Add new user");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(105, 125, 242));
        jButton2.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addGap(26, 26, 26)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(37, 37, 37))
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Filter");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tbUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N°", "First Name", "Last Name", "Email", "Role", "Date Add", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUsersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUsers);

        btnModif.setBackground(new java.awt.Color(105, 125, 242));
        btnModif.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnModif.setForeground(new java.awt.Color(255, 255, 255));
        btnModif.setText("Modify");
        btnModif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifActionPerformed(evt);
            }
        });

        btnBorrow.setBackground(new java.awt.Color(105, 125, 242));
        btnBorrow.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnBorrow.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrow.setText("Borrow");
        btnBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(190, 190, 190)
                .addComponent(btnModif, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(378, 378, 378))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBorrow, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModif, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        setSize(new java.awt.Dimension(1186, 642));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tbUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsersMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbUsersMouseClicked

    private void btnModifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_btnModifActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        AddUsers users = new AddUsers();
        users.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
               String recherche = txtSearch.getText().trim();
        try{
        String sql="SELECT * FROM Adherent WHERE "+"nom_adherent LIKE? OR prenom_adherent LIKE? OR email_adherent LIKE? OR role_adherent LIKE? OR date_erg LIKE? OR id_adherent LIKE?";
        pst=conn.prepareStatement(sql);
        for(int i=1; i<=6;i++){
            pst.setString(i,"%"+recherche+"%");
        }

        rs=pst.executeQuery();
        DefaultTableModel model=(DefaultTableModel)tbUsers.getModel();
        model.setRowCount(0);
        
        while(rs.next()){
            Object[]row={
                rs.getString("id_adherent"),
                rs.getString("nom_adherent"),
                rs.getString("prenom_adherent"),
                rs.getString("email_adherent"),
                rs.getString("role_adherent"),
                rs.getString("date_erg"),
                
            };
            model.addRow(row);
        }
        rs.close();
        pst.close();
        
        }catch(Exception e){
                    JOptionPane.showMessageDialog(this, "Erreur: "+e.getMessage());

        }
        
    }                                         

    private void jButton1ActionPerformed() {                                         
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnBorrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrowActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
         Accueil Home = new Accueil();
        Home.setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Users().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrow;
    private javax.swing.JButton btnModif;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbUsers;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
