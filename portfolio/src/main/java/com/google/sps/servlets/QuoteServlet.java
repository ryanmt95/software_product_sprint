package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet returns a quote from HIMYM 
@WebServlet("/quote")
public final class QuoteServlet extends HttpServlet {

    private List<String> quotes = new ArrayList<>();

    @Override
    public void init() {
        quotes.add("If you're not scared, you're not taking a chance. "
        + "And if you're not taking a chance, then what the hell are you doing? - Ted Mosby");
        quotes.add("We struggle so hard to hold on to these things that are "
        + "eventually gonna disappear. And that's really noble. - Lily Aldrin");
        quotes.add("Whatever you do in life, it's not legendary until "
        + "your friends are there to see it. - Barney Stinson");
        quotes.add("That's life, you know, we never end up where you thought you "
        + "wanted to be. - Marshall Ericksen");
        quotes.add("I don't know where I'm gonna be in five years. I don't wanna know. "
        + "I want my life to be an adventure. - Robin Sherbatsky");
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String quote = quotes.get((int) (Math.random() * quotes.size()));

        response.setContentType("text/html");
        response.getWriter().println(quote);
    }
}