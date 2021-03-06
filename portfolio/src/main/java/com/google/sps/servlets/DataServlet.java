// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import com.google.gson.Gson;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private Map<String,String> summary = new HashMap<String, String>();

    @Override
    public void init() {
        summary.put("intro", "There is no too short, too tall, too heavy, too warm, too wet, or too humid. "
        + "There is just one excuse: too weak. - Alex Megos");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String summary_json = convertToJson(summary);
        
        response.setContentType("text/html;");
        response.getWriter().println(summary_json);
    }

    private String convertToJson(Map summary) {
        Gson gson = new Gson();
        String json = gson.toJson(summary);
        return json;
    }
}