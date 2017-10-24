package br.ol.hexeditor.view;

import br.ol.hexeditor.model.HexEditor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 *
 * @author leonardo
 */
public class ViewData extends JPanel implements KeyListener {
    
    private HexEditor model;
    private List<ViewDataListener> listeners = new ArrayList<ViewDataListener>();
    
    private final Font font;
    private int fontWidth;
    private int fontHeight;
    private int fontSpacing;
    private int currentRows;
    
    private final Point mousePosition = new Point();
    private final Rectangle selectedByteRectangle = new Rectangle();
    private Integer selectedBytePosition;
    
    private static final String VALID_CHARS = "0123456789ABCDEF";
    
    public ViewData() {
        setBackground(Color.WHITE);
        font = new Font("Courier New", Font.PLAIN, 20);
        addKeyListener(this);
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }
    
    public HexEditor getModel() {
        return model;
    }

    public void setModel(HexEditor model) {
        this.model = model;
    }

    public Integer getSelectedBytePosition() {
        return selectedBytePosition;
    }

    public void addListener(ViewDataListener listener) {
        listeners.add(listener);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (model == null) {
            return;
        }
        
        g.setFont(font);
        
        FontMetrics fm = g.getFontMetrics();
        fontWidth = fm.charWidth('X');
        fontHeight = fm.getHeight();
        fontSpacing = (fontHeight - fm.getAscent()) / 2;
        
        JViewport viewport = (JViewport) getParent();
        JScrollPane scrollPane = (JScrollPane) viewport.getParent();
        Rectangle viewRect = viewport.getViewRect();

        if (currentRows != model.getRows()) {
            currentRows = model.getRows();
            setPreferredSize(new Dimension(getPreferredSize().width, (currentRows + 1) * fontHeight));
            scrollPane.updateUI();
        }
                
        //int r = model.getPosition() / model.getCols();
        //int c = model.getPosition() % model.getCols();
        //Rectangle rect = new Rectangle(fontWidth * 6 + c * fontWidth * 3 - viewRect.x, r * fontHeight + fontSpacing - viewRect.y, fontWidth * 2, fontHeight);
        //scrollPane.getViewport().scrollRectToVisible(rect);
        
        int firstRow = (int) (viewRect.getY() / fontHeight);
        int lastRow = (int) Math.ceil((viewRect.getY() + viewRect.getHeight()) / fontHeight);
        if (lastRow > model.getRows()) {
            lastRow = model.getRows();
        }
        
        //System.out.println("first row: "+ firstRow + " / last row: " + lastRow);

        //g.setColor(Color.WHITE);
        //g.fillRect(viewRect.x, viewRect.y, viewRect.width, viewRect.height);
        
        selectedBytePosition = null;
        
        outer:
        for (int row = firstRow; row < lastRow; row++) {
            g.setColor(Color.BLACK);
            g.drawString("" + model.getStartAddress(row), 0, (row + 1) * fontHeight);
            for (int col = 0; col < model.getCols(); col++) {
                int data = model.getData(col, row);
                if (data < 0) {
                    break outer;
                }
                String hexData = "00" + Integer.toHexString(data).toUpperCase();
                hexData = hexData.substring(hexData.length() - 2, hexData.length());
                selectedByteRectangle.setBounds(fontWidth * 6 + col * fontWidth * 3, row * fontHeight + fontSpacing, fontWidth * 2, fontHeight);
                if (model.getPosition() == model.getPosition(col, row)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(selectedByteRectangle.x, selectedByteRectangle.y, selectedByteRectangle.width, selectedByteRectangle.height);
                    g.setColor(Color.BLUE);
                    g.fillRect(fontWidth * 6 + col * fontWidth * 3 + fontWidth * model.getNibblePosition(), (row + 1) * fontHeight + fontSpacing, fontWidth, 3);
                    g.setColor(Color.WHITE);
                }
                else {
                    g.setColor(Color.BLACK);
                }

                if (model.getPosition(col, row) == model.getDataList().size() - 1) {
                    g.setColor(Color.RED);
                    g.fillRect(selectedByteRectangle.x, selectedByteRectangle.y, selectedByteRectangle.width, selectedByteRectangle.height);
                    g.setColor(Color.WHITE);
                }
                
                g.drawString("" + hexData, fontWidth * 6 + col * fontWidth * 3, (row + 1) * fontHeight);
                if (selectedByteRectangle.contains(mousePosition)) {
                    g.setColor(Color.GRAY);
                    g.drawRect(selectedByteRectangle.x, selectedByteRectangle.y, selectedByteRectangle.width, selectedByteRectangle.height);
                    selectedBytePosition = model.getPosition(col, row);
                }
            }
        }
        
    }

    // --- KeyListener implementation ---
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                model.move(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                model.move(1, 0);
                break;
            case KeyEvent.VK_UP:
                model.move(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                model.move(0, 1);
                break;
            case KeyEvent.VK_BACK_SPACE:
                model.move(-1, 0);
                model.move(-1, 0);
                model.deleteData();
                break;
            case KeyEvent.VK_DELETE:
                model.deleteData();
                break;
            case KeyEvent.VK_INSERT:
                if (e.isControlDown()) {
                    model.insertData(0);
                }
                else {
                    model.setInsertMode(!model.isInsertMode());
                }
                break;
            case KeyEvent.VK_HOME:
                if (e.isControlDown()) {
                    String startAddressStr = JOptionPane.showInputDialog(this, "Change start address:", 256);
                    try {
                        int startAddressInt = Integer.parseInt(startAddressStr);
                        model.setStartAddressOffset(startAddressInt);
                    }
                    catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid value !", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            default:
                String charStr = ("" + e.getKeyChar()).toUpperCase();
                if (VALID_CHARS.contains(charStr)) {
                    model.setData(charStr);
                }
        }
        
        JViewport viewport = (JViewport) getParent();
        JScrollPane scrollPane = (JScrollPane) viewport.getParent();
        Rectangle viewRect = viewport.getViewRect();
        
        int r = model.getPosition() / model.getCols();
        int c = model.getPosition() % model.getCols();
        Rectangle rect = new Rectangle(fontWidth * 6 + c * fontWidth * 3 - viewRect.x, r * fontHeight + fontSpacing - viewRect.y, fontWidth * 2, fontHeight + 3);
        scrollPane.getViewport().scrollRectToVisible(rect);
        
        currentRows = model.getRows();
        setPreferredSize(new Dimension(getPreferredSize().width, (currentRows + 1) * fontHeight));
        repaint();
        invalidate();
        updateUI();
        fireUpdateUI();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePosition.x = e.getX();
            mousePosition.y = e.getY();
            repaint();
            fireUpdateUI();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (selectedBytePosition != null) {
                model.setPosition(selectedBytePosition);
            }
            repaint();
            fireUpdateUI();
        }
        
    }
    
    // ---
    
    public void load() throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getAbsolutePath();
            model.loadData(fileName);
        }
        else {
            return;
        }
        
        currentRows = model.getRows();
        setPreferredSize(new Dimension(getPreferredSize().width, (currentRows + 1) * fontHeight));
        repaint();
        invalidate();
        updateUI();
    }

    public void save() throws Exception {
        if (model.getFileName() == null) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                model.setFileName(fileName);
            }
            else {
                return;
            }
        }
        
        model.saveData();
        
        currentRows = model.getRows();
        setPreferredSize(new Dimension(getPreferredSize().width, (currentRows + 1) * fontHeight));
        repaint();
        invalidate();
        updateUI();
    }
    
    // --- listener ---
    
    private void fireUpdateUI() {
        for (ViewDataListener listener : listeners) {
            listener.updateUI();
        }
    }
    
}
