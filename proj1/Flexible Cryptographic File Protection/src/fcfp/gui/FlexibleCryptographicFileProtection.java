package fcfp.gui;

import fcfp.pp.PPEngine;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 2.1
 */
public class FlexibleCryptographicFileProtection extends javax.swing.JFrame {

    private PPEngine ppEngine;

    /**
     * Load all EcryptionPP and IntegrityPP located in the system plugins
     * folder.
     */
    private void loadPPs() {

        cypherAndDecypherButton.setEnabled(false);
        ppEngine.loadPPs();
        encryptionComboBox.setModel(new javax.swing.DefaultComboBoxModel(ppEngine.getEncryptionPPNames().toArray()));
        integrityComboBox.setModel(new javax.swing.DefaultComboBoxModel(ppEngine.getIntegrityPPNames().toArray()));

        if (ppEngine.hasEncryptionPPs() && ppEngine.hasIntegrityPPs()) {
            cypherAndDecypherButton.setEnabled(true);
        } else {
            cypherAndDecypherButton.setEnabled(false);
        }
    }

    /**
     * Creates new form FlexibleCryptographicFileProtection.
     */
    public FlexibleCryptographicFileProtection() {

        initComponents();
        cypherRadioButton.setSelected(true);
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
        cypherModePanel = new javax.swing.JPanel();
        cypherRadioButton = new javax.swing.JRadioButton();
        decypherRadioButton = new javax.swing.JRadioButton();
        cypherAndDecypherButton = new javax.swing.JButton();
        dummyContentPanel = new javax.swing.JPanel();
        dummyFilePathLabel = new javax.swing.JLabel();
        dummyFilePathTextField = new javax.swing.JTextField();
        dummyFilePathButton = new javax.swing.JButton();
        dummyContentRadioButton = new javax.swing.JRadioButton();
        fcfpMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        OptionMenu = new javax.swing.JMenu();

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
                    .addGroup(ppPanelLayout.createSequentialGroup()
                        .addComponent(encryptionPPLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(encryptionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ppPanelLayout.createSequentialGroup()
                        .addComponent(integrityPPLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(integrityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reloadPPsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
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

        keyLabel.setText("Key");

        dummyKeyLabel.setText("Dummy Key");

        javax.swing.GroupLayout keyPanelLayout = new javax.swing.GroupLayout(keyPanel);
        keyPanel.setLayout(keyPanelLayout);
        keyPanelLayout.setHorizontalGroup(
            keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(keyLabel)
                    .addComponent(dummyKeyLabel))
                .addGap(9, 9, 9)
                .addGroup(keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(keyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dummyKeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        useRealNameRadioButton.setText("Use Real Name");
        useRealNameRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useRealNameRadioButtonActionPerformed(evt);
            }
        });

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
                        .addGap(28, 28, 28)
                        .addComponent(pngFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pngFilePathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(steganographyRadioButton))
                .addContainerGap(17, Short.MAX_VALUE))
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

        cypherModePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        buttonGroup.add(cypherRadioButton);
        cypherRadioButton.setText("Cypher");
        cypherRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cypherRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(decypherRadioButton);
        decypherRadioButton.setText("Decypher");
        decypherRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decypherRadioButtonActionPerformed(evt);
            }
        });

        cypherAndDecypherButton.setText("Start");
        cypherAndDecypherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cypherAndDecypherButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cypherModePanelLayout = new javax.swing.GroupLayout(cypherModePanel);
        cypherModePanel.setLayout(cypherModePanelLayout);
        cypherModePanelLayout.setHorizontalGroup(
            cypherModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cypherModePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cypherRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decypherRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cypherAndDecypherButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        cypherModePanelLayout.setVerticalGroup(
            cypherModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cypherModePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cypherModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cypherRadioButton)
                    .addComponent(decypherRadioButton)
                    .addComponent(cypherAndDecypherButton))
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
                .addContainerGap(14, Short.MAX_VALUE))
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

        OptionMenu.setActionCommand("Option");
        OptionMenu.setLabel("Options");
        fcfpMenuBar.add(OptionMenu);

        setJMenuBar(fcfpMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(steganographyPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dummyContentPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cypherModePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(keyPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ppPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addComponent(cypherModePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void originalFilePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_originalFilePathButtonActionPerformed
        // TODO add your handling code here:
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
        // TODO add your handling code here:
    }//GEN-LAST:event_dummyContentRadioButtonActionPerformed

    private void dummyFilePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dummyFilePathButtonActionPerformed
        // TODO add your handling code here:
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
        // TODO add your handling code here:
    }//GEN-LAST:event_steganographyRadioButtonActionPerformed

    private void pngFilePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pngFilePathButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pngFilePathButtonActionPerformed

    private void reloadPPsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadPPsButtonActionPerformed
        ppEngine.loadPPs();
    }//GEN-LAST:event_reloadPPsButtonActionPerformed

    private void cypherRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cypherRadioButtonActionPerformed
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
        encryptionPPLabel.setEnabled(false);
        encryptionComboBox.setEnabled(true);
        integrityPPLabel.setEnabled(true);
        integrityComboBox.setEnabled(true);
        reloadPPsButton.setEnabled(true);
        useRealNameRadioButton.setEnabled(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_cypherRadioButtonActionPerformed

    private void decypherRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decypherRadioButtonActionPerformed
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
        // TODO add your handling code here:
    }//GEN-LAST:event_decypherRadioButtonActionPerformed

    private void cypherAndDecypherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cypherAndDecypherButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cypherAndDecypherButtonActionPerformed

    private void useRealNameRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useRealNameRadioButtonActionPerformed
        if (useRealNameRadioButton.isSelected()) {
            newFileNameLabel.setEnabled(false);
            newFilenameTextField.setEnabled(false);
        } else {
            newFileNameLabel.setEnabled(true);
            newFilenameTextField.setEnabled(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_useRealNameRadioButtonActionPerformed

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
    private javax.swing.JMenu OptionMenu;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton cypherAndDecypherButton;
    private javax.swing.JPanel cypherModePanel;
    private javax.swing.JRadioButton cypherRadioButton;
    private javax.swing.JRadioButton decypherRadioButton;
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
