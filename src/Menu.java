import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fenti
 */
public class Menu extends javax.swing.JFrame {
    
    public Menu() {
        initComponents();
        panelIncome.setVisible(false);
        panelExpense.setVisible(false);
        panelTotal.setVisible(false);
        //getConnection(); /*Test Purposes*/        
    }
    
    
    public Connection getConnection()
    {
        Connection con = null;
        
        try
        {
            con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            JOptionPane.showMessageDialog(null, "Connected!");
            return con;
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Not Connected!");
            return null;
        }                
    }
    
    
    
    //Get Expense Table
    void getIncome()
    {
        tableIncome.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 14));   //Set Header Font
        tableIncome.setAutoCreateRowSorter(true);                                   //Sort Row
        tableIncome.getRowSorter().toggleSortOrder(0);                              //Automatically Sort Row by Number
        DefaultTableModel dtm = (DefaultTableModel)tableIncome.getModel();
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from incomeTable");            
            int col = rs.getMetaData().getColumnCount();
            while(tableIncome.getRowCount() > 0)
                dtm.removeRow(0);
            while(rs.next())
            {
                Object rows[] = new Object[col];
                for(int i = 1; i <= col; i++)
                    rows[i-1] = rs.getObject(i);
                dtm.insertRow(rs.getRow() - 1, rows);
            }           
            rs.close();
            st.close();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Unable to get data!");
        }
    }
    
    //Add Income to Table
    void addIncome()
    {
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            PreparedStatement ps = con.prepareStatement("insert into incomeTable (date, income, note) values (?,?,?)"); 
            ps.setString(1, incomeDateField.getText());
            ps.setString(2, incomeField.getText());
            ps.setString(3, incomeNoteField.getText());
            int i = ps.executeUpdate();
            if(i>0)
                JOptionPane.showMessageDialog(null, "Saved!");
            else
                JOptionPane.showMessageDialog(null, "Unable to save!");
            getIncome();    //Refresh the Table.
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //Delete Income
    void deleteIncome()
    {
        int row = tableIncome.getSelectedRow();
        
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            String date = tableIncome.getModel().getValueAt(row, 0).toString();
            String income = tableIncome.getModel().getValueAt(row, 1).toString();
            String note = tableIncome.getModel().getValueAt(row, 2).toString();
            PreparedStatement ps = con.prepareStatement("delete from incomeTable where (date = '" + date + "' and income = '" + income + "' and note = '" + note + "')");
            ps.execute();
            getIncome();    //Refresh the Table.
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //Get Expense Table
    void getExpense()
    {
        tableExpense.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 14));  //Set Header Font
        tableExpense.setAutoCreateRowSorter(true);                                  //Sort Row
        tableExpense.getRowSorter().toggleSortOrder(0);                             //Automatically Sort Row by Number
        DefaultTableModel dtm = (DefaultTableModel)tableExpense.getModel();
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from expenseTable");            
            int col = rs.getMetaData().getColumnCount();
            while(tableExpense.getRowCount() > 0)
                dtm.removeRow(0);
            while(rs.next())
            {
                Object rows[] = new Object[col];
                for(int i = 1; i <= col; i++)
                    rows[i-1] = rs.getObject(i);
                dtm.insertRow(rs.getRow() - 1, rows);
            }           
            rs.close();
            st.close();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Unable to get data!");
        }
    }
    
    //Add Expense to Table
    void addExpense()
    {
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            PreparedStatement ps = con.prepareStatement("insert into expenseTable (date, expense, note) values (?,?,?)"); 
            ps.setString(1, expenseDateField.getText());
            ps.setString(2, expenseField.getText());
            ps.setString(3, expenseNoteField.getText());
            int i = ps.executeUpdate();
            if(i>0)
                JOptionPane.showMessageDialog(null, "Saved!");
            else
                JOptionPane.showMessageDialog(null, "Unable to save!");
            getExpense();   //Refresh the Table.
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //Delete Expense
    void deleteExpense()
    {
        int row = tableExpense.getSelectedRow();
        
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            String date = tableExpense.getModel().getValueAt(row, 0).toString();
            String expense = tableExpense.getModel().getValueAt(row, 1).toString();
            String note = tableExpense.getModel().getValueAt(row, 2).toString();
            PreparedStatement ps = con.prepareStatement("delete from expenseTable where (date = '" + date + "' and expense = '" + expense + "' and note = '" + note + "')");
            ps.execute();
            getExpense();   //Refresh the Table.
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    double getTotalIncome()
    {
        try
        {
            double income = 0;            
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from incomeTable");
            while(rs.next())
                income += rs.getDouble("income");
            return income;
        }
        catch(Exception ex)
        {
             JOptionPane.showMessageDialog(null, ex);     
             return 0;
        }
    }
    
    double getTotalExpense()
    {
        DecimalFormat df = new DecimalFormat(".00");
        try
        {
            double expense = 0;            
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project1","root","ab17032725");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from expenseTable");
            while(rs.next())
                expense += rs.getDouble("expense");
            return expense;
            //totalExpenseField.setText(df.format(expense));
            //totalExpenseField.setEditable(false);
        }
        catch(Exception ex)
        {
             JOptionPane.showMessageDialog(null, ex);
             return 0;
        }
    }
    
    void getTotal()
    {
        DecimalFormat df = new DecimalFormat(".00");
        double income = getTotalIncome();
        double expense = getTotalExpense();
        totalIncomeField.setEditable(false);
        totalExpenseField.setEditable(false);
        totalField.setEditable(false);
        totalIncomeField.setText(df.format(income));
        totalExpenseField.setText(df.format(expense));
        totalField.setText(df.format(income - expense));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        panelTotal = new javax.swing.JPanel();
        total = new javax.swing.JLabel();
        totalExpense1 = new javax.swing.JLabel();
        totalIncome1 = new javax.swing.JLabel();
        totalIncomeField = new javax.swing.JTextField();
        totalExpenseField = new javax.swing.JTextField();
        totalField = new javax.swing.JTextField();
        $income = new javax.swing.JLabel();
        $expense = new javax.swing.JLabel();
        $total = new javax.swing.JLabel();
        panelExpense = new javax.swing.JPanel();
        expenseDate = new javax.swing.JLabel();
        expenseDateField = new javax.swing.JTextField();
        expenseExpense = new javax.swing.JLabel();
        expenseField = new javax.swing.JTextField();
        expenseNote = new javax.swing.JLabel();
        expenseNoteField = new javax.swing.JTextField();
        expenseScroll = new javax.swing.JScrollPane();
        tableExpense = new javax.swing.JTable();
        expenseButtonSave = new javax.swing.JButton();
        expenseButtonDelete = new javax.swing.JButton();
        panelIncome = new javax.swing.JPanel();
        incomeDate = new javax.swing.JLabel();
        incomeDateField = new javax.swing.JTextField();
        incomeIncome = new javax.swing.JLabel();
        incomeField = new javax.swing.JTextField();
        incomeNote = new javax.swing.JLabel();
        incomeNoteField = new javax.swing.JTextField();
        incomeScroll = new javax.swing.JScrollPane();
        tableIncome = new javax.swing.JTable();
        incomeButtonSave = new javax.swing.JButton();
        incomeButtonDelete = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuIncome = new javax.swing.JMenuItem();
        menuExpense = new javax.swing.JMenuItem();
        menuTotal = new javax.swing.JMenuItem();

        jMenu1.setText("File");
        jMenuBar2.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar2.add(jMenu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        total.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        total.setText("Total:");
        total.setPreferredSize(new java.awt.Dimension(60, 20));

        totalExpense1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        totalExpense1.setText("Expense:");
        totalExpense1.setPreferredSize(new java.awt.Dimension(60, 20));

        totalIncome1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        totalIncome1.setText("Income:");
        totalIncome1.setPreferredSize(new java.awt.Dimension(60, 20));

        totalIncomeField.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        totalExpenseField.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        totalField.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        $income.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        $income.setText("$");

        $expense.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        $expense.setText("$");

        $total.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        $total.setText("$");

        javax.swing.GroupLayout panelTotalLayout = new javax.swing.GroupLayout(panelTotal);
        panelTotal.setLayout(panelTotalLayout);
        panelTotalLayout.setHorizontalGroup(
            panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalLayout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addGroup(panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalLayout.createSequentialGroup()
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent($total)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTotalLayout.createSequentialGroup()
                        .addGroup(panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(totalIncome1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(totalExpense1, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalLayout.createSequentialGroup()
                                .addComponent($expense)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalExpenseField, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalLayout.createSequentialGroup()
                                .addComponent($income)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalIncomeField, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(672, Short.MAX_VALUE))
        );
        panelTotalLayout.setVerticalGroup(
            panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalLayout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addGroup(panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalIncome1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalIncomeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent($income, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalExpense1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(totalExpenseField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent($expense, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent($total, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(193, 193, 193))
        );

        expenseDate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        expenseDate.setText("Date:");
        expenseDate.setPreferredSize(new java.awt.Dimension(60, 20));

        expenseExpense.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        expenseExpense.setText("Expense:");
        expenseExpense.setPreferredSize(new java.awt.Dimension(60, 20));

        expenseNote.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        expenseNote.setText("Note:");
        expenseNote.setPreferredSize(new java.awt.Dimension(60, 20));

        expenseScroll.setToolTipText("");

        tableExpense.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tableExpense.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Expense", "Note"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableExpense.setRowHeight(24);
        tableExpense.setShowHorizontalLines(false);
        tableExpense.setShowVerticalLines(false);
        tableExpense.setSurrendersFocusOnKeystroke(true);
        tableExpense.getTableHeader().setReorderingAllowed(false);
        expenseScroll.setViewportView(tableExpense);

        expenseButtonSave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        expenseButtonSave.setText("Save");
        expenseButtonSave.setPreferredSize(new java.awt.Dimension(90, 25));
        expenseButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expenseButtonSaveActionPerformed(evt);
            }
        });

        expenseButtonDelete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        expenseButtonDelete.setText("Delete");
        expenseButtonDelete.setPreferredSize(new java.awt.Dimension(90, 25));
        expenseButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expenseButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelExpenseLayout = new javax.swing.GroupLayout(panelExpense);
        panelExpense.setLayout(panelExpenseLayout);
        panelExpenseLayout.setHorizontalGroup(
            panelExpenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelExpenseLayout.createSequentialGroup()
                .addGroup(panelExpenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelExpenseLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(expenseButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(expenseButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelExpenseLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(panelExpenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(expenseScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 1110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelExpenseLayout.createSequentialGroup()
                                .addComponent(expenseDate, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(expenseDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(expenseExpense, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(expenseField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(expenseNote, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(expenseNoteField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        panelExpenseLayout.setVerticalGroup(
            panelExpenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelExpenseLayout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addGroup(panelExpenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expenseDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expenseDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expenseExpense, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expenseField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expenseNote, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expenseNoteField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(expenseScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(panelExpenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expenseButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expenseButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        incomeDate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        incomeDate.setText("Date:");
        incomeDate.setPreferredSize(new java.awt.Dimension(60, 20));

        incomeIncome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        incomeIncome.setText("Income:");
        incomeIncome.setPreferredSize(new java.awt.Dimension(60, 20));

        incomeNote.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        incomeNote.setText("Note:");
        incomeNote.setPreferredSize(new java.awt.Dimension(60, 20));

        incomeScroll.setToolTipText("");

        tableIncome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tableIncome.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Income", "Note"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableIncome.setRowHeight(24);
        tableIncome.setShowHorizontalLines(false);
        tableIncome.setShowVerticalLines(false);
        tableIncome.setSurrendersFocusOnKeystroke(true);
        tableIncome.getTableHeader().setReorderingAllowed(false);
        incomeScroll.setViewportView(tableIncome);
        tableIncome.getAccessibleContext().setAccessibleName("");
        tableIncome.getAccessibleContext().setAccessibleDescription("");

        incomeButtonSave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        incomeButtonSave.setText("Save");
        incomeButtonSave.setPreferredSize(new java.awt.Dimension(90, 25));
        incomeButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incomeButtonSaveActionPerformed(evt);
            }
        });

        incomeButtonDelete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        incomeButtonDelete.setText("Delete");
        incomeButtonDelete.setPreferredSize(new java.awt.Dimension(90, 25));
        incomeButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incomeButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelIncomeLayout = new javax.swing.GroupLayout(panelIncome);
        panelIncome.setLayout(panelIncomeLayout);
        panelIncomeLayout.setHorizontalGroup(
            panelIncomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIncomeLayout.createSequentialGroup()
                .addGroup(panelIncomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelIncomeLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(incomeButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(incomeButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelIncomeLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(panelIncomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(incomeScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 1110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelIncomeLayout.createSequentialGroup()
                                .addComponent(incomeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(incomeDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(incomeIncome, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(incomeField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(incomeNote, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(incomeNoteField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        panelIncomeLayout.setVerticalGroup(
            panelIncomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelIncomeLayout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addGroup(panelIncomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(incomeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incomeDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incomeIncome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incomeField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incomeNote, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incomeNoteField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(incomeScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(panelIncomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(incomeButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incomeButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        menuFile.setText("File");
        menuFile.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        menuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileActionPerformed(evt);
            }
        });

        menuIncome.setText("Income");
        menuIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIncomeActionPerformed(evt);
            }
        });
        menuFile.add(menuIncome);

        menuExpense.setText("Expense");
        menuExpense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExpenseActionPerformed(evt);
            }
        });
        menuFile.add(menuExpense);

        menuTotal.setText("Total");
        menuTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTotalActionPerformed(evt);
            }
        });
        menuFile.add(menuTotal);

        jMenuBar1.add(menuFile);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelExpense, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelExpense, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void incomeButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incomeButtonSaveActionPerformed
        addIncome();
    }//GEN-LAST:event_incomeButtonSaveActionPerformed

    private void menuIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIncomeActionPerformed
        panelExpense.setVisible(false);
        panelTotal.setVisible(false);
        panelIncome.setVisible(true);
        getIncome();
    }//GEN-LAST:event_menuIncomeActionPerformed

    private void expenseButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expenseButtonSaveActionPerformed
        addExpense();
    }//GEN-LAST:event_expenseButtonSaveActionPerformed

    private void menuExpenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExpenseActionPerformed
        panelIncome.setVisible(false);
        panelTotal.setVisible(false);
        panelExpense.setVisible(true);
        getExpense();
    }//GEN-LAST:event_menuExpenseActionPerformed

    private void incomeButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incomeButtonDeleteActionPerformed
        deleteIncome();
    }//GEN-LAST:event_incomeButtonDeleteActionPerformed

    private void expenseButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expenseButtonDeleteActionPerformed
        deleteExpense();
    }//GEN-LAST:event_expenseButtonDeleteActionPerformed

    private void menuTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTotalActionPerformed
        panelIncome.setVisible(false);
        panelExpense.setVisible(false);
        panelTotal.setVisible(true);
        getTotal();
    }//GEN-LAST:event_menuTotalActionPerformed

    private void menuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileActionPerformed
        
    }//GEN-LAST:event_menuFileActionPerformed

    
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Menu().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel $expense;
    private javax.swing.JLabel $income;
    private javax.swing.JLabel $total;
    private javax.swing.JButton expenseButtonDelete;
    private javax.swing.JButton expenseButtonSave;
    private javax.swing.JLabel expenseDate;
    private javax.swing.JTextField expenseDateField;
    private javax.swing.JLabel expenseExpense;
    private javax.swing.JTextField expenseField;
    private javax.swing.JLabel expenseNote;
    private javax.swing.JTextField expenseNoteField;
    private javax.swing.JScrollPane expenseScroll;
    private javax.swing.JButton incomeButtonDelete;
    private javax.swing.JButton incomeButtonSave;
    private javax.swing.JLabel incomeDate;
    private javax.swing.JTextField incomeDateField;
    private javax.swing.JTextField incomeField;
    private javax.swing.JLabel incomeIncome;
    private javax.swing.JLabel incomeNote;
    private javax.swing.JTextField incomeNoteField;
    private javax.swing.JScrollPane incomeScroll;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JMenuItem menuExpense;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuIncome;
    private javax.swing.JMenuItem menuTotal;
    private javax.swing.JPanel panelExpense;
    private javax.swing.JPanel panelIncome;
    private javax.swing.JPanel panelTotal;
    private javax.swing.JTable tableExpense;
    private javax.swing.JTable tableIncome;
    private javax.swing.JLabel total;
    private javax.swing.JLabel totalExpense1;
    private javax.swing.JTextField totalExpenseField;
    private javax.swing.JTextField totalField;
    private javax.swing.JLabel totalIncome1;
    private javax.swing.JTextField totalIncomeField;
    // End of variables declaration//GEN-END:variables
}
