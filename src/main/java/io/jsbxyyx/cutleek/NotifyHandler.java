package io.jsbxyyx.cutleek;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.NotificationDisplayType;
import io.jsbxyyx.cutleek.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jsbxyyx
 */
public class NotifyHandler {

    private ScheduledExecutorService service;

    public void onInit() {
        if (service != null) {
            service.shutdownNow();
        }
        service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String notify_time = PropertiesComponent.getInstance().getValue("key_notify_time");
                if (StringUtils.isBlank(notify_time)) {
                    return ;
                }
                try {
                    String format = DateFormatUtils.format(new Date(), "HH:mm");
                    if (Objects.equals(format, notify_time)) {
                        LogUtil.info("mai mai mai mai mai mai mai mai mai.", NotificationDisplayType.BALLOON);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }
}
