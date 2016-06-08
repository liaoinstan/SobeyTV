package com.sobey.common.utils;

public class NumUtil {
	public static void main(String[] args)
    {  
          
        int ri = ((Double)(Math.random()*10)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*100)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*1000)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*10000)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*100000)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*1000000)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*10000000)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*100000000)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*1000000000)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
          
        ri = ((Double)(Math.random()*10000000000l)).intValue();
        System.out.println(ri+" : "+intToZH(ri));
    }  
      
    /** 
     * 将数字转换成中文数字 
     * @author Prosper 
     * 
     */  
    public static String intToZH(int i)
    {  
        String[] zh = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};
          
        String str = "";
        StringBuffer sb = new StringBuffer(String.valueOf(i));
        sb = sb.reverse();  
        int r = 0;  
        int l = 0;  
        for (int j = 0; j < sb.length(); j++)  
        {  
            /** 
             * 当前数字 
             */  
            r = Integer.valueOf(sb.substring(j, j+1));
              
            if (j != 0)  
                /** 
                 * 上一个数字 
                 */  
                l = Integer.valueOf(sb.substring(j-1, j));
              
            if (j == 0)  
            {  
                if (r != 0 || sb.length() == 1)  
                    str = zh[r];  
                continue;  
            }  
              
            if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9)  
            {  
                if (r != 0)  
                    str = zh[r] + unit[j] + str;  
                else if (l != 0)  
                    str = zh[r] + str;  
                continue;  
            }  
              
            if (j == 4 || j == 8)  
            {  
                str =  unit[j] + str;  
                if ((l != 0 && r == 0) || r != 0)  
                    str = zh[r] + str;  
                continue;  
            }  
        }  
        return str;  
    }
    public static String intToZH_forWeek(int i){
    	String ret = intToZH(i);
    	if ("七".equals(ret)) {
			ret = "日";
		}
    	return ret;
    }
    public static String NumberFormat(float f, int m){
    	return String.format("%."+m+"f",f);
    }
    public static float NumberFormatFloat(float f,int m){
    	String strfloat = NumberFormat(f,m);
    	return Float.parseFloat(strfloat);
    }
}
