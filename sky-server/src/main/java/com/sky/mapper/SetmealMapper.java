package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐
     * @param setmeal
     */
    @Insert("insert into setmeal (category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) values " +
            "(#{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Setmeal setmeal);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> queryPage(SetmealPageQueryDTO setmealPageQueryDTO);



    void update(Setmeal setmeal);

    @Select("select s.*,c.name as categoryName from setmeal s LEFT JOIN sky_take_out.category c on s.category_id = c.id where s.id = #{id}")
    SetmealVO getSetmealById(Long id);

    @Delete("delete from setmeal where id in (${strIds})")
    void deleteByIds(@Param("strIds") String strIds);
}
