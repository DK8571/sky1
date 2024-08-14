package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
    @Autowired
    private SetmealDishMapper setmealDishMapper;
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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> dishVOS = dishMapper.pageQuery(dishPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(dishVOS.getTotal());
        pageResult.setRecords(dishVOS.getResult());
        return pageResult;
    }

    /**
     * 菜品删除
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {

        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(dish.getName()
                                                    + MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> SetmealIds = setmealDishMapper.getSetmealIdsByDishId(ids);
        if(SetmealIds.size()>0 && SetmealIds != null){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }


        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);

    }

    /**
     * 查询菜品
     * @param id
     */
    @Transactional
    public DishVO getDishById(Long id) {

        Dish dish = dishMapper.getById(id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.listDishFlavorByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        Long empId = BaseContext.getCurrentId();
        dish.setUpdateUser(empId);
        dish.setUpdateTime(LocalDateTime.now());

        List<DishFlavor> dishFlavor = dishDTO.getFlavors();


        dishMapper.updateDish(dish);
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        Long dishId = dish.getId();
        if(dishFlavor!=null&&dishFlavor.size()>0){
            dishFlavor.forEach(dishFlavor1 -> {dishFlavor1.setDishId(dishId);});
            dishFlavorMapper.insert(dishFlavor);
        }
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<DishVO> listDishByCategoryId(Long categoryId) {

        List<DishVO> dishVOS = dishMapper.listDishByCategoryId(categoryId);
        log.info("dishVOS:{}",dishVOS);
        return dishVOS;
    }

    @Override
    public void startOrStop(Integer status, Long id) {

        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.updateDish(dish);
    }
}
