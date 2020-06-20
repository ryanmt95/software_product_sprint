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
import com.google.sps.data.Message;

/** Servlet that processes text. */
@WebServlet("/message")
public final class ContactFormServlet extends HttpServlet {

  private DatastoreService datastore;
  
  @Override
  public void init() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    this.datastore = datastore;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get input from form
    String firstName = getParameter(request, "firstName", "");
    String lastName = getParameter(request, "lastName", "");
    String email = getParameter(request, "email", "");
    String mobile = getParameter(request, "mobile", "");
    String comments = getParameter(request, "comments", "");

    Entity formEntity = new Entity("Message");
    formEntity.setProperty("firstName", firstName);
    formEntity.setProperty("lastName", lastName);
    formEntity.setProperty("email", email);
    formEntity.setProperty("mobile", mobile);
    formEntity.setProperty("comments", comments);

    this.datastore.put(formEntity);

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
    Query query = new Query("Message");

    PreparedQuery results = this.datastore.prepare(query);

    List<Message> messageList = new ArrayList<>();
    for (Entity entity : results.asIterable()) {

      String firstName = (String) entity.getProperty("firstName");
      String lastName = (String) entity.getProperty("lastName");
      String email = (String) entity.getProperty("email");
      String mobile = (String) entity.getProperty("mobile");
      String comments = (String) entity.getProperty("comments");

      Message message = new Message(firstName, lastName, email, mobile, comments);

      messageList.add(message);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(messageList));
  }
}