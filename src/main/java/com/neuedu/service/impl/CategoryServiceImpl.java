package com.neuedu.service.impl;

import com.google.common.collect.Sets;
import com.neuedu.noUse.ServerResponse;
import com.neuedu.mapper.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //非空校验
        if (categoryId==null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //根据categoryId查询类别
        List<Category>categoryList=categoryMapper.findChildCategory(categoryId);
        return ServerResponse.serverResponseBySuccess(categoryList);
    }


    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //参数校验
        if (categoryName==null||categoryName.equals("")){
            return  ServerResponse.serverResponseByError("参数不为空");
        }
        //添加节点
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(1);
        int result=categoryMapper.insert(category);
        //返回结果
        if (result>0){
            //添加成功
            return  ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("添加失败");

    }

    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //参数校验
        if (categoryId==null||categoryId.equals("")){
            return  ServerResponse.serverResponseByError("类别id不为空");
        }
        if (categoryName==null||categoryName.equals("")){
            return  ServerResponse.serverResponseByError("类别名称不为空");
        }
        //根据categoryId查询
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.serverResponseByError("要修改的类别不存在");
        }
        //修改
        category.setName(categoryName);
        int result=categoryMapper.updateByPrimaryKey(category);
        //返回结果
        if (result>0){
            //修改成功
            return  ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("修改失败");

    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //参数的非空校验
        if (categoryId==null){
            return ServerResponse.serverResponseByError("类别id不存在");
        }
        //查询
        Set<Category>categorySet= Sets.newHashSet();
        categorySet=findAllChildCategory(categorySet,categoryId);
        //
        Set<Integer> integerSet=Sets.newHashSet();
        Iterator<Category> categoryIterator=categorySet.iterator();
        while(categoryIterator.hasNext()){
            Category category=categoryIterator.next();
            integerSet.add(category.getId());
        }
        return ServerResponse.serverResponseBySuccess(integerSet);
    }
    private Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            categorySet.add(category);
        }
        //查找categoryId下的子节点(平级）
        List<Category>categoryList=categoryMapper.findChildCategory(categoryId);
        if (categoryList!=null&&categoryList.size()>0){
            for (Category category1:categoryList){
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return  categorySet;
    }
}
