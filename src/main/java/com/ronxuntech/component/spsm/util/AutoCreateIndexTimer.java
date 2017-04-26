package com.ronxuntech.component.spsm.util;

import com.ronxuntech.component.spsm.lucene.LuceneUtil;
import com.ronxuntech.util.PageData;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自动执行创建索引任务
 *
 * @author angrl
 */
public class AutoCreateIndexTimer {
    Timer timer;
    private List<PageData> pagedataList;
    private List<String> fieldNameList;
    private int seconds;

    public AutoCreateIndexTimer() {
        timer = new Timer();
        timer.schedule(new AutoCreateIndexTimerTask(), 3 * 1000, seconds * 1000);
    }

    public AutoCreateIndexTimer(List<PageData> pagedataList, List<String> fieldNameList, int seconds) {
        this.pagedataList = pagedataList;
        this.fieldNameList = fieldNameList;
        timer = new Timer();
        timer.schedule(new AutoCreateIndexTimerTask(), 3 * 1000, seconds * 1000);
    }

    /**
     * 定时任务
     *
     * @author angrl
     */
    class AutoCreateIndexTimerTask extends TimerTask {
        public void run() {
            try {
                LuceneUtil.createIndex(pagedataList, fieldNameList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启定时任务
     *
     * @param pagedataList
     * @param fieldNameList
     * @param seconds
     */
    public static void startTimer(List<PageData> pagedataList, List<String> fieldNameList, int seconds) {
        new AutoCreateIndexTimer(pagedataList, fieldNameList, seconds);
    }


    public List<PageData> getPagedataList() {
        return pagedataList;
    }

    public void setPagedataList(List<PageData> pagedataList) {
        this.pagedataList = pagedataList;
    }

    public List<String> getFieldNameList() {
        return fieldNameList;
    }

    public void setFieldNameList(List<String> fieldNameList) {
        this.fieldNameList = fieldNameList;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

}
