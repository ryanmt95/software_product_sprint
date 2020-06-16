package com.google.sps.servlets;

import java.io.IOException;
import com.google.gson.Gson;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;

/** Servlet that processes text. */
@WebServlet("/contact_form")
public final class ContactFormServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get input from form
    String fname = getParameter(request, "fname", "");
    String lname = getParameter(request, "lname", "");
    String email = getParameter(request, "email", "");
    String mobile = getParameter(request, "mobile", "");
    String message = getParameter(request, "message", "");

    // // Respond with the result.
    // response.setContentType("text/html;");
    // response.getWriter().println(form);

    Entity form_entity = new Entity("contact_form");
    form_entity.setProperty("fname", fname);
    form_entity.setProperty("lname", lname);
    form_entity.setProperty("email", email);
    form_entity.setProperty("mobile", mobile);
    form_entity.setProperty("message", message);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(form_entity);

    response.sendRedirect("/contact.html");
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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("contact_form");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Map<String, String>> users = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();

      Map<String, String> user = new HashMap<String, String>();

      String fname = (String) entity.getProperty("fname");
      String lname = (String) entity.getProperty("lname");
      String email = (String) entity.getProperty("email");
      String mobile = (String) entity.getProperty("mobile");
      String message = (String) entity.getProperty("message");

      user.put("fname", fname);
      user.put("lname", lname);
      user.put("email", email);
      user.put("mobile", mobile);
      user.put("message", message);

      users.add(user);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(users));
  }
}