package io.jsbxyyx.cutleek;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

public class SettingsWindow implements Configurable {

    private JPanel panel1;
    private JLabel label;
    private JTextArea textArea1;
    private JTextArea textArea2;

    private String orgin;

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Cut Leek";
    }

    @Override
    public JComponent createComponent() {
        String value = PropertiesComponent.getInstance().getValue("key_funds");
        String value_stock = PropertiesComponent.getInstance().getValue("key_stocks");
        textArea1.setText(value);
        textArea2.setText(value_stock);
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
    }

}
