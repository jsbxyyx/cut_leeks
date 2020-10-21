package io.jsbxyyx.cutleek;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.jsbxyyx.cutleek.contants.Contants;
import io.jsbxyyx.cutleek.handler.FundRefreshHandler;
import io.jsbxyyx.cutleek.handler.TianTianFundHandler;
import io.jsbxyyx.cutleek.util.LogUtil;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FundWindow implements ToolWindowFactory {

    private JPanel mPanel;
    private JTable table1;
    private JButton refreshButton;

    FundRefreshHandler fundRefreshHandler;

    private StockWindow stockWindow = new StockWindow();

    private NotifyHandler notifyHandler = new NotifyHandler();

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mPanel, "Fund", false);

        Content content_stock = contentFactory.createContent(stockWindow.getPanel1(), "Stock", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().addContent(content_stock);
        LogUtil.setProject(project);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onInit();
                stockWindow.onInit();
                notifyHandler.onInit();
            }
        });

    }

    @Override
    public void init(ToolWindow window) {
        fundRefreshHandler = new TianTianFundHandler(table1);
        onInit();
        stockWindow.onInit();
        notifyHandler.onInit();
    }

    public void onInit() {
        List<String> fundCodes = loadFunds();
        fundRefreshHandler.setFunds(fundCodes);
        fundRefreshHandler.handle();
    }

    private List<String> loadFunds() {
        List<String> temp = new ArrayList<>();
        String value = PropertiesComponent.getInstance().getValue(Contants.KEY_FUNDS);
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


    @Override
    public boolean shouldBeAvailable(Project project) {
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return true;
    }

}
