package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品及口味
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        List<DishFlavor> dishFlavor = dishDTO.getFlavors();

        BeanUtils.copyProperties(dishDTO,dish);

        Long empId = BaseContext.getCurrentId();
        dish.setUpdateUser(empId);
        dish.setUpdateTime(LocalDateTime.now());
        dish.setCreateUser(empId);
        dish.setCreateTime(LocalDateTime.now());

        dishMapper.insert(dish);

        Long dishId = dish.getId();
        if(dishFlavor!=null&&dishFlavor.size()>0){
            dishFlavor.forEach(dishFlavor1 -> {dishFlavor1.setDishId(dishId);});
            dishFlavorMapper.insert(dishFlavor);
        }


    }
}
