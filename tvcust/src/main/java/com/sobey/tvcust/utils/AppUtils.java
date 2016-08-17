package com.sobey.tvcust.utils;

import com.sobey.common.utils.DateUtils;
import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.entity.CountEntity;
import com.sobey.tvcust.entity.SBCountWarningPojo;
import com.sobey.tvcust.entity.SBCountWarningStates;
import com.sobey.tvcust.entity.SBWarningCount;
import com.sobey.tvcust.entity.TVStation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AppUtils {
    public static String getStationCodeStr(List<TVStation> stations) {
        if (stations == null || stations.size() == 0) {
            return "";
        } else if (stations.size() == 1) {
            return stations.get(0).getStationCode();
        } else {
            String ret = "";
            for (TVStation station : stations) {
                ret += station.getStationCode() + "|";
            }
            ret = ret.substring(0, ret.length() - 1).replaceAll("\\|", "%7C");
            return ret;
        }
    }

    /**
     * 是否需要检查弹出签到
     * false 不需要
     * true 需要
     */
    public static boolean getNeedCheckSign() {
        String token = AppData.App.getToken();
        HashMap<String, Long> map = AppData.Cache.getSignList();
        if (map!=null && map.containsKey(token)) {
            Long time = map.get(token);
            if (DateUtils.isSameDate(new Date().getTime(), time)) {
                //和存储的日期是同一天返回true
                return false;
            } else {
                //和存储的日期不是同一天返回false
                return true;
            }
        } else {
            //不存在该用户，返回false
            return true;
        }
    }

    public static void saveNeedCheckSign() {
        String token = AppData.App.getToken();
        HashMap<String, Long> map = AppData.Cache.getSignList();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(token, new Date().getTime());
        AppData.Cache.saveSignList(map);
    }


    public static List<CountEntity> getWarningList(List<SBCountWarningStates> statsList,List<Integer> colors){
        List<SBWarningCount> warningCounts = new ArrayList<>();

        for (SBCountWarningStates states : statsList){
            warningCounts.addAll(states.getKitGroupDetail());
        }

        List<CountEntity> counts = getCountListWarningList(warningCounts,colors);

        return counts;
    }

    private static List<CountEntity> getCountListWarningList(List<SBWarningCount> list,List<Integer> colors) {
        if (list==null || list.size()==0){
            return null;
        }
        ArrayList<CountEntity> counts = new ArrayList<>();
        int i = 0;
        for (SBWarningCount warningCount : list) {
            CountEntity countEntity = new CountEntity();
            String name;
            if (!StrUtils.isEmpty(warningCount.getGroupName())) {
                name = warningCount.getGroupName();
            } else {
                if (!StrUtils.isEmpty(warningCount.getGroupCode())) {
                    name = warningCount.getGroupCode();
                } else {
                    name = "未分组";
                }
            }
            countEntity.setName(name);
            countEntity.setValue(warningCount.getCount());
            countEntity.setColor(colors.get(i % colors.size()));
            counts.add(countEntity);
            i++;
        }
        return counts;
    }
}
