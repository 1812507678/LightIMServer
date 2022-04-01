package com.ideaout.im.serviceImpl;

import com.ideaout.im.bean.StatisticsCount;
import com.ideaout.im.dao.StatisticsMapper;
import com.ideaout.im.domain.App;
import com.ideaout.im.domain.AppExample;
import com.ideaout.im.http.entity.AppStaResult;
import com.ideaout.im.http.param.StaCountParam;
import com.ideaout.im.service.StatisticsService;
import com.ideaout.im.util.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    StatisticsMapper statisticsMapper;

    @Override
    public Object queryAppStatisticsData(List<App> apps,int userId) {
        List<AppStaResult> appStaResultList = new ArrayList<>();

        for (App app:apps){
            appStaResultList.add(new AppStaResult(app.getAppKey(),app.getAppSecret(),app.getState(),app.getName(),app.getTimestamp()));
        }


        StaCountParam param = new StaCountParam();
        param.userId = userId;

        String curDate = FormatUtil.getSpecialFormatTime("yyyy-MM-dd", new Date());  //今天

        Date yesterday = new Date();
        yesterday.setDate(yesterday.getDate()-1);
        String yesterdayDate = FormatUtil.getSpecialFormatTime("yyyy-MM-dd", yesterday); //昨天

        Date monthFirstday = new Date();
        monthFirstday.setDate(1);
        String monthFirstDayDate = FormatUtil.getSpecialFormatTime("yyyy-MM-dd", monthFirstday);  //月初第一天

        //当天
        param.startDate = curDate;
        param.endDate = curDate;
        //statisticsMapper.queryRegisterUserCount(param);
        List<StatisticsCount> dayMsgStatisticsCounts = statisticsMapper.queryMessageCount(param);


        //昨天
       /*  param.startDate = yesterdayDate;
        param.endDate =yesterdayDate;
        statisticsMapper.queryRegisterUserCount(param);
        statisticsMapper.queryMessageCount(param);*/

        //当月
        param.startDate = monthFirstDayDate;
        param.endDate = curDate;
        List<StatisticsCount> monthUserStatisticsCounts = statisticsMapper.queryRegisterUserCount(param);
        List<StatisticsCount> monthMsgStatisticsCounts = statisticsMapper.queryMessageCount(param);

        //所有
        param.startDate = null;
        param.endDate = null;
        List<StatisticsCount> allUserStatisticsCounts = statisticsMapper.queryRegisterUserCount(param);


        for (AppStaResult appStaResult:appStaResultList){
            for (StatisticsCount statisticsCount:dayMsgStatisticsCounts){
                if (appStaResult.appKey.equals(statisticsCount.appKey)){
                    appStaResult.dayMsgCount = statisticsCount.count;
                }
            }

            for (StatisticsCount statisticsCount:monthUserStatisticsCounts){
                if (appStaResult.appKey.equals(statisticsCount.appKey)){
                    appStaResult.monthUserCount = statisticsCount.count;
                }
            }

            for (StatisticsCount statisticsCount:monthMsgStatisticsCounts){
                if (appStaResult.appKey.equals(statisticsCount.appKey)){
                    appStaResult.monthMsgCount = statisticsCount.count;
                }
            }

            for (StatisticsCount statisticsCount:allUserStatisticsCounts){
                if (appStaResult.appKey.equals(statisticsCount.appKey)){
                    appStaResult.allUserCount = statisticsCount.count;
                }
            }
        }

        return appStaResultList;
    }
}
