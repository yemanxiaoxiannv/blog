package com.scs.web.blog.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scs.web.blog.dao.UserDao;
import com.scs.web.blog.domain.dto.UserAvatarDto;
import com.scs.web.blog.domain.dto.UserDto;
import com.scs.web.blog.domain.dto.UserUpdateDto;
import com.scs.web.blog.domain.vo.HotUserVO;
import com.scs.web.blog.entity.User;
import com.scs.web.blog.factory.DaoFactory;
import com.scs.web.blog.factory.ServiceFactory;
import com.scs.web.blog.listener.MySessionContext;
import com.scs.web.blog.service.UserService;
import com.scs.web.blog.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/user", "/api/user/*"})
public class UserController extends HttpServlet {
    private UserDao userDao = DaoFactory.getUserDaoInstance();
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService = ServiceFactory.getUserServiceInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI().trim();
        if (UrlPatten.USER.equals(uri)) {
            String page = req.getParameter("page");
            String keywords = req.getParameter("keywords");
            String count = req.getParameter("count");
            String userId = req.getParameter("userId");
            String f_user  =req.getParameter("f_user") ;
            if (userId != null && !"0".equals(userId)) {
                HttpUtil.getResponseBody(resp, userService.getUserInfo(Long.valueOf(userId)));
            }
            if (page != null) {
                HttpUtil.getResponseBody(resp, userService.selectByPage(Integer.parseInt(page), Integer.parseInt(count)));
            } else if (keywords != null) {
                HttpUtil.getResponseBody(resp, userService.selectByKeywords(keywords));
            } else {
                List<User> userList = (List<User>) userService.getHotUsers();
                List<HotUserVO> hotUserVOList = new ArrayList<>();
                for (User user : userList) {
                    try {
                        HotUserVO hotUserVO = new HotUserVO();
                        BeanUtils.copyProperties(hotUserVO, user);
                        long n = 0 ;
                        if (f_user.equals("")){
                            n = 0 ;
                        }else{
                            n = Long.parseLong(f_user) ;
                        }
                        hotUserVO.setFlag(userService.selectUserFnas(n, user.getId()));
                        hotUserVOList.add(hotUserVO);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                HttpUtil.getResponseBody(resp, Result.success(hotUserVOList));
            }
        } else {
            System.out.println(uri);
            HttpUtil.getResponseBody(resp, userService.getUser(Long.parseLong(HttpUtil.getPathParam(req))));
        }
    }

//    private void getHotUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Gson gson = new GsonBuilder().create();
//        Result result = userService.getHotUsers();
//        PrintWriter out = resp.getWriter();
//        out.print(gson.toJson(result));
//        out.close();
//    }
//
//    private void getUsersByPage(HttpServletResponse resp, int page, int count) throws IOException {
//        Gson gson = new GsonBuilder().create();
//        Result result = userService.selectByPage(page, count);
//        PrintWriter out = resp.getWriter();
//        out.print(gson.toJson(result));
//        out.close();
//    }
//
//    private void getUsersByKeywords(HttpServletResponse resp, String keywords) throws IOException {
//        Gson gson = new GsonBuilder().create();
//        Result result = userService.selectByKeywords(keywords);
//        PrintWriter out = resp.getWriter();
//        out.print(gson.toJson(result));
//        out.close();
//    }
//
//    private void getUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String info = req.getPathInfo().trim();
//        String id = info.substring(info.indexOf("/") + 1);
//        Gson gson = new GsonBuilder().create();
//        Result result = userService.getUser(Long.parseLong(id));
//        PrintWriter out = resp.getWriter();
//        out.print(gson.toJson(result));
//        out.close();
//    }
//
//    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
//        Gson gson = new GsonBuilder().create();
//        BufferedReader reader = req.getReader();
//        StringBuilder stringBuilder = new StringBuilder();
//        String line = null;
//        while ((line=reader.readLine())!=null){
//            stringBuilder.append(line);
//        }
//        System.out.println(stringBuilder);
//        UserDto user = gson.fromJson(stringBuilder.toString(), UserDto.class);
//        Result result = userService.upDate(user);
//        PrintWriter out = resp.getWriter();
//        out.print(gson.toJson(result));
//        out.close();
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI().trim();
        switch (uri) {
            case UrlPatten.USER_SIGN_IN:
                signIn(req, resp);
                break;
            case UrlPatten.USER_SIGN_UP:
                signUp(req, resp);
                break;
            case UrlPatten.USER_CHECK_MOBILE:
                String mobile = req.getParameter("mobile");
                HttpUtil.getResponseBody(resp, userService.checkMobile(mobile));
                break;
            case UrlPatten.USER_UPDATE:
                updateInfo(req, resp);
            case UrlPatten.USER_CHANGE_AVATAR:
                updateAvatar(req, resp);
                break;
            default:
        }
    }

    private void updateAvatar(HttpServletRequest req, HttpServletResponse resp) {
        String requestBody = HttpUtil.getRequestBody(req);
        logger.info("修改用户信息：" + requestBody);
        Gson gson = new GsonBuilder().create();
        UserAvatarDto userAvatarDto = gson.fromJson(requestBody, UserAvatarDto.class);
        HttpUtil.getResponseBody(resp,userService.updateAvatar(userAvatarDto.getUserId(), userAvatarDto.getAvatar()));
    }

    private void updateInfo(HttpServletRequest req, HttpServletResponse resp) {
        String requestBody = HttpUtil.getRequestBody(req);
        logger.info("修改用户信息：" + requestBody);
        Gson gson = new GsonBuilder().create();

        UserUpdateDto userUpdateDto = gson.fromJson(requestBody, UserUpdateDto.class);
        HttpUtil.getResponseBody(resp, userService.updateInfo(userUpdateDto));
    }

    private void signIn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        logger.info("登录用户信息：" + stringBuilder.toString());
        Gson gson = new GsonBuilder().create();
        UserDto userDto = gson.fromJson(stringBuilder.toString(), UserDto.class);
        String inputCode = userDto.getCode().trim();
        String sessionId = req.getHeader("Access-Token");
        System.out.println("客户端传来的JSESSIONID：" + sessionId);
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession session = myc.getSession(sessionId);
        String correctCode = session.getAttribute("code").toString();
        System.out.println("正确的验证码：" + correctCode);
        PrintWriter out = resp.getWriter();
        if (inputCode.equalsIgnoreCase(correctCode)) {
            Result result = userService.signIn(userDto);
            out.print(gson.toJson(result));
        } else {
            Result result = Result.failure(ResultCode.USER_VERIFY_CODE_ERROR);
            out.print(gson.toJson(result));
        }
        out.close();
    }

//    private void check(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.getWriter().println("验证账号");
//    }
//
//    private void follow(HttpServletResponse resp,  long id, int iscare) throws ServletException, IOException {
//        Gson gson = new GsonBuilder().create();
//        Result rs = userService.follow(id, iscare);
//        PrintWriter out = resp.getWriter();
//        out.print(gson.toJson(rs));
//        out.close();
//    }

    private void signUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        System.out.println(stringBuilder.toString());
        Gson gson = new GsonBuilder().create();
        Map<String, Object> map = null;
        UserDto userDto = gson.fromJson(stringBuilder.toString(), UserDto.class);
        String requestPath = req.getRequestURI().trim();
        PrintWriter out = resp.getWriter();
        Result result = userService.signUp(userDto);
        resp.setContentType("application/json;charset=utf-8");
        int code = resp.getStatus();
        String msg = code == 200 ? "成功":"失败";
        ResponseObject ro = ResponseObject.success(code,msg,userDto);
        PrintWriter out1 = resp.getWriter();
        out.print(gson.toJson(ro));
        out.close();
    }
}