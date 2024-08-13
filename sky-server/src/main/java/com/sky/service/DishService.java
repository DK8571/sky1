package com.sky.service;

import com.sky.dto.DishDTO;



public interface DishService {

    /**
     *添加菜品与口味
     */
    void saveWithFlavor(DishDTO dishDTO);
}
