package fcfp.gui;

import fcfp.pp.PPEngine;

/**
 * Main window of the application.
 * @author Simão Paulo Rato Alves Reis
 * @author Rafael Saraiva Figueiredo
 * @version 2.2
 */
public class FlexibleCryptographicFileProtection extends javax.swing.JFrame {

    private PPEngine ppEngine;

    /**
     * Load all EcryptionPP and IntegrityPP located in the system plugins
     * folder.
     */
    private void loadPPs() {

        cipherAndDecipherButton.setEnabled(false);
        ppEngine.loadPPs();
        encryptionComboBox.setModel(new javax.swing.DefaultComboBoxModel(ppEngine.getEncryptionPPNames().toArray()));
        integrityComboBox.setModel(new javax.swing.DefaultComboBoxModel(ppEngine.getIntegrityPPNames().toArray()));
        if (ppEngine.hasEncryptionPPs() && ppEngine.hasIntegrityPPs()) {
            cipherAndDecipherButton.setEnabled(true);
        } else {
            cipherAndDecipherButton.setEnabled(false);
        }
    }

    /**
     * Creates new form FlexibleCryptographicFileProtection.
     */
    public FlexibleCryptographicFileProtection() {

        initComponents();
        setLocationRelativeTo(null);
        cipherRadioButton.setSelected(true);
        dummyFilePathTextField.setEnabled(false);
        dummyFilePathLabel.setEnabled(false);
        dummyFilePathButton.setEnabled(false);
        pngFilePathLabel.setEnabled(false);
        pngFilePathTextField.setEnabled(false);
        pngFilePathButton.setEnabled(false);
        useRealNameRadioButton.setEnabled(false);
        ppEngine = PPEngine.getInstance();
        loadPPs();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        ppPanel = new javax.swing.JPanel();
        integrityComboBox = new javax.swing.JComboBox();
        encryptionComboBox = new javax.swing.JComboBox();
        encryptionPPLabel = new javax.swing.JLabel();
        integrityPPLabel = new javax.swing.JLabel();
        reloadPPsButton = new javax.swing.JButton();
        keyPanel = new javax.swing.JPanel();
        keyTextField = new javax.swing.JPasswordField();
        keyLabel = new javax.swing.JLabel();
        dummyKeyLabel = new javax.swing.JLabel();
        dummyKeyTextField = new javax.swing.JTextField();
        contentPanel = new javax.swing.JPanel();
        originalFilePathLabel = new javax.swing.JLabel();
        originalFilePathTextField = new javax.swing.JTextField();
        originalFilePathButton = new javax.swing.JButton();
        newFileNameLabel = new javax.swing.JLabel();
        newFilenameTextField = new javax.swing.JTextField();
        useRealNameRadioButton = new javax.swing.JRadioButton();
        steganographyPanel = new javax.swing.JPanel();
        pngFilePathLabel = new javax.swing.JLabel();
        pngFilePathTextField = new javax.swing.JTextField();
        steganographyRadioButton = new javax.swing.JRadioButton();
        pngFilePathButton = new javax.swing.JButton();
        cipherModePanel = new javax.swing.JPanel();
        cipherRadioButton = new javax.swing.JRadioButton();
        decipherRadioButton = new javax.swing.JRadioButton();
        cipherAndDecipherButton = new javax.swing.JButton();
        dummyContentPanel = new javax.swing.JPanel();
        dummyFilePathLabel = new javax.swing.JLabel();
        dummyFilePathTextField = new javax.swing.JTextField();
        dummyFilePathButton = new javax.swing.JButton();
        dummyContentRadioButton = new javax.swing.JRadioButton();
        fcfpMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("fcfpFrame"); // NOI18N
        setResizable(false);

        ppPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        encryptionPPLabel.setText("Encryption PP");

        integrityPPLabel.setText("Integrity PP");

        reloadPPsButton.setText("Reload PPs");
        reloadPPsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadPPsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ppPanelLayout = new javax.swing.GroupLayout(ppPanel);
        ppPanel.setLayout(ppPanelLayout);
        ppPanelLayout.setHorizontalGroup(
            ppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ppPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(encryptionPPLabel)
                    .addComponent(integrityPPLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(integrityComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(encryptionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reloadPPsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ppPanelLayout.setVerticalGroup(
            ppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ppPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(encryptionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(encryptionPPLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ppPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(integrityPPLabel)
                    .addComponent(integrityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reloadPPsButton))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        keyPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        keyLabel.setText("Password");

        dummyKeyLabel.setText("Dummy Pass");

        javax.swing.GroupLayout keyPanelLayout = new javax.swing.GroupLayout(keyPanel);
        keyPanel.setLayout(keyPanelLayout);
        keyPanelLayout.setHorizontalGroup(
            keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(keyLabel)
                    .addComponent(dummyKeyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dummyKeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(keyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        keyPanelLayout.setVerticalGroup(
            keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, keyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(keyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(keyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dummyKeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dummyKeyLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        contentPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        originalFilePathLabel.setText("Original File Path");

        originalFilePathButton.setText("Open File");
        originalFilePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                originalFilePathButtonActionPerformed(evt);
            }
        });

        newFileNameLabel.setText("New File Name");

        useRealNameRadioButton.setText("Original Name");
        useRealNameRadioButton.setActionCommand("Real Name");

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(originalFilePathLabel)
                    .addComponent(newFileNameLabel))
                .addGap(14, 14, 14)
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(originalFilePathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(newFilenameTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(originalFilePathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(useRealNameRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(originalFilePathLabel)
                    .addComponent(originalFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(originalFilePathButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newFileNameLabel)
                    .addComponent(newFilenameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(useRealNameRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        steganographyPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pngFilePathLabel.setText("PNG File Path");

        steganographyRadioButton.setText("Steganography");
        steganographyRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                steganographyRadioButtonActionPerformed(evt);
            }
        });

        pngFilePathButton.setText("Open File");
        pngFilePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pngFilePathButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout steganographyPanelLayout = new javax.swing.GroupLayout(steganographyPanel);
        steganographyPanel.setLayout(steganographyPanelLayout);
        steganographyPanelLayout.setHorizontalGroup(
            steganographyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(steganographyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(steganographyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(steganographyPanelLayout.createSequentialGroup()
                        .addComponent(pngFilePathLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pngFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pngFilePathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(steganographyPanelLayout.createSequentialGroup()
                        .addComponent(steganographyRadioButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        steganographyPanelLayout.setVerticalGroup(
            steganographyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(steganographyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(steganographyRadioButton)
                .addGap(3, 3, 3)
                .addGroup(steganographyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pngFilePathLabel)
                    .addComponent(pngFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pngFilePathButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cipherModePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        buttonGroup.add(cipherRadioButton);
        cipherRadioButton.setText("Cipher");
        cipherRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cipherRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(decipherRadioButton);
        decipherRadioButton.setText("Decipher");
        decipherRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decipherRadioButtonActionPerformed(evt);
            }
        });

        cipherAndDecipherButton.setText("Start");
        cipherAndDecipherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cipherAndDecipherButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cipherModePanelLayout = new javax.swing.GroupLayout(cipherModePanel);
        cipherModePanel.setLayout(cipherModePanelLayout);
        cipherModePanelLayout.setHorizontalGroup(
            cipherModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cipherModePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cipherRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decipherRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cipherAndDecipherButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        cipherModePanelLayout.setVerticalGroup(
            cipherModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cipherModePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cipherModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cipherRadioButton)
                    .addComponent(decipherRadioButton)
                    .addComponent(cipherAndDecipherButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dummyContentPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        dummyFilePathLabel.setText("Dummy File Path");

        dummyFilePathButton.setText("Open File");
        dummyFilePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dummyFilePathButtonActionPerformed(evt);
            }
        });

        dummyContentRadioButton.setText("Dummy Content");
        dummyContentRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dummyContentRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dummyContentPanelLayout = new javax.swing.GroupLayout(dummyContentPanel);
        dummyContentPanel.setLayout(dummyContentPanelLayout);
        dummyContentPanelLayout.setHorizontalGroup(
            dummyContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dummyContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dummyContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dummyContentRadioButton)
                    .addGroup(dummyContentPanelLayout.createSequentialGroup()
                        .addComponent(dummyFilePathLabel)
                        .addGap(18, 18, 18)
                        .addComponent(dummyFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dummyFilePathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        dummyContentPanelLayout.setVerticalGroup(
            dummyContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dummyContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dummyContentRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dummyContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dummyFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dummyFilePathButton)
                    .addComponent(dummyFilePathLabel))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        fileMenu.setText("File");
        fcfpMenuBar.add(fileMenu);

        aboutMenu.setText("About");
        aboutMenu.setActionCommand("Option");
        fcfpMenuBar.add(aboutMenu);

        setJMenuBar(fcfpMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(steganographyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cipherModePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(keyPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ppPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(dummyContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(dummyContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(steganographyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ppPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(keyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cipherModePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void originalFilePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_originalFilePathButtonActionPerformed
        new FileChooserFrame(originalFilePathTextField).setVisible(true);
    }//GEN-LAST:event_originalFilePathButtonActionPerformed

    private void dummyContentRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dummyContentRadioButtonActionPerformed
        if (dummyContentRadioButton.isSelected()) {
            dummyFilePathTextField.setEnabled(true);
            dummyFilePathLabel.setEnabled(true);
            dummyFilePathButton.setEnabled(true);
        } else {
            dummyFilePathTextField.setEnabled(false);
            dummyFilePathLabel.setEnabled(false);
            dummyFilePathButton.setEnabled(false);
        }
    }//GEN-LAST:event_dummyContentRadioButtonActionPerformed

    private void dummyFilePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dummyFilePathButtonActionPerformed
        new FileChooserFrame(dummyFilePathTextField).setVisible(true);
    }//GEN-LAST:event_dummyFilePathButtonActionPerformed

    private void steganographyRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_steganographyRadioButtonActionPerformed
        if (steganographyRadioButton.isSelected()) {
            pngFilePathTextField.setEnabled(true);
            pngFilePathLabel.setEnabled(true);
            pngFilePathButton.setEnabled(true);
        } else {
            pngFilePathTextField.setEnabled(false);
            pngFilePathLabel.setEnabled(false);
            pngFilePathButton.setEnabled(false);
        }
    }//GEN-LAST:event_steganographyRadioButtonActionPerformed

    private void pngFilePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pngFilePathButtonActionPerformed
        new FileChooserFrame(pngFilePathTextField, ".png").setVisible(true);
    }//GEN-LAST:event_pngFilePathButtonActionPerformed

    private void reloadPPsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadPPsButtonActionPerformed
        loadPPs();
    }//GEN-LAST:event_reloadPPsButtonActionPerformed

    private void cipherRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cipherRadioButtonActionPerformed
        newFileNameLabel.setEnabled(true);
        newFilenameTextField.setEnabled(true);
        dummyContentRadioButton.setEnabled(true);
        if (dummyContentRadioButton.isSelected()) {
            dummyFilePathTextField.setEnabled(true);
            dummyFilePathLabel.setEnabled(true);
            dummyFilePathButton.setEnabled(true);
        }
        steganographyRadioButton.setEnabled(true);
        if (steganographyRadioButton.isSelected()) {
            pngFilePathTextField.setEnabled(true);
            pngFilePathLabel.setEnabled(true);
            pngFilePathButton.setEnabled(true);
        }
        dummyKeyLabel.setEnabled(true);
        dummyKeyTextField.setEnabled(true);
        encryptionPPLabel.setEnabled(true);
        encryptionComboBox.setEnabled(true);
        integrityPPLabel.setEnabled(true);
        integrityComboBox.setEnabled(true);
        reloadPPsButton.setEnabled(true);
        useRealNameRadioButton.setEnabled(false);
    }//GEN-LAST:event_cipherRadioButtonActionPerformed

    private void decipherRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decipherRadioButtonActionPerformed
        if (useRealNameRadioButton.isSelected()) {
            newFileNameLabel.setEnabled(false);
            newFilenameTextField.setEnabled(false);
        } else {
            newFileNameLabel.setEnabled(true);
            newFilenameTextField.setEnabled(true);
        }
        dummyContentRadioButton.setEnabled(false);
        dummyFilePathLabel.setEnabled(false);
        dummyFilePathTextField.setEnabled(false);
        dummyFilePathButton.setEnabled(false);
        steganographyRadioButton.setEnabled(false);
        pngFilePathTextField.setEnabled(false);
        pngFilePathLabel.setEnabled(false);
        pngFilePathButton.setEnabled(false);
        dummyKeyLabel.setEnabled(false);
        dummyKeyTextField.setEnabled(false);
        encryptionPPLabel.setEnabled(false);
        encryptionComboBox.setEnabled(false);
        integrityPPLabel.setEnabled(false);
        integrityComboBox.setEnabled(false);
        reloadPPsButton.setEnabled(false);
        useRealNameRadioButton.setEnabled(true);
    }//GEN-LAST:event_decipherRadioButtonActionPerformed

    private void cipherAndDecipherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cipherAndDecipherButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cipherAndDecipherButtonActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FlexibleCryptographicFileProtection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FlexibleCryptographicFileProtection().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton cipherAndDecipherButton;
    private javax.swing.JPanel cipherModePanel;
    private javax.swing.JRadioButton cipherRadioButton;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JRadioButton decipherRadioButton;
    private javax.swing.JPanel dummyContentPanel;
    private javax.swing.JRadioButton dummyContentRadioButton;
    private javax.swing.JButton dummyFilePathButton;
    private javax.swing.JLabel dummyFilePathLabel;
    private javax.swing.JTextField dummyFilePathTextField;
    private javax.swing.JLabel dummyKeyLabel;
    private javax.swing.JTextField dummyKeyTextField;
    private javax.swing.JComboBox encryptionComboBox;
    private javax.swing.JLabel encryptionPPLabel;
    private javax.swing.JMenuBar fcfpMenuBar;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JComboBox integrityComboBox;
    private javax.swing.JLabel integrityPPLabel;
    private javax.swing.JLabel keyLabel;
    private javax.swing.JPanel keyPanel;
    private javax.swing.JPasswordField keyTextField;
    private javax.swing.JLabel newFileNameLabel;
    private javax.swing.JTextField newFilenameTextField;
    private javax.swing.JButton originalFilePathButton;
    private javax.swing.JLabel originalFilePathLabel;
    private javax.swing.JTextField originalFilePathTextField;
    private javax.swing.JButton pngFilePathButton;
    private javax.swing.JLabel pngFilePathLabel;
    private javax.swing.JTextField pngFilePathTextField;
    private javax.swing.JPanel ppPanel;
    private javax.swing.JButton reloadPPsButton;
    private javax.swing.JPanel steganographyPanel;
    private javax.swing.JRadioButton steganographyRadioButton;
    private javax.swing.JRadioButton useRealNameRadioButton;
    // End of variables declaration//GEN-END:variables
}
