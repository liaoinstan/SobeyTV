package com.sobey.tvcust.utils;

import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.entity.CountEntity;
import com.sobey.tvcust.entity.SBWarningCount;
import com.sobey.tvcust.entity.TVStation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AppUtils {
    public static String getStationCodeStr(List<TVStation> stations) {
        if (stations == null) {
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

//    public static List<CountEntity> getCountListWarningList(List<SBWarningCount> list) {
//        ArrayList<CountEntity> counts = new ArrayList<>();
//        int i = 0;
//        for (SBWarningCount warningCount : list) {
//            CountEntity countEntity = new CountEntity();
//            String name;
//            if (!StrUtils.isEmpty(warningCount.getGroupName())) {
//                name = warningCount.getGroupName();
//            } else {
//                if (!StrUtils.isEmpty(warningCount.getGroupCode())) {
//                    name = warningCount.getGroupCode();
//                } else {
//                    name = "未分组";
//                }
//            }
//            countEntity.setName(name);
//            countEntity.setValue(warningCount.getCount());
//            countEntity.setColor(colors.get(i % colors.size()));
//            counts.add(countEntity);
//            i++;
//        }
//        return counts;
//    }
}
