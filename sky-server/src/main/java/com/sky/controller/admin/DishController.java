package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("admin/dish")
@Api(tags = "菜品管理")
public class  DishController {

    @Autowired
   private DishService dishService;
    /**
     *新增菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result saveDish(@RequestBody DishDTO dishDTO) {

        log.info("新增菜品为:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @return
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> getDishByPage(DishPageQueryDTO dishPageQueryDTO) {

        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result deleteDish(@RequestParam List<Long> ids) {

        log.info("删除菜品:{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();

    }

    /**
     * 查询菜品
     * @param id
     * @return
     */
    @ApiOperation("查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id) {

        log.info("通过id查询菜品:{}",id);
        DishVO dishVO = dishService.getDishById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}",dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result listDishByCategoryId(@RequestParam Long categoryId) {

        log.info("根据分类id查询菜品:{}",categoryId);
        List<DishVO> dishVOS = dishService.listDishByCategoryId(categoryId);
        return Result.success(dishVOS);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售、停售")
    public Result startOrStop(@PathVariable Integer status , Long id){
        log.info("菜品起售、停售");
        dishService.startOrStop(status,id);
        return Result.success();
    }
}
