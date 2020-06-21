package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.google.gson.Gson;

@WebServlet("/loginStatus")
public class LoginStatusServlet extends HttpServlet {

  private UserService userService;
  private Map<String, Boolean> loginInformation = new HashMap<String, Boolean>();

  @Override
  public void init() {
    userService = UserServiceFactory.getUserService();
  }
    
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    loginInformation.put("loginStatus", userService.isUserLoggedIn());
    String data = convertToJson(loginInformation);
        
    response.setContentType("text/html;");
    response.getWriter().println(data);
  }

  private String convertToJson(Map data) {
      Gson gson = new Gson();
      String json = gson.toJson(data);
      return json;
  }
}