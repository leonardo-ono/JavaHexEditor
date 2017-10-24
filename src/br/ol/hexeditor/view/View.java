package br.ol.hexeditor.view;

import br.ol.hexeditor.model.HexEditor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import javax.swing.JViewport;

/**
 *
 * @author leonardo
 */
public class View extends javax.swing.JFrame implements ViewDataListener {
    
    private final HexEditor model;
    
    private final HelpContentDialog helpContentDialog;
    private final AboutDialog aboutDialog;
    /**
     * Creates new form View
     */
    public View() {
        model = new HexEditor();
        helpContentDialog = new HelpContentDialog(this, false);
        aboutDialog = new AboutDialog(this, true);
        
        initComponents();
        viewData1.setModel(model);
        viewData1.addListener(this);
        setLocationRelativeTo(null);
        viewData1.requestFocus();
        
        jScrollPane1.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);        
        ActionMap am = jScrollPane1.getActionMap();
        DisableKeysAction disableKeysAction = new DisableKeysAction();
                
        am.put("unitScrollLeft", disableKeysAction);
        am.put("unitScrollRight", disableKeysAction);
        am.put("unitScrollUp", disableKeysAction);
        am.put("unitScrollDown", disableKeysAction);
        
        updateStatusBar();
    }

    private class DisableKeysAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        viewData1 = new br.ol.hexeditor.view.ViewData();
        labelStatusBar = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemNew = new javax.swing.JMenuItem();
        menuItemOpen = new javax.swing.JMenuItem();
        menuItemSave = new javax.swing.JMenuItem();
        menuAbout = new javax.swing.JMenu();
        menuItemHelpContent = new javax.swing.JMenuItem();
        menuItemAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hex Editor");

        viewData1.setPreferredSize(new java.awt.Dimension(400, 300));

        javax.swing.GroupLayout viewData1Layout = new javax.swing.GroupLayout(viewData1);
        viewData1.setLayout(viewData1Layout);
        viewData1Layout.setHorizontalGroup(
            viewData1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 758, Short.MAX_VALUE)
        );
        viewData1Layout.setVerticalGroup(
            viewData1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 443, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(viewData1);

        labelStatusBar.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        labelStatusBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        menuFile.setText("File");

        menuItemNew.setText("New");
        menuItemNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemNewActionPerformed(evt);
            }
        });
        menuFile.add(menuItemNew);

        menuItemOpen.setText("Open");
        menuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOpenActionPerformed(evt);
            }
        });
        menuFile.add(menuItemOpen);

        menuItemSave.setText("Save");
        menuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSaveActionPerformed(evt);
            }
        });
        menuFile.add(menuItemSave);

        jMenuBar1.add(menuFile);

        menuAbout.setText("Help");

        menuItemHelpContent.setText("Help Content");
        menuItemHelpContent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemHelpContentActionPerformed(evt);
            }
        });
        menuAbout.add(menuItemHelpContent);

        menuItemAbout.setText("About");
        menuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAboutActionPerformed(evt);
            }
        });
        menuAbout.add(menuItemAbout);

        jMenuBar1.add(menuAbout);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
            .addComponent(labelStatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNewActionPerformed
        try {
            model.createNewData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_menuItemNewActionPerformed

    private void menuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpenActionPerformed
        try {
            viewData1.load();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        repaint();
    }//GEN-LAST:event_menuItemOpenActionPerformed

    private void menuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSaveActionPerformed
        try {
            viewData1.save();
            JOptionPane.showMessageDialog(this, "File saved successfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_menuItemSaveActionPerformed

    private void menuItemHelpContentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemHelpContentActionPerformed
        helpContentDialog.setVisible(true);
    }//GEN-LAST:event_menuItemHelpContentActionPerformed

    private void menuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAboutActionPerformed
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_menuItemAboutActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelStatusBar;
    private javax.swing.JMenu menuAbout;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuItemAbout;
    private javax.swing.JMenuItem menuItemHelpContent;
    private javax.swing.JMenuItem menuItemNew;
    private javax.swing.JMenuItem menuItemOpen;
    private javax.swing.JMenuItem menuItemSave;
    private br.ol.hexeditor.view.ViewData viewData1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updateUI() {
        updateStatusBar();
    }

    private void updateStatusBar() {
        String fileName = model.getFileName() == null ? "<untitled>" : model.getFileName();
        String insertMode = model.isInsertMode() ? "ON" : "OFF";
        String cursorAddress = model.getDataList().size() == 0 ? "-----" : "00000" + Integer.toHexString(model.getPosition() + model.getStartAddressOffset()).toUpperCase();
        cursorAddress = cursorAddress.substring(cursorAddress.length() - 5, cursorAddress.length());
        String mouseAddress = "-----";
        if (viewData1.getSelectedBytePosition() != null) {
            mouseAddress = "00000" + Integer.toHexString(viewData1.getSelectedBytePosition() + model.getStartAddressOffset()).toUpperCase();
            mouseAddress = mouseAddress.substring(mouseAddress.length() - 5, mouseAddress.length());
        }
        labelStatusBar.setText(" Cursor address: " + cursorAddress + " | Mouse address = " + mouseAddress + " | Insert mode: " + insertMode + " | File: " + fileName);
    }
    
}