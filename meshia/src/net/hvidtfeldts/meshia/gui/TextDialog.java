package net.hvidtfeldts.meshia.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class TextDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private final JPanel contentPanel = new JPanel();
    private JTextField gridSizeTextField;
    
    public enum CloseAction {
        OK, CANCEL
    }
    
    private CloseAction closeAction = CloseAction.CANCEL;
    
    private JTextArea codeTextArea;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            TextDialog dialog = new TextDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create the dialog.
     */
    public TextDialog() {
        setMinimumSize(new Dimension(400, 200));
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        createGridSizePanel();
        createCodePanel();
        createButtonPanel();
        
    }
    
    private void createButtonPanel() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCloseAction(CloseAction.OK);
                dispose();
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCloseAction(CloseAction.CANCEL);
                dispose();
            }
        });
        
    }
    
    private void createCodePanel() {
        JPanel panel_1 = new JPanel();
        panel_1.setPreferredSize(new Dimension(10, 800));
        contentPanel.add(panel_1);
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
        
        JLabel lblCode = new JLabel("Code");
        lblCode.setVerticalTextPosition(SwingConstants.TOP);
        lblCode.setVerticalAlignment(SwingConstants.TOP);
        panel_1.add(lblCode);
        
        Component horizontalStrut = Box.createHorizontalStrut(20);
        panel_1.add(horizontalStrut);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel_1.add(scrollPane);
        
        codeTextArea = new JTextArea();
        scrollPane.setViewportView(codeTextArea);
        
    }
    
    private void createGridSizePanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(10, 40));
        contentPanel.add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        JLabel lblGridSize = new JLabel("Grid size");
        panel.add(lblGridSize);
        
        gridSizeTextField = new JTextField();
        panel.add(gridSizeTextField);
        gridSizeTextField.setText("34");
        gridSizeTextField.setColumns(10);
        
    }
    
    public CloseAction getCloseAction() {
        return closeAction;
    }
    
    private void setCloseAction(CloseAction closeAction) {
        this.closeAction = closeAction;
    }
    
    public int getGridSize() {
        int i = Integer.parseInt(gridSizeTextField.getText());
        return i;
    }
    
    public String getCode() {
        return codeTextArea.getText();
    }
    
}
