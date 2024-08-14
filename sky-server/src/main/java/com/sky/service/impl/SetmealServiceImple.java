package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImple implements SetmealService {


    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        Long id = BaseContext.getCurrentId();
        setmeal.setCreateUser(id);
        setmeal.setUpdateUser(id);
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setUpdateTime(LocalDateTime.now());

        setmealMapper.insert(setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
        }
        log.info("setmealDishes:{}", setmealDishes);
        setmealDishMapper.insert(setmealDishes);
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Transactional
    public PageResult queryPage(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVOPage = setmealMapper.queryPage(setmealPageQueryDTO);
//        for(SetmealVO setmealVO : setmealVOPage.getResult() ){
//            setmealVO.setSetmealDishes(setmealDishMapper.listSetmealDishes(setmealVO.getId()));
//        }
        PageResult pageResult = new PageResult();
        log.info("setmealVOPage:{}", setmealVOPage);
        pageResult.setTotal(setmealVOPage.getTotal());
        pageResult.setRecords(setmealVOPage.getResult());
        return pageResult;
    }

    /**
     * 起售停售
     * @param id
     * @param status
     */
    public void startOrStop(Long id, Integer status) {
        if(status.equals(StatusConstant.ENABLE)) {
            for (SetmealDish setmealDish : setmealDishMapper.listSetmealDishes(id)) {
                if (dishMapper.getById(setmealDish.getDishId()).getStatus() == StatusConstant.DISABLE) {
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        Setmeal setmeal = new Setmeal().builder()
                        .id(id)
                        .status(status)
                         .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Transactional
    public SetmealVO getSetmealById(Long id) {
        SetmealVO setmealVO = setmealMapper.getSetmealById(id);
        setmealVO.setSetmealDishes(setmealDishMapper.listSetmealDishes(setmealVO.getId()));
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        Long empId = BaseContext.getCurrentId();
        setmeal.setUpdateUser(empId);
        setmeal.setUpdateTime(LocalDateTime.now());

        setmealMapper.update(setmeal);
        if(setmealDTO.getSetmealDishes() !=null && setmealDTO.getSetmealDishes().size()>0) {
            setmealDishMapper.delete(setmeal.getId());
            List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
            }
            setmealDishMapper.insert(setmealDishes);
        }
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    public void deleteSetmeal(List<Long> ids) {

        String strIds = "";
        for(Long id : ids) {
            strIds += id + ",";
        }
        strIds = strIds.substring(0, strIds.length() - 1);
        setmealDishMapper.deleteByIds(strIds);
        setmealMapper.deleteByIds(strIds);
    }
}
