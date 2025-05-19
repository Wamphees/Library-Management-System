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
public class BooksList extends javax.swing.JFrame {

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    private DefaultTableModel tableModel;
    
    public BooksList() {
        super("BooksList");
        initComponents();
        conn = JavaConnect.connectDB();
        tableModel = (DefaultTableModel) tbBooks.getModel();
        tbBooks.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbBooks.getColumnModel().getColumn(0).setPreferredWidth(80);
        tbBooks.getColumnModel().getColumn(1).setPreferredWidth(210);
        tbBooks.getColumnModel().getColumn(2).setPreferredWidth(180);
        tbBooks.getColumnModel().getColumn(3).setPreferredWidth(130);
        tbBooks.getColumnModel().getColumn(4).setPreferredWidth(90);
        tbBooks.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbBooks.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbBooks.getColumnModel().getColumn(7).setPreferredWidth(230);

         
        populateTable();
     
    }
    //This method fills the JTable with data from the “Book” table in the database.
    private void populateTable(){
        // On vide d'abord toutes les lignes existantes de la table avant de recharger les nouvelles données
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM Livre";
        
        try{
            // Préparation de la requête
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // Récupération du nombre de colonnes dans le résultat
            int columnCount = rs.getMetaData().getColumnCount();
            
            while(rs.next()){
             // Création d'un tableau d'objets pour représenter une ligne
            // On ajoute une colonne supplémentaire pour les boutons d'action
                Object[] rowData = new Object[columnCount + 1]; //+1 pour la colonne action
                // Remplissage du tableau avec les données de la ligne
                for(int i = 1; i <= columnCount; i++){
                    rowData[i - 1] = rs.getObject(i);
                }
                // Dernière colonne : un libellé "Action" (par exemple pour un bouton Supprimer/Modifier)
                rowData[columnCount] = "Action"; //Placeholder
                tableModel.addRow(rowData);
            }
            addActionButtons();
            
        }catch(SQLException ex){
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
    
    private void addActionButtons(){
        TableColumn actionColumn = tbBooks.getColumnModel().getColumn(tbBooks.getColumnCount() - 1);
        actionColumn.setPreferredWidth(210);
        actionColumn.setCellRenderer(new ActionCellRenderer());
        actionColumn.setCellEditor(new ActionCellEditor(tbBooks));
    }
    
    //Renderer pour afficher les boutons
class ActionCellRenderer extends JPanel implements TableCellRenderer {
    public ActionCellRenderer() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnEdit = new JButton("Modifier");
        JButton btnDelete = new JButton("Supprimer");
        btnEdit.setPreferredSize(new Dimension(80, 25));
        btnDelete.setPreferredSize(new Dimension(90, 25));
        add(btnEdit);
        add(btnDelete);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

    
    //Editeur de cellule pour gérer les clics sur les boutons
class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private JTable table;

    public ActionCellEditor(JTable table) {
        this.table = table;
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnEdit = new JButton("Modifier");
        btnDelete = new JButton("Supprimer");

        btnEdit.setPreferredSize(new Dimension(80, 25));
        btnDelete.setPreferredSize(new Dimension(90, 25));

        panel.add(btnEdit);
        panel.add(btnDelete);

        // Action Modifier
        btnEdit.addActionListener(e -> {
            int row = table.getEditingRow();
            if (row >= 0) {
                String id = table.getValueAt(row, 0).toString();
                String title = (String) table.getValueAt(row, 1);
                String author = (String) table.getValueAt(row, 2);
                String type = (String) table.getValueAt(row, 3);
                int quantity = (int) table.getValueAt(row, 4);
                String edition = (String) table.getValueAt(row, 5);
                String date = (String) table.getValueAt(row, 6);

                // Ouvre la fenêtre de modification
                Jmodif fenetre = new Jmodif(id, title, author, type, quantity, edition, date);
                fenetre.setVisible(true);
            }
            fireEditingStopped(); // Important
        });

        // Action Supprimer
        btnDelete.addActionListener(e -> {
            int row = table.getEditingRow();
            if (row >= 0) {
                String id = table.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ce livre ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        String sql = "DELETE FROM Livre WHERE ISBN = ?";
                        PreparedStatement pst = conn.prepareStatement(sql);
                        pst.setString(1, id);
                        pst.executeUpdate();
                        ((DefaultTableModel) table.getModel()).removeRow(row);
                        JOptionPane.showMessageDialog(null, "Livre supprimé !");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage());
                    }
                }
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        DateR = new com.toedter.calendar.JDateChooser();
        DateRC = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbBooks = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(105, 125, 242));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setMaximumSize(new java.awt.Dimension(26, 26));

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Books List");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(424, 424, 424))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(105, 125, 242));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Search by book name or description");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Value to Search :");

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(234, 234, 234));
        jButton1.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(105, 125, 242));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Search book between two date");

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Date Received between :");

        jLabel7.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("and");

        jButton2.setBackground(new java.awt.Color(227, 225, 225));
        jButton2.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        jButton2.setText("Search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(DateR, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DateRC, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DateRC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DateR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tbBooks.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        tbBooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Title", "Author", "Genre", "Quantity", "Edition", "Date-RCV", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbBooks.setDoubleBuffered(true);
        tbBooks.setGridColor(new java.awt.Color(105, 125, 242));
        tbBooks.setSelectionBackground(new java.awt.Color(105, 125, 242));
        jScrollPane1.setViewportView(tbBooks);

        btnRefresh.setBackground(new java.awt.Color(105, 125, 242));
        btnRefresh.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1079, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(503, 503, 503)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1169, 711));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
        String recherche = txtSearch.getText().trim();
        try{
        String sql="SELECT * FROM livre WHERE "+"titre LIKE? OR auteur LIKE? OR genre LIKE? OR quantite LIKE? OR edition LIKE? OR ISBN LIKE?";
        pst=conn.prepareStatement(sql);
        for(int i=1; i<=6;i++){
            pst.setString(i,"%"+recherche+"%");
        }

        rs=pst.executeQuery();
        DefaultTableModel model=(DefaultTableModel)tbBooks.getModel();
        model.setRowCount(0);
        
        while(rs.next()){
            Object[]row={
                rs.getString("ISBN"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getString("genre"),
                rs.getString("quantite"),
                rs.getString("edition"),
                
            };
            model.addRow(row);
        }
        rs.close();
        pst.close();
        
        }catch(Exception e){
                    JOptionPane.showMessageDialog(this, "Erreur: "+e.getMessage());

        }
        
    }//GEN-LAST:event_txtSearchActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String recherche = txtSearch.getText().trim();
        try{
        String sql="SELECT * FROM livre WHERE "+"titre LIKE? OR auteur LIKE? OR genre LIKE? OR quantite LIKE? OR edition LIKE? OR ISBN LIKE?";
        pst=conn.prepareStatement(sql);
        for(int i=1; i<=6;i++){
            pst.setString(i,"%"+recherche+"%");
        }

        rs=pst.executeQuery();
        DefaultTableModel model=(DefaultTableModel)tbBooks.getModel();
        model.setRowCount(0);
        
        while(rs.next()){
            Object[]row={
                rs.getString("ISBN"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getString("genre"),
                rs.getString("quantite"),
                rs.getString("edition"),
                
            };
            model.addRow(row);
        }
        rs.close();
        pst.close();
        
        }catch(Exception e){
                    JOptionPane.showMessageDialog(this, "Erreur: "+e.getMessage());

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        java.util.Date date_debut = DateR.getDate();
        java.util.Date date_fin = DateRC.getDate();
        
        if(date_debut == null || date_fin == null){
            JOptionPane.showMessageDialog(this, "Veuillez selectionner les deux dates");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = sdf.format(date_debut);
        String date2 = sdf.format(date_fin);

        try{
        String sql="SELECT * FROM livre WHERE date_reception between ? AND ?";
        pst=conn.prepareStatement(sql);
        pst.setString(1, date1);
        pst.setString(2, date2);

        rs=pst.executeQuery();
        
        DefaultTableModel model=(DefaultTableModel)tbBooks.getModel();
        model.setRowCount(0);
        
        while(rs.next()){
            Object[]row={
                rs.getString("ISBN"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getString("genre"),
                rs.getString("quantite"),
                rs.getString("edition"),
                rs.getString("date_reception"),

                
                
            };
            model.addRow(row);
        }
        rs.close();
        pst.close();
        
        }catch(Exception e){
                    JOptionPane.showMessageDialog(this, "Erreur: "+e.getMessage());

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM Livre";
        
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            
            while(rs.next()){
                Object[] rowData = new Object[columnCount + 1]; //+1 pour la colonne action
                for(int i = 1; i <= columnCount; i++){
                    rowData[i - 1] = rs.getObject(i);
                }
                rowData[columnCount] = "Action"; //Placeholder
                tableModel.addRow(rowData);
            }
            addActionButtons();
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des données : " + ex.getMessage(), "Erreur de la base de données", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Accueil Home = new Accueil();
        Home.setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(BooksList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BooksList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BooksList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BooksList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BooksList().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DateR;
    private com.toedter.calendar.JDateChooser DateRC;
    private javax.swing.JButton btnRefresh;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbBooks;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
