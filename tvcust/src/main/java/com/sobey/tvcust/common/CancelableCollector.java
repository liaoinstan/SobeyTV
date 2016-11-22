package com.sobey.tvcust.common;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class CancelableCollector {
    private static List<Callback.Cancelable> canceles = new ArrayList<>();

    public static void add(Callback.Cancelable cancelable) {
        canceles.add(cancelable);
    }

    public static void CancleAll() {
        for (Callback.Cancelable cancelable : canceles) {
            if (cancelable != null && !cancelable.isCancelled()) {
                cancelable.cancel();
            }
        }
        canceles.clear();
    }
}
