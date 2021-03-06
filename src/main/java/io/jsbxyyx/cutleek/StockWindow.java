package io.jsbxyyx.cutleek;

import com.intellij.ide.util.PropertiesComponent;
import io.jsbxyyx.cutleek.contants.Contants;
import io.jsbxyyx.cutleek.handler.StockRefreshHandler;
import io.jsbxyyx.cutleek.handler.TencentStockHandler;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.util.ArrayList;
import java.util.List;

public class StockWindow {

    private JPanel panel1;
    private JTable table1;
    private JLabel label;

    StockRefreshHandler handler;

    public JPanel getPanel1() {
        return panel1;
    }

    public StockWindow() {
        handler = new TencentStockHandler(table1, label);
    }

    public void onInit() {
        List<String> stocks = loadStocks();
        int stockRefreshTime = loadStockRefreshTime();
        handler.setStocks(stocks);
        handler.setStockRefreshTime(stockRefreshTime);
        handler.handle();
    }

    private List<String> loadStocks() {
        ArrayList<String> temp = new ArrayList<>();
        String value = PropertiesComponent.getInstance().getValue(Contants.KEY_STOCKS);
//        String value = "sh600519,sz000001";
        if (value == null) {
            return temp;
        }
        String[] codes = value.split("[,，]");
        for (String code : codes) {
            if (!code.isEmpty()) {
                temp.add(code);
            }
        }
        return temp;
    }

    private int loadStockRefreshTime() {
        String value = PropertiesComponent.getInstance().getValue("key_stock_refresh_time");
        if (value == null) {
            return 10 * 1000;
        }
        return Integer.parseInt(value);
    }

}
