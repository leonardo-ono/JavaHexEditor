package br.ol.hexeditor.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class HexEditor {
    
    private String fileName = null;
    private List<Integer> data = new ArrayList<Integer>();
    private int position;
    private int nibblePosition; // 0 or 1
    private int cols = 16;
    private int startAddressOffset = 0x100;
    private boolean insertMode = false;
    
    public HexEditor() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Integer> getDataList() {
        return data;
    }

    public int getPosition(int col, int row) {
        return row * cols + col;
    }

    public boolean isLastPosition() {
        return position == data.size() - 1;
    }
    
    public int getNibblePosition() {
        return nibblePosition;
    }
    
    public void move(int dx, int dy) {
        if (dx > 0 && nibblePosition == 0) {
            nibblePosition++;
        }
        else if (dx > 0 && nibblePosition == 1 && setPosition(position + dx + dy * cols)) {
            nibblePosition = 0;
        }
        else if (dx < 0 && nibblePosition == 1) {
            nibblePosition--;
        }
        else if (dx < 0 && nibblePosition == 0 && setPosition(position + dx + dy * cols)) {
            nibblePosition = 1;
        }
        else {
            setPosition(position + dx + dy * cols);
        }
    }
    
    public int getData(int col, int row) {
        int index = getPosition(col, row);
        if (index >= data.size()) {
            return -1;
        }
        return data.get(index);
    }
    
    public void setData(int col, int row, int newValue) {
        int index = getPosition(col, row);
        data.set(index, newValue);
    }

    public void insertData(int newValue) {
        data.add(position, newValue);
    }

    public void setData(String newNibbleValue) { // newNibbleValue = 0~9 or A~F
        int row = position / cols;
        int col = position % cols;
        if (nibblePosition == 0 && insertMode) {
            insertData(0);
        }
        int currentValue = getData(col, row);
        int newValue = currentValue;
        if (nibblePosition == 0) {
            newValue = (currentValue & 0x0F) + (Integer.parseInt(newNibbleValue, 16) << 4);
        }
        else if (nibblePosition == 1) {
            newValue = (currentValue & 0xF0) + Integer.parseInt(newNibbleValue, 16);
            
        }
        int index = getPosition(col, row);
        data.set(index, newValue);
        if (index == data.size() - 1) {
            data.add(0);
        }
        move(1, 0);
    }
    
    public void deleteData() {
        if (position == data.size() - 1) {
            return;
        }
        data.remove(position);
    }
    
    public int getPosition() {
        return position;
    }

    public boolean setPosition(int position) {
        if (position < 0 || position > data.size() - 1) {
            return false;
        }
        this.position = position;
        return true;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        int rows = 0;
        if (data.size() > 0 && cols > 0) {
            rows = (int) Math.ceil(data.size() / (double) cols);
        }
        return rows;
    }

    public int getStartAddressOffset() {
        return startAddressOffset;
    }

    public void setStartAddressOffset(int startAddressOffset) {
        this.startAddressOffset = startAddressOffset;
    }
    
    public String getStartAddress(int row) {
        String address = "00000" + Integer.toHexString(row * cols + startAddressOffset).toUpperCase();
        address = address.substring(address.length() - 5, address.length());
        return address;
    }

    public boolean isInsertMode() {
        return insertMode;
    }

    public void setInsertMode(boolean insertMode) {
        this.insertMode = insertMode;
    }
    
    public void createNewData() throws Exception {
        data.clear();
        data.add(0);
        fileName = null;
        position = 0;
    }

    // TODO: load data in a faster way ?
    public void loadData(String fileName) throws Exception {
        data.clear();
        FileReader fr = new FileReader(fileName);
        int c = -1;
        while ((c = fr.read()) >= 0) {
            data.add(c);
        }
        fr.close();
        data.add(0);
        position = 0;
        this.fileName = fileName;
    }

    // TODO: save content in a faster way ?
    public void saveData() throws Exception {
        FileWriter fw = new FileWriter(fileName);
        for (int i = 0; i < data.size() - 1; i++) {
            fw.write(data.get(i));
        }
        fw.close();
    }

}
