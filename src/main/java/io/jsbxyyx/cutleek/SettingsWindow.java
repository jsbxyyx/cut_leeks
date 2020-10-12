package io.jsbxyyx.cutleek;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import io.jsbxyyx.cutleek.contants.Contants;
import org.jetbrains.annotations.Nls;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SettingsWindow implements Configurable {

    private JPanel panel1;
    private JTextArea taFundCode;
    private JTextArea taStockCode;
    private JCheckBox cbConvertMode;
    private JTextField tfNotifyTime;
    private JTextField tfStockRefreshTime;

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Cut Leek";
    }

    @Override
    public JComponent createComponent() {
        String funds = PropertiesComponent.getInstance().getValue(Contants.KEY_FUNDS);
        String stocks = PropertiesComponent.getInstance().getValue(Contants.KEY_STOCKS);
        String convert_mode = PropertiesComponent.getInstance().getValue(Contants.KEY_CONVERT_MODE);
        String notify_time = PropertiesComponent.getInstance().getValue(Contants.KEY_NOTIFY_TIME);
        String stockRefreshTime = PropertiesComponent.getInstance().getValue(Contants.KEY_STOCK_REFRESH_TIME);
        taFundCode.setText(funds);
        taStockCode.setText(stocks);
        cbConvertMode.setSelected(convert_mode == null ? Contants.VALUE_CONVERT_MODE : Boolean.parseBoolean(convert_mode));
        tfNotifyTime.setText(notify_time);
        tfStockRefreshTime.setText(stockRefreshTime == null ? Contants.VALUE_STOCK_REFRESH_TIME : stockRefreshTime);
        return panel1;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent.getInstance().setValue(Contants.KEY_FUNDS, taFundCode.getText());
        PropertiesComponent.getInstance().setValue(Contants.KEY_STOCKS, taStockCode.getText());
        PropertiesComponent.getInstance().setValue(Contants.KEY_CONVERT_MODE, cbConvertMode.isSelected());
        PropertiesComponent.getInstance().setValue(Contants.KEY_NOTIFY_TIME, tfNotifyTime.getText());
        PropertiesComponent.getInstance().setValue(Contants.KEY_STOCK_REFRESH_TIME, tfStockRefreshTime.getText());
    }

}
