/*
 */
package com.chaudhuri.plistcheck;

import com.chaudhuri.plist.PlistLexer;
import com.chaudhuri.plist.PlistParser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class PlistTest extends javax.swing.JFrame {
    private static final Logger log = LogManager.getLogger(PlistTest.class);

    private Timer watcher;
    private File watchedFile;
    
    /**
     * Creates new form PlistTest.
     */
    public PlistTest() {
        initComponents();
        
        txtFile.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                triggerValidation();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                triggerValidation();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                triggerValidation();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbFile = new javax.swing.JLabel();
        btFile = new javax.swing.JButton();
        txtFile = new javax.swing.JTextField();
        lbStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);

        lbFile.setText("Plist File");

        btFile.setText("...");
        btFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFileActionPerformed(evt);
            }
        });

        txtLog.setEditable(false);
        txtLog.setColumns(20);
        txtLog.setFont(new java.awt.Font("Liberation Mono", 0, 15)); // NOI18N
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btFile)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbFile)
                    .addComponent(btFile)
                    .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbStatus))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFileActionPerformed
        log.debug("btFileActionPerformed({})", evt);
        
        File current = new File(txtFile.getText());
        JFileChooser jfc = new JFileChooser();
        jfc.setSelectedFile(current);
        
        if (jfc.showDialog(this, "Validate") == JFileChooser.APPROVE_OPTION) {
            txtFile.setText(jfc.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btFileActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PlistTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            JFrame f = new PlistTest();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
    
    private Timer createWatcher() {
        return new Timer(500, new ActionListener() {
                    
                    private long lastChecked = 0;
                    
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        if (watchedFile.lastModified() > lastChecked) {
                            lastChecked = watchedFile.lastModified();
                            validateFile();
                        }
                    }
                });
    }
    
    private void triggerValidation() {
        File f = new File(txtFile.getText());
        if (f.exists()) {
            //install filewatcher via Swing Timer
            if (watcher == null) {
                watchedFile = f;
                watcher = createWatcher();
                watcher.start();
            } else {
                if (!f.getAbsolutePath().equals(watchedFile.getAbsolutePath()) ) {
                    // need to switch file and immediately trigger validation
                    watcher.stop();
                    watchedFile = f;
                    watcher = createWatcher();
                    watcher.start();
                }
            }
            
            // validate now
            validateFile();
        }
    }

    class MyANTLRErrorListener implements ANTLRErrorListener {
        
        private int errorCount;

        public int getErrorCount() {
            return errorCount;
        }
        
        @Override
        public void syntaxError(Recognizer<?, ?> rcgnzr, Object o, int line, int column, String message, RecognitionException re) {
            errorCount++;
            txtLog.append(String.format("%nline %d:%d %s", line, column, message));
        }

        @Override
        public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean bln, BitSet bitset, ATNConfigSet atncs) {
            errorCount++;
            txtLog.append("\n");
            txtLog.append(parser.toString() + dfa + i + i1 + bln + bitset + atncs);
        }

        @Override
        public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitset, ATNConfigSet atncs) {
            errorCount++;
            txtLog.append("\n");
            txtLog.append(parser.toString() + dfa + i + i1 + bitset + atncs);
        }

        @Override
        public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atncs) {
            errorCount++;
            txtLog.append("\n");
            txtLog.append(parser.toString() + dfa + i + i1 + i2 + atncs);
        }

    }
    
    private void validateFile() {
        File f = new File(txtFile.getText());
        txtLog.setText(new Date().toString());
        txtLog.append("\nValidating "+f.getAbsolutePath());
        
        MyANTLRErrorListener el = new MyANTLRErrorListener();

        try {
            InputStream in = new FileInputStream(f);
            try (ReadableByteChannel channel = Channels.newChannel(in)) {
                CharStream charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8, 4096, CodingErrorAction.REPLACE, f.getAbsolutePath(), -1);
                PlistLexer lexer = new PlistLexer(charStream);
                lexer.addErrorListener(el);

                CommonTokenStream tokenStream = new CommonTokenStream(lexer);
                PlistParser parser = new PlistParser(tokenStream);
                parser.addErrorListener(el);
                parser.parse();
            
                if (0 == el.getErrorCount()) {
                    txtLog.append("\nSuccessfully parsed.");
                } else {
                    txtLog.append(String.format("%nFound %d errors.", el.getErrorCount()));
                    setVisible(true);
                    toFront();
                }
            }
        } catch (Exception e) {
            log.error("Problem", e);
            txtLog.append("\n");
            txtLog.append(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFile;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbFile;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JTextField txtFile;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
