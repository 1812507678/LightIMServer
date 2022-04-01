package com.ideaout.im.dao;

import com.ideaout.im.bean.StatisticsCount;
import com.ideaout.im.http.param.StaCountParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatisticsMapper {
    List<StatisticsCount> queryRegisterUserCount(@Param("param") StaCountParam param);
    List<StatisticsCount> queryMessageCount(@Param("param") StaCountParam param);


}