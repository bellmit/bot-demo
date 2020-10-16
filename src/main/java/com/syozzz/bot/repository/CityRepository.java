package com.syozzz.bot.repository;

import com.syozzz.bot.repository.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * City repo
 * @author syozzz
 * @version 1.0
 * @date 2020-10-16
 */
public interface CityRepository extends JpaRepository<City, Long> {

    /**
     * 根据城市名 查找城市ID
     * @param cname 城市名称
     * @return 城市
     */
    City findCityByCnameIsLike(String cname);

}
