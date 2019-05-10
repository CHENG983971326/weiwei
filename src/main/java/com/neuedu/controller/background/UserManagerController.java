package com.neuedu.controller.background;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.noUse.ServerResponse;
import com.neuedu.mapper.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.common.ResultModel;
import com.neuedu.utils.MD5Utils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class UserManagerController {
    @Autowired
    IUserService userService;
    @Autowired
    UserInfoMapper userInfoMapper;

    @RequestMapping(value = "/user/manager")
    public String loginDo(){
        return "manager";
    }
    @RequestMapping(value = "/user/customer")
    public String loginCustomerDo(){
        return "/customer/main";
    }
    /**
     * 登陆
     */
    @RequestMapping(value = "/user/login")
    public String loginTo(){
        return "index";
    }
    @RequestMapping(value = "/user/login.do")
    public String login(HttpSession session, String username, String password, Map<String,Object>map){
        System.out.println(username+","+password);
        String str=userService.login(username,password);
        if (str.equals("用户名不存在")){
            map.put("msg","用户名不存在");
            return "index";
        }else if (str.equals("用户名或者密码错误")){
            map.put("msg","用户名或者密码错误");
            return "index";
        }
        else if (str.equals("普通用户登录")){
            session.setAttribute("loginuser",username);
            return "redirect:/user/customer";
        }else {
            session.setAttribute("loginuser",username);
            return str;
        }

    }


    /**
     * 退出登录
     */
    @RequestMapping(value = "/user/logout")
    public String logOut(HttpSession session){
        session.removeAttribute("loginuser");
        return "redirect:/user/login";
    }
    /**
    注册
     */
    @RequestMapping(value = "/user/registerpage")
    public String registerTo(){
        return "registerpage";
    }

    @PostMapping(value = "/user/register")
    @ResponseBody
    public  ResultModel register(HttpSession session,String username,String password,String repassword,
                            String email,String phone){
        if (!password.equals(repassword)){
            return ResultModel.build(10,"两次密码不一致，重新输入");
        }
        UserInfo userInfo=new UserInfo(username, password,email,phone,1,new BigDecimal(0));
        return  userService.register(userInfo);

    }
    /**
     * 管理员查看用户列表
     */
     @RequestMapping(value = "/user/list")
    public String userList(HttpSession session,Map<String,Object> map,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10")Integer pageSize,HttpServletRequest request) throws Exception {
         String username = (String) session.getAttribute("loginuser");
         UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);

         if (userInfo == null) {
             map.put("msg", "当前无人登陆，请登录");
             return "index";
         } else if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
             map.put("error", "当前用户无此权限");
             return "index";                                         //错误页面，无权限
         } else {
             return "/userlist/list";
         }
     }
    @GetMapping("/user/lists")
    @ResponseBody()
    public List<UserInfo> userInfoList(){
        List<UserInfo> list = userInfoMapper.selectAll();
        return list;
    }
    /**
     * 管理员删除用户
     */
    @RequestMapping("/user/delete.do")
    @ResponseBody
    public ResultModel deleteUser(HttpSession session, UserInfo user) {
        String username = (String) session.getAttribute("loginuser");
//        if (userInfo == null) {
//            map.put("msg", "当前无人登陆，请登录");
//            return "index";
//        } else if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
//            map.put("error", "当前用户无此权限");
//            return "index";                                         //错误页面，无权限
//        } else {
        if (user.getRole()==Const.RoleEnum.ROLE_ADMIN.getCode()){
            return new ResultModel(1,"no","无此权限");
        }
        int result=userInfoMapper.deleteByPrimaryKey(user.getId());
        if (result>0){
            return new ResultModel(200,"ok","删除成功");}
        else {return new ResultModel(0,"no","删除失败");}

    }

    /**
     * 用户信息回显
     */
    @RequestMapping (value = "/user/update.do/{id}")
    public String toUpdate(@PathVariable("id") String username,Model model){
        UserInfo userInfo=userInfoMapper.selectByPrimaryKey(Integer.valueOf(username));
        model.addAttribute("user",userInfo);
        return "/userlist/userUpdate";
    }
    /**
     * 管理员编辑用户信息
     */
    @RequestMapping(value = "/user/update")
    public String updateUser(HttpServletRequest request) {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Integer role=Integer.valueOf(request.getParameter("role"));
        String email= request.getParameter("email");
        String phone=request.getParameter("phone");
        String question= request.getParameter("question");
        String answer= request.getParameter("answer");
        BigDecimal balance= new BigDecimal(request.getParameter("balance")) ;
        int so=userInfoMapper.checkEmail(email);
        if (so!=1) {
            UserInfo userInfo1=userInfoMapper.selectUserInfoByUsername(username);
            userInfo1.setPassword(password);userInfo1.setRole(role);userInfo1.setEmail(email);userInfo1.setPhone(phone);userInfo1.setQuestion(question);
             userInfo1.setAnswer(answer);userInfo1.setBalance(balance);
            System.out.println(userInfo1);
            userInfoMapper.updateUserBySelectActive(userInfo1);
            return "redirect:/user/list";
        }else {
            return "redirect:/user/list";
        }
    }

    /**
     * 管理员添加用户
     */
    @RequestMapping(value = "/user/add")
    public String insertUser(HttpServletRequest request) {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Integer role=Integer.valueOf(request.getParameter("role"));
        String email= request.getParameter("email");
        String phone=request.getParameter("phone");
        String question= request.getParameter("question");
        String answer= request.getParameter("answer");
        BigDecimal balance= new BigDecimal(request.getParameter("balance")) ;
        int count=userInfoMapper.checkUsername(username);
        int so=userInfoMapper.checkEmail(email);
        if (count!=1&&so!=1) {
            UserInfo userInfo = new UserInfo(username, password, email, phone,role, balance);
            int result = userInfoMapper.insert(userInfo);
            return "redirect:/user/list";
        }else {
            return "/user/list";
        }
    }
    @RequestMapping(value = "/user/add.do")
    public String toInsertUser(){
        return "/userlist/userAdd";
    }

    /**
     * 管理员重置密码
     */
    @RequestMapping(value = "/user/resetPassword.do")
    public String resetPassword(HttpSession session,Model model){
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        model.addAttribute("user",userInfo);
        return "/extra/resetPassword";
    }


    @PostMapping(value = "/user/resetPassword")
    @ResponseBody
    public ResultModel toReset(HttpSession session,String oldPassword,String newPassword,String renewPassword,Model model) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo=userInfoMapper.selectUserInfoByUsername(username);
        if (!MD5Utils.getMD5Code(oldPassword).equals(userInfo.getPassword())){
            return ResultModel.build(10,"原密码不匹配，请重新输入");
        }else if (!newPassword.equals(renewPassword)){
            return ResultModel.build(20,"两次新密码不匹配，请重新输入");
        }else {
            userInfo.setPassword(MD5Utils.getMD5Code(newPassword));
            userInfoMapper.updateByPrimaryKey(userInfo);
            return ResultModel.ok();
        }

    }
    /**
     * 用户重置密码
     */
    @RequestMapping(value = "/customer/resetPassword.do")
    public String reNewPassword(){
        return "/customer/reNewPassword";
    }


    @RequestMapping(value = "/customer/resetPassword")
    public String toNewReset(HttpSession session,HttpServletRequest request,Model model) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo=userInfoMapper.selectUserInfoByUsername(username);

        String oldPassword=MD5Utils.getMD5Code(request.getParameter("oldPassword"));
        String newPassword=request.getParameter("newPassword");
        String renewPassword=request.getParameter("renewPassword");

        if (!oldPassword.equals(userInfo.getPassword())){
            model.addAttribute("msg","原密码不匹配，请重新输入");
            return "/customer/reNewPassword";
        }else if (!newPassword.equals(renewPassword)){
            model.addAttribute("msg","两次新密码不匹配，请重新输入");
            return "/customer/reNewPassword";
        }else {
            userInfo.setPassword(MD5Utils.getMD5Code(newPassword));
            userInfoMapper.updateByPrimaryKey(userInfo);
            return "redirect:/customer/product/list.do";
        }

    }
    /**
     * 普通用户余额查询
     */
    @RequestMapping(value = "/customer/balance.do")
    public String queryBalance(HttpSession session,Model model){
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo=userInfoMapper.selectUserInfoByUsername(username);
        model.addAttribute("balance",userInfo.getBalance());
        return "/customer/balance";
    }
    /**
     * 管理员为客户充值
     */
    @RequestMapping(value = "/user/recharge.do")
    public String recharge(Model model){
        List<UserInfo> list=userInfoMapper.selectAll();

        model.addAttribute("lists",list);
        return "/extra/recharge";
    }

    @PostMapping(value = "/user/recharge")
    @ResponseBody
    public ResultModel toCharge(HttpSession session,String userId,String balance,Model model) {
//        String username=request.getParameter("username");
       UserInfo userInfo=userInfoMapper.selectByPrimaryKey(Integer.valueOf(userId));
//       String username1=userInfo.getUsername();
        if (userInfo.getBalance()==null) {
            userInfo.setBalance(new BigDecimal("1000"));
        }
        BigDecimal bigDecimal=userInfo.getBalance();
//        if (!username1.equals(username)){
//            model.addAttribute("msg","用户id与用户名不匹配");
//            return "/extra/recharge";
//        }
        if (Integer.parseInt(balance)>0){
            int a=bigDecimal.intValue();
            int b=Integer.parseInt(balance);
            userInfo.setBalance(new BigDecimal(a+b));
            System.out.println(userInfo.getBalance());
            userInfoMapper.updateByPrimaryKey(userInfo);
            return ResultModel.ok();
        }
        else {
           return ResultModel.build(0,"充值失败");
        }

    }
    @GetMapping(value = "/user/recharge/getName")
    @ResponseBody()
    public ResultModel getName(Integer id){
      UserInfo userInfo1=  userInfoMapper.selectByPrimaryKey(id);
     if (!userInfo1.getUsername().equals("")&&userInfo1.getUsername()!=null){
         System.out.println(123);
         return  ResultModel.build(200,"成功",userInfo1.getUsername());
     }
      else  return new ResultModel(0,"名字为空",null);
    }

}
