package io.jsbxyyx.cutleek;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

public class SettingsWindow implements Configurable {

    private JPanel panel1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JCheckBox checkBox1;
    private JTextField textField1;

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Cut Leek";
    }

    @Override
    public JComponent createComponent() {
        String funds = PropertiesComponent.getInstance().getValue("key_funds");
        String stocks = PropertiesComponent.getInstance().getValue("key_stocks");
        String convert_mode = PropertiesComponent.getInstance().getValue("key_convert_mode");
        String notify_time = PropertiesComponent.getInstance().getValue("key_notify_time");
        textArea1.setText(funds);
        textArea2.setText(stocks);
        checkBox1.setSelected(convert_mode == null ? false : Boolean.parseBoolean(convert_mode));
        textField1.setText(notify_time);
        return panel1;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent.getInstance().setValue("key_funds", textArea1.getText());
        PropertiesComponent.getInstance().setValue("key_stocks", textArea2.getText());
        PropertiesComponent.getInstance().setValue("key_convert_mode", checkBox1.isSelected());
        PropertiesComponent.getInstance().setValue("key_notify_time", textField1.getText());
    }

}
