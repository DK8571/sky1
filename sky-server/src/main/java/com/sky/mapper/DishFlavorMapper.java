package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 新增菜品口味
     * @param dishFlavor
     */
    void insert(List<DishFlavor> dishFlavor);
}
