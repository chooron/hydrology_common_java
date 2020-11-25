package utils;

import java.util.*;

/**
 * 该工具类用于double类型数组的常规计算
 */
public class ListUtils {

    /**
     * 数组求和
     *
     * @return 求和结果
     */

    /**
     * 数组求和
     *
     * @param list 数组
     * @return 求和结果
     */
    public static double sum(ArrayList<Double> list) {
        double result = 0;
        for (Double aDouble : list) {
            result += aDouble;
        }
        return result;
    }

    public static double getMid(ArrayList<Double> list) {
        double mid;
        Collections.sort(list);
        if (list.size() % 2 == 0) {
            mid = (list.get(list.size() / 2) + list.get(list.size() / 2 + 1)) / 2;
        } else {
            mid = list.get(list.size() / 2 + 1);
        }
        return mid;
    }

    /**
     * 获取数组最大值
     *
     * @param list 数组
     * @return 返回最大值及其坐标
     */
    public static Map<String, Object> getMax(ArrayList<Double> list) {
        double max = -10e5;  //最大值
        int index = 0;
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i)) {
                max = list.get(i);
                index = i;
            }
        }
        map.put("index", index);
        map.put("value", max);
        return map;
    }
    public static Map<String, Integer> getMaxForInt(ArrayList<Integer> list) {
        int max = (int) -10e5;  //最大值
        int index = 0;
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i)) {
                max = list.get(i);
                index = i;
            }
        }
        map.put("index", index);
        map.put("value", max);
        return map;
    }

    /**
     * 获取数组最小值
     *
     * @param list 数组
     * @return 返回最小值及其坐标
     */
    public static Map<String, Object> getMin(ArrayList<Double> list) {
        double min = 10e5;
        int index = 0;
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (min > list.get(i)) {
                min = list.get(i);
                index = i;
            }
        }
        map.put("index", index);
        map.put("value", min);
        return map;
    }

    /**
     * 由指定坐标从旧数组产生新数组
     *
     * @param list 数组
     * @param i    起始坐标
     * @param j    终止坐标
     * @return
     */
    public static ArrayList<Double> selectByIndexForDouble(ArrayList<Double> list, int i, int j) {
        ArrayList<Double> newList = new ArrayList<>();
        for (int k = i; k <= j; k++) {
            newList.add(list.get(k));
        }
        return newList;
    }

    public static ArrayList<Integer> selectByIndexForInt(ArrayList<Integer> list, int i, int j) {
        ArrayList<Integer> newList = new ArrayList<>();
        for (int k = i; k <= j; k++) {
            newList.add(list.get(k));
        }
        return newList;
    }

    /**
     * 矩阵减法
     *
     * @param list1 数组1
     * @param list2 数组2
     * @return
     */
    public static ArrayList<Double> listSub(ArrayList<Double> list1, ArrayList<Double> list2) {
        ArrayList<Double> newList = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            newList.add(list1.get(i) - list2.get(i));
        }
        return newList;
    }

    public static <E> Map<String, Object> getMinForInt(ArrayList<Integer> list) {
        int min = 10000;
        int index = 0;
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (min > list.get(i)) {
                min = list.get(i);
                index = i;
            }
        }
        map.put("index", index);
        map.put("value", min);
        return map;

    }

    public static double getInterpolation(double[][] zv, double x, int j) {
        int loc=0;
        for (int i = 0; i < zv.length; i++) {
            if (x>zv[i][j]&&x<zv[i+1][j]) {
                loc = i;
                break;
            }else {
                if (x==zv[i][j]){
                    return zv[i][1-j];
                }
            }
        }
        double result = ((zv[loc+1][1-j]-zv[loc][1-j])/(zv[loc+1][j]-zv[loc][j])*(x-zv[loc][j]))+zv[loc][1-j];
        return result;
    }
}
