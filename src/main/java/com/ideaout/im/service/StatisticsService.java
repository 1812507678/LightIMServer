package com.ideaout.im.service;

import com.ideaout.im.domain.App;

import java.util.List;

public interface StatisticsService {
    Object queryAppStatisticsData(List<App> apps,int userId);
}
