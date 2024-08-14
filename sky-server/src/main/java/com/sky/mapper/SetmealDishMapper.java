package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    void insert(List<SetmealDish> setmealDishes);

    @Select("select * from setmeal_dish where setmeal_id =#{setmealId}")
    List<SetmealDish> listSetmealDishes(Long setmealId);

    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void delete(Long id);

    @Delete("delete from setmeal_dish where setmeal_id in ${strIds}")
    void deleteByIds(@Param("strIds") String strIds);
}
