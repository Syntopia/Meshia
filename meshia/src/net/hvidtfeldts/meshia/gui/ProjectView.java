package net.hvidtfeldts.meshia.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.hvidtfeldts.meshia.engine3d.Object3D;

public class ProjectView extends JList<Object3D> {
    private static final long serialVersionUID = 1L;
    private final JCheckBox checkBox = new JCheckBox();
    
    ProjectView() {
        setCellRenderer(new ListCellRenderer<Object3D>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Object3D> list, Object3D value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                
                if (isSelected) {
                    checkBox.setBackground(list.getSelectionBackground());
                    checkBox.setForeground(list.getSelectionForeground());
                }
                else {
                    checkBox.setBackground(list.getBackground());
                    checkBox.setForeground(list.getForeground());
                }
                
                checkBox.setText(value.toString());
                checkBox.setSelected(value.isVisible());
                return checkBox;
            }
        });
        
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent event)
            {
                
                @SuppressWarnings("unchecked")
                JList<Object3D> list = (JList<Object3D>) event.getSource();
                
                // Get index of item clicked
                int index = list.locationToIndex(event.getPoint());
                Object3D item =
                        list.getModel().getElementAt(index);
                
                item.setVisible(!item.isVisible());
                list.repaint(list.getCellBounds(index, index));
            }
        });
    }
}
