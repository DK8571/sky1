package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 通过菜品id查询
     * @param ids
     * @return
     */

    List<Long> getSetmealIdsByDishId(List<Long> ids);
}
