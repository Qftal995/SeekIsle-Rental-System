package com.bitejiuyeke.biteadminapi.map.constants;

/**
 * 地图常量信息
 */
public class MapConstants {

    /**
     * 搜索提示URL 参考 https://lbs.qq.com/service/webService/webServiceGuide/search/webServiceSuggestion
     */
    public static final String QQMAP_API_PLACE_SUGGESTION = "/ws/place/v1/suggestion";

    /**
     * 逆地址解析(经纬度->地址) URL  参考 https://lbs.qq.com/service/webService/webServiceGuide/address/Gcoder
     */
    public static final String QQMAP_GEOCODER = "/ws/geocoder/v1/";

    /**
     * 热门城市key
     */
    public final static String CFG_HOT_CITY_KEY = "sys_hot_city";


    /**
     * 城市缓存_KEY
     */
    public final static String  CACHE_MAP_CITY_KEY = "map:city:id";

    /**
     * 城市缓存_V2KEY
     */
    public final static String  CACHE_MAP_CITY_KEY_V2 = "map:city_v2:id";

    /**
     * 城市缓存_CODE_KEY
     */
    public final static String  CACHE_MAP_CITY_CODE = "map:city:code";

    /**
     * 城市拼音缓存_KEY
     */
    public final static String  CACHE_MAP_CITY_PINYIN_KEY = "map:city:pinyin";

    /**
     * 城市区划缓存key
     */
    public final static String  CACHE_MAP_CITY_CHILDREND_KEY = "map:city:children";

    /**
     * 热门城市缓存key
     */
    public final static String  CACHE_MAP_HOT_CITY = "map:city:hot";


    /**
     * QQ地图查询页数
     */
    public final static Integer QQMAP_DEFAULT_SIZE = 20;

    /**
     * 城市级别
     */
    public final static Integer CITY_LEVEL = 2;

}
