package io.jsbxyyx.cutleek.handler;

import io.jsbxyyx.cutleek.domain.Stock;
import io.jsbxyyx.cutleek.util.HttpClient;
import io.jsbxyyx.cutleek.util.LogUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TencentStockHandler extends StockRefreshHandler {

    private Map<String, String> stocks = new LinkedHashMap<>();
    private int stockRefreshTime;

    private Thread worker;
    private JLabel label;

    public TencentStockHandler(JTable table) {
        super(table);
    }

    public TencentStockHandler(JTable table1, JLabel label) {
        super(table1);
        this.label = label;
    }

    @Override
    public void setStocks(List<String> stocks) {
        this.stocks.clear();
        if (stocks != null && !stocks.isEmpty()) {
            for (String stock : stocks) {
                String[] splits = stock.split("\\|");
                if (splits.length >= 2) {
                    this.stocks.put(splits[0], splits[1]);
                } else {
                    this.stocks.put(splits[0], "");
                }
            }
        }

    }

    @Override
    public void setStockRefreshTime(int stockRefreshTime) {
        this.stockRefreshTime = stockRefreshTime;
    }

    @Override
    public void handle() {

        LogUtil.info("Cut Leek 更新股票编码数据.");
        if (stocks.isEmpty()) {
            return;
        }
        if (worker != null) {
            worker.interrupt();
            worker.stop();
        }
        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (worker != null && worker.hashCode() == Thread.currentThread().hashCode() && !worker.isInterrupted()) {
                    stepAction();
                    try {
                        Thread.sleep(stockRefreshTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        super.clear();
        //排序，按加入顺序
        for (String s : stocks.keySet()) {
            updateData(new Stock(s));
        }
        worker.start();
    }

    private void stepAction() {
        if (stocks.isEmpty()) {
            return;
        }
        StringBuilder stringBuffer = new StringBuilder();
        Set<String> strings = stocks.keySet();
        Iterator<String> iterator = strings.iterator();
        for (int i = 0; i < strings.size(); i++) {
            stringBuffer.append(iterator.next());
            if (i < stocks.size() - 1) {
                stringBuffer.append(',');
            }
        }
        try {
            String result = HttpClient.getHttpClient().get("http://qt.gtimg.cn/q=" + stringBuffer.toString());
            parse(result);
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parse(String result) {
        String[] lines = result.split("\n");
        for (String line : lines) {
            String code = line.substring(line.indexOf("_") + 1, line.indexOf("="));
            String dataStr = line.substring(line.indexOf("=") + 2, line.length() - 2);
            String[] values = dataStr.split("~");
            Stock bean = new Stock(code);
            bean.setName(values[1]);
            String buyPrice = stocks.get(code);
            bean.setBuyPrice(buyPrice);
            bean.setNow(values[3]);
            bean.setClose(values[4]);
            bean.setOpen(values[5]);
            bean.setChange(values[31]);
            bean.setChangePercent(values[32]);
            bean.setTime(values[30]);
            if (StringUtils.isNotBlank(buyPrice) && bean.getNow() != null) {
                Double buyPriceDouble = Double.valueOf(buyPrice);
                Double nowDouble = Double.valueOf(bean.getNow());
                bean.setMyChange(String.format("%.2f%%", (nowDouble - buyPriceDouble) / buyPriceDouble * 100));
            }
            updateData(bean);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                label.setText("最后刷新时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }
        });
    }

}
