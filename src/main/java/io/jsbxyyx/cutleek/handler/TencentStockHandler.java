package io.jsbxyyx.cutleek.handler;

import io.jsbxyyx.cutleek.domain.Stock;
import io.jsbxyyx.cutleek.util.HttpClient;
import io.jsbxyyx.cutleek.util.LogUtil;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TencentStockHandler extends StockRefreshHandler {

    private List<String> codes = new ArrayList<>();

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
    public void handle(List<String> code) {

        LogUtil.info("Cut Leek 更新股票编码数据.");
        if (code.isEmpty()) {
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
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        clear();
        codes.clear();
        codes.addAll(code);
        //排序，按加入顺序
        for (String s : codes) {
            updateData(new Stock(s));
        }
        worker.start();

    }

    private void stepAction() {
        if (codes.isEmpty()) {
            return;
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < codes.size(); i++) {
            stringBuffer.append(codes.get(i));
            if (i < codes.size() - 1) {
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
            bean.setNow(values[3]);
            bean.setChange(values[31]);
            bean.setChangePercent(values[32]);
            bean.setTime(values[30]);
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
