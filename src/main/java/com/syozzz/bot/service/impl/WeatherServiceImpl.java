package com.syozzz.bot.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.syozzz.bot.client.WeatherClient;
import com.syozzz.bot.repository.CityRepository;
import com.syozzz.bot.repository.model.City;
import com.syozzz.bot.service.IWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 天气服务实现
 * @author syozzz
 * @version 1.0
 * @date 2020-10-16
 */
@Service
public class WeatherServiceImpl implements IWeatherService {

    /**
     * redis 集合操作
     */
    private final SetOperations<String, Long> setOperations;

    private final WeatherClient client;

    private final CityRepository cityRepository;

    private final RedisTemplate<String, Long> redisTemplate;

    private static final String CACHE_KEY_WEATHER_NOTIFY_GROUPS = "bot:weather:groups";

    private static final String CACHE_PREFIX_WEATHER_CITYS_OF_GROUP = "bot:weather:citys:group:";

    @Autowired
    public WeatherServiceImpl(RedisTemplate<String, Long> redisTemplate, WeatherClient client, CityRepository cityRepository) {
        this.redisTemplate = redisTemplate;
        this.setOperations = redisTemplate.opsForSet();
        this.client = client;
        this.cityRepository = cityRepository;
    }


    @Override
    public void registerGroupWeatherNotify(Long gId) {
        setOperations.add(CACHE_KEY_WEATHER_NOTIFY_GROUPS, gId);
    }

    @Override
    public void cancleWetherNotify(Long gId) {
        setOperations.remove(CACHE_KEY_WEATHER_NOTIFY_GROUPS, gId);
    }

    @Override
    public Collection<Long> getAllRegisterGroups() {
        return setOperations.members(CACHE_KEY_WEATHER_NOTIFY_GROUPS);
    }

    @Override
    public Boolean isAlreadyRegister(Long gId) {
        return setOperations.isMember(CACHE_KEY_WEATHER_NOTIFY_GROUPS, gId);
    }

    @Override
    public void addNotifyCity(Long gId, Long... cityIds) {
        String key = cityOfGroupKey(gId);
        setOperations.add(key, cityIds);
    }

    @Override
    public void removeNotifyCity(Long gId, Long... cityIds) {
        String key = cityOfGroupKey(gId);
        setOperations.remove(key, cityIds);
    }

    @Override
    public void deleteNotifyCity(Long gId) {
        redisTemplate.delete(cityOfGroupKey(gId));
    }

    @Override
    public Collection<Long> getAllCitysOfGroup(Long gId) {
        String key = cityOfGroupKey(gId);
        return setOperations.members(key);
    }

    @Override
    public Long findCityIdByCityName(String name) {
        City city = cityRepository.findCityByCnameIsLike(name);
        return city == null ? null : city.getCid();
    }

    @Override
    public List<String> getWeatherInfoByGroup(Long gId) {
        //获取群组关注的城市列表
        Collection<Long> citys = getAllCitysOfGroup(gId);
        if (citys.isEmpty()) {
            return Collections.emptyList();
        } else {
            return getWeatherInfo(citys);
        }
    }

    @Override
    public List<String> getWeatherInfoByCityName(String name) {
        //获取城市的ID
        Long cid = findCityIdByCityName(name);
        if (cid == null) {
            return Collections.emptyList();
        } else {
            return getWeatherInfo(CollUtil.newArrayList(cid));
        }
    }

    /**
     * http get 请求获取天气信息
     * @param citys 城市集合id
     * @return 天气信息集合
     */
    @SuppressWarnings("unchecked")
    private List<String> getWeatherInfo(Collection<Long> citys) {
        List<String> list = new ArrayList<>(citys.size());
        Iterator<Long> ite = citys.iterator();
        while (ite.hasNext()) {
            Object city = ite.next();
            Map<String, Object> weatherinfo = (Map<String, Object>) client.weather(String.valueOf(city)).get("weatherinfo");
            list.add(StrUtil.format("{}: {}-{}, {};",
                    weatherinfo.get("city"),
                    weatherinfo.get("temp1"),
                    weatherinfo.get("temp2"),
                    weatherinfo.get("weather")));
        }
        return list;
    }

    private String cityOfGroupKey(Long gId) {
        return CACHE_PREFIX_WEATHER_CITYS_OF_GROUP + gId;
    }

}
