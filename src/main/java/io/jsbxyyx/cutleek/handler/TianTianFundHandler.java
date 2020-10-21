package io.jsbxyyx.cutleek.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsbxyyx.cutleek.domain.Fund;
import io.jsbxyyx.cutleek.domain.FundIntro;
import io.jsbxyyx.cutleek.domain.Result;
import io.jsbxyyx.cutleek.util.HttpClient;
import io.jsbxyyx.cutleek.util.LogUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.JTable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TianTianFundHandler extends FundRefreshHandler {

    private static Gson gson = new Gson();
    private Map<String, String> funds = new LinkedHashMap<>();
    private int fundRefreshTime = 60 * 1000;

    private Thread worker;

    @Override
    public void setFunds(List<String> funds) {
        this.funds.clear();
        if (funds != null && !funds.isEmpty()) {
            for (String fund : funds) {
                String[] splits = fund.split("\\|");
                if (splits.length >= 2) {
                    this.funds.put(splits[0], splits[1]);
                } else {
                    this.funds.put(splits[0], "");
                }
            }
        }

    }

    @Override
    public void setFundRefreshTime(int fundRefreshTime) {
        this.fundRefreshTime = fundRefreshTime;
    }

    public TianTianFundHandler(JTable table) {
        super(table);
    }

    @Override
    public void handle() {
        LogUtil.info("Cut Leek 更新基金编码数据.");
        if (worker != null) {
            worker.interrupt();
            worker.stop();
        }
        if (funds.isEmpty()) {
            return;
        }
        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (worker != null && worker.hashCode() == Thread.currentThread().hashCode() && !worker.isInterrupted()) {
                    stepAction();
                    try {
                        Thread.sleep(fundRefreshTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        super.clear();
        //排序，按加入顺序
        for (String s : funds.keySet()) {
            updateData(new Fund(s));
        }
        worker.start();
    }

    private void stepAction() {
        for (String s : funds.keySet()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        long t = System.currentTimeMillis();
                        String url0 = String.format("https://fundmobapi.eastmoney.com/FundMApi/FundBaseTypeInformation.ashx?FCODE=%s&deviceid=Wap&plat=Wap&product=EFund&version=2.0.0&Uid=&_=%s", s, t);
                        String json0 = HttpClient.getHttpClient().get(url0);
                        Result<FundIntro> result0 = gson.fromJson(json0, new TypeToken<Result<FundIntro>>(){}.getType());

                        String url = String.format("https://fundmobapi.eastmoney.com/FundMApi/FundValuationDetail.ashx?FCODE=%s&deviceid=Wap&plat=Wap&product=EFund&version=2.0.0&Uid=&_=%s", s, t);
                        String json = HttpClient.getHttpClient().get(url);
                        Result<List<Fund>> result = gson.fromJson(json, new TypeToken<Result<List<Fund>>>(){}.getType());

                        Fund fund = result.getDatas().get(0);
                        fund.setFundName(result0.getDatas().getShortname());
                        String buyPrice = funds.get(fund.getFundCode());
                        fund.setGsz(fund.getGsz() + (StringUtils.isBlank(buyPrice) ? "" : "/" + buyPrice));
                        updateData(fund);
                        updateUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}