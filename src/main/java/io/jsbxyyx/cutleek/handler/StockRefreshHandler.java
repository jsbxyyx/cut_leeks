package io.jsbxyyx.cutleek.handler;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ui.JBColor;
import io.jsbxyyx.cutleek.domain.Stock;
import org.apache.commons.lang3.StringUtils;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public abstract class StockRefreshHandler {

    private ArrayList<Stock> data = new ArrayList<>();
    private JTable table;
    private int[] sizes = new int[]{0, 0, 0, 0, 0};

    public StockRefreshHandler(JTable table) {
        this.table = table;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public abstract void setStocks(List<String> stocks);
    public abstract void setStockRefreshTime(int stockRefreshTime);
    public abstract void handle();

    /**
     * 更新全部数据
     */
    public void updateUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                recordTableSize();
                String[] columnNames = {"股票名称", "当前价/成本/昨收/累计盈亏率", "涨跌", "涨跌幅", "更新时间"};
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
        table.getColumn("涨跌幅").setCellRenderer(new DefaultTableCellRenderer() {
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

    protected void updateData(Stock bean) {
        int index = data.indexOf(bean);
        if (index >= 0) {
            data.set(index, bean);
        } else {
            data.add(bean);
        }
    }

    private Object[][] convertData() {
        Object[][] temp = new Object[data.size()][5];
        for (int i = 0; i < data.size(); i++) {
            Stock stock = data.get(i);
            String timeStr = getTime(stock.getTime());
            String changeStr = "--";
            String changePercentStr = "--";
            if (stock.getChange() != null) {
                changeStr = stock.getChange().startsWith("-") ? stock.getChange() : "+" + stock.getChange();
            }
            if (stock.getChangePercent() != null) {
                changePercentStr = stock.getChangePercent().startsWith("-") ? stock.getChangePercent() : "+" + stock.getChangePercent();
            }
            temp[i] = new Object[]{
                    stock.getName() + " (" + stock.getCode() + ")",
                    stock.getNow() + "/" +
                            (StringUtils.isBlank(stock.getBuyPrice()) ? "--" : stock.getBuyPrice()) + "/" +
                            (StringUtils.isBlank(stock.getClose()) ? "--" : stock.getClose()) + "/" +
                            (StringUtils.isBlank(stock.getMyChange()) ? "--" : stock.getMyChange()),
                    changeStr,
                    changePercentStr + "%",
                    timeStr
            };
        }
        return temp;
    }

    private String getTime(String time) {
        time = time.length() > 14 ? time.substring(time.length() - 8) : time.substring(time.length() - 6);
        if (time.length() == 8) {
            return time;
        }
        return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4);
    }

    protected void clear() {
        data.clear();
    }

}