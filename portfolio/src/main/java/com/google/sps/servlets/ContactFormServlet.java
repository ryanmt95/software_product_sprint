package com.google.sps.servlets;

import java.io.IOException;
import com.google.gson.Gson;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that processes text. */
@WebServlet("/contact_form")
public final class ContactFormServlet extends HttpServlet {
    
    private Map<String,String> form = new HashMap<String, String>();
    
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get input from form
    form.put("fname", getParameter(request, "fname", ""));
    form.put("lname", getParameter(request, "lname", ""));
    form.put("email", getParameter(request, "email", ""));
    form.put("mobile", getParameter(request, "mobile", ""));
    form.put("message", getParameter(request, "message", ""));

    // Respond with the result.
    response.setContentType("text/html;");
    response.getWriter().println(form);
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}