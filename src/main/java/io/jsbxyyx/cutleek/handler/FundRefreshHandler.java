package io.jsbxyyx.cutleek.handler;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ui.JBColor;
import io.jsbxyyx.cutleek.domain.Fund;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class FundRefreshHandler {

    private ArrayList<Fund> data = new ArrayList<>();
    private JTable table;
    private int[] sizes = new int[]{0, 0, 0, 0};

    public FundRefreshHandler(JTable table) {
        this.table = table;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /**
     * 从网络更新数据
     *
     * @param code
     */
    public abstract void handle(List<String> code);

    /**
     * 更新全部数据
     */
    public void updateUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                recordTableSize();
                String[] columnNames = {"基金名称", "估算净值", "估算涨跌", "更新时间"};
                DefaultTableModel model = new DefaultTableModel(convertData(), columnNames);
                table.setModel(model);
                updateColors();
                resizeTable();

            }
        });
    }

    private void recordTableSize() {
        if (table.getColumnModel().getColumnCount() == 0) {
            return;
        }
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = table.getColumnModel().getColumn(i).getWidth();
        }
    }

    private void resizeTable() {
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] > 0) {
                table.getColumnModel().getColumn(i).setWidth(sizes[i]);
                table.getColumnModel().getColumn(i).setPreferredWidth(sizes[i]);
            }
        }
    }

    private void updateColors() {
        table.getColumn("估算涨跌").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0.0;
                try {
                    String s = value.toString().substring(0, value.toString().length() - 1);
                    temp = Double.parseDouble(s);
                } catch (Exception e) {
                }
                boolean convert_mode = PropertiesComponent.getInstance().getBoolean("key_convert_mode");
                if (!convert_mode) {
                    Color orgin = getForeground();
                    if (temp > 0) {
                        setForeground(JBColor.RED);
                    } else if (temp < 0) {
                        setForeground(JBColor.GREEN);
                    } else if (temp == 0) {
                        setForeground(orgin);
                    }
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }

    protected void updateData(Fund bean) {
        int index = data.indexOf(bean);
        if (index >= 0) {
            data.set(index, bean);
        } else {
            data.add(bean);
        }
    }

    private Object[][] convertData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Object[][] temp = new Object[data.size()][4];
        for (int i = 0; i < data.size(); i++) {
            Fund fund = data.get(i);
            String timeStr = fund.getGztime();

            String today = dateFormat.format(new Date());
            if (timeStr != null && timeStr.startsWith(today)) {
                timeStr = timeStr.substring(timeStr.indexOf(" ") + 1);
            } else if (timeStr == null) {
                timeStr = "--";
            }
            String gszzlStr = "--";
            if (fund.getGszzl() != null) {
                gszzlStr = fund.getGszzl().startsWith("-") ? fund.getGszzl() : "+" + fund.getGszzl();
                gszzlStr = gszzlStr + "%";
            }
            String gsz = fund.getGsz();
            if (gsz == null) {
                gsz = "--";
            }
            temp[i] = new Object[]{fund.getFundName() + "(" + fund.getFundCode() + ")", gsz, gszzlStr, timeStr};
        }
        return temp;
    }

    protected void clear() {
        data.clear();
    }

}
