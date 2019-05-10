    package com.neuedu.controller.background;

    import com.github.pagehelper.PageHelper;
    import com.github.pagehelper.PageInfo;
    import com.neuedu.common.Const;
    import com.neuedu.mapper.CategoryMapper;
    import com.neuedu.mapper.UserInfoMapper;
    import com.neuedu.pojo.Category;
    import com.neuedu.pojo.UserInfo;
    import com.neuedu.service.ICategoryService;
    import com.neuedu.common.ResultModel;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpSession;
    import java.util.List;
    import java.util.Map;

    @Controller
    public class CategoryManageController {
    @Autowired
    ICategoryService categoryService;
    @Autowired
        UserInfoMapper userInfoMapper;
    @Autowired
        CategoryMapper categoryMapper;
    /**
     * 管理员查看类别
     */
    @RequestMapping(value = "/manage/category/list.do")
    public String productList(HttpSession session, Map<String, Object> map) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        if (userInfo == null) {
            map.put("msg", "当前无人登陆，请登录");
            return "index";
        } else if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            map.put("error", "当前用户无此权限");
            return "index";                                         //错误页面，无权限
        } else {
            return "/category/categoryList";
        }
    }
    @GetMapping("/manage/category/list")
    @ResponseBody()
    public List<Category> cateList(){
        List<Category> lists=categoryMapper.selectAll();
        return lists;
    }
        /**
         * 添加类别
         */
        @RequestMapping(value = "/manage/category/add")
        public String insertProd(HttpServletRequest request){
            Integer parentId=Integer.valueOf(request.getParameter("parentId"));
            Integer status=Integer.valueOf(request.getParameter("status"));
            String name= request.getParameter("name");
            Category category=new Category();category.setParentId(parentId);category.setName(name);category.setStatus(status);
            categoryMapper.insert(category);
            return "redirect:/manage/category/list.do";

        }
        @RequestMapping(value = "/manage/category/add.do")
        public String toInsert(){
            return "/category/categoryAdd";
        }
        /**
         * 删除类别
         */
        @RequestMapping("/manage/category/delete.do")
        @ResponseBody
        public ResultModel deleteProduct(HttpSession session, Category category) {
            String username = (String) session.getAttribute("loginuser");
            UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
//        if (userInfo == null) {
//            map.put("msg", "当前无人登陆，请登录");
//            return "index";
//        } else if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
//            map.put("error", "当前用户无此权限");
//            return "index";                                         //错误页面，无权限
//        } else {
            int result=categoryMapper.deleteByPrimaryKey(category.getId());
            if (result>0){
                return new ResultModel(200,"ok","删除成功");}
            else {return new ResultModel(0,"no","删除失败");}
        }

        /**
         * 修改商品
         */
        @RequestMapping(value = "/manage/category/update.do/{id}")
        public String toUpdate(@PathVariable("id")Integer id, Model model){
            Category category=categoryMapper.selectByPrimaryKey(id);
            model.addAttribute("id",id);
            model.addAttribute("category",category);
            return "/category/categoryUpdate";
        }

        @RequestMapping(value = "/manage/category/update")
        public String updateProd(HttpServletRequest request) {
            Integer id=Integer.valueOf(request.getParameter("id"));
            Integer parentId=Integer.valueOf(request.getParameter("parentId"));
            Integer status=Integer.valueOf(request.getParameter("status"));
            String name= request.getParameter("name");
            Category category=new Category();category.setId(id);category.setParentId(parentId);category.setName(name);category.setStatus(status);
            categoryMapper.updateByPrimaryKey(category);
            return "redirect:/manage/category/list.do";
        }

        //    /**
    //     * 获取产品子节点（平级）
    //     */
    //    @RequestMapping(value = "/get_category.do")
    //    public ServerResponse get_category(HttpSession session, Integer categoryId){
    //        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
    //        if (userInfo==null){
    //            return ServerResponse.serverResponseByError(Const.ReponseCodeEnum.NEED_LOGIN.getCode(),Const.ReponseCodeEnum.NEED_LOGIN.getDesc());
    //        }
    //        //判断用户权限
    //        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
    //            return ServerResponse.serverResponseByError(Const.ReponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
    //        }
    //        return  categoryService.get_category(categoryId);
    //    }
    //    /**
    //     * 增加节点
    //     */
    //    @RequestMapping(value = "/add_category.do")
    //    public ServerResponse add_category(HttpSession session, @RequestParam(required = false,defaultValue = "0") Integer parentId, String categoryName){
    //        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
    //        if (userInfo==null){
    //            return ServerResponse.serverResponseByError(Const.ReponseCodeEnum.NEED_LOGIN.getCode(),Const.ReponseCodeEnum.NEED_LOGIN.getDesc());
    //        }
    //        //判断用户权限
    //        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
    //            return ServerResponse.serverResponseByError(Const.ReponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
    //        }
    //        return  categoryService.add_category(parentId,categoryName);
    //    }
    //    /**
    //     * 修改节点
    //     */
    //    @RequestMapping(value = "/set_category_name.do")
    //    public ServerResponse set_category_name(HttpSession session, Integer categoryId, String categoryName){
    //        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
    //        if (userInfo==null){
    //            return ServerResponse.serverResponseByError(Const.ReponseCodeEnum.NEED_LOGIN.getCode(),Const.ReponseCodeEnum.NEED_LOGIN.getDesc());
    //        }
    //        //判断用户权限
    //        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
    //            return ServerResponse.serverResponseByError(Const.ReponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
    //        }
    //        return  categoryService.set_category_name(categoryId,categoryName);
    //    }
    //
    //    /**
    //     * 获取当前分类id及递归子节点的categoryId
    //     */
    //    @RequestMapping(value = "/get_deep_category.do")
    //    public ServerResponse get_deep_category(HttpSession session, Integer categoryId){
    //        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
    //        if (userInfo==null){
    //            return ServerResponse.serverResponseByError(Const.ReponseCodeEnum.NEED_LOGIN.getCode(),Const.ReponseCodeEnum.NEED_LOGIN.getDesc());
    //        }
    //        //判断用户权限
    //        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
    //            return ServerResponse.serverResponseByError(Const.ReponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
    //        }
    //        return  categoryService.get_deep_category(categoryId);
    //    }
    }
