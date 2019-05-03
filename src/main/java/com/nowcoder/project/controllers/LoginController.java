package com.nowcoder.project.controllers;


import com.nowcoder.project.biz.LoginBiz;
import com.nowcoder.project.model.User;
import com.nowcoder.project.service.UserService;
import com.nowcoder.project.utils.CookieUtils;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by nowcoder on 2018/08/07 下午2:14
 */
@Controller
public class LoginController {

  @Autowired
  private LoginBiz loginBiz;

  @Autowired
  private UserService userService;

  @GetMapping("/users/register")
  public String register() {
    return "login/register";
  }

  @PostMapping(value = {"/users/register/do"})
  public String doRegister(
      Model model,
      HttpServletResponse response,
      @RequestParam("name") String name,
      @RequestParam("email") String email,
      @RequestParam("password") String password
  ) {

    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setPassword(password);

    try {
      String t = loginBiz.register(user);
      CookieUtils.writeCookie("t", t, response);
      return "redirect:/index";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "404";
    }
  }

  @RequestMapping(path = {"/users/login"}, method = {RequestMethod.GET})
  public String login() {
    return "login/login";
  }

  @RequestMapping(path = {"/users/login/do"}, method = {RequestMethod.POST})
  public String doLogin(
      Model model,
      HttpServletResponse response,
      @RequestParam("email") String email,
      @RequestParam("password") String password
  ) {
    try {
      String t = loginBiz.login(email, password);
      CookieUtils.writeCookie("t", t, response);
      return "redirect:/index";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "404";
    }
  }

  @RequestMapping(path = {"/users/logout/do"}, method = {RequestMethod.GET})
  public String doLogout(
      @CookieValue("t") String t
  ) {

    loginBiz.logout(t);
    return "redirect:/index";

  }
}
