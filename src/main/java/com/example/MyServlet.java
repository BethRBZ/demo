package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/МуServlet")
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DIRECTORY_PATH = "C://Users//Beth//Desktop";

    public static Map<String, Integer> searchWordInFiles(String searchWord) {
        Map<String, Integer> wordCounts = new HashMap<>();
        File directory = new File(DIRECTORY_PATH);
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        int count = 0;
                        while ((line = reader.readLine()) != null) {
                            count += countOccurrences(line, searchWord);
                        }
                        if (count > 0) {
                            wordCounts.put(file.getName(), count);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return wordCounts;
    }

    private static int countOccurrences(String line, String searchWord) {
        int count = 0;
        int index = line.indexOf(searchWord);
        while (index >= 0) {
            count++;
            index = line.indexOf(searchWord, index + 1);
        }
        return count;
    }


    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchWord = req.getParameter("word");
        if (searchWord != null && !searchWord.isEmpty()) {
            Map<String, Integer> wordCounts = searchWordInFiles(searchWord);
            resp.setContentType("text/html");
            resp.getWriter().println("<html>");
            resp.getWriter().println("<head><title>Search results for \"" + searchWord + "\"</title></head>");
            resp.getWriter().println("<body>");
            if (wordCounts.isEmpty()) {
                resp.getWriter().println("<p>No results found for \"" + searchWord + "\"</p>");
            } else {
                resp.getWriter().println("<h1>Search results for \"" + searchWord + "\":</h1>");
                resp.getWriter().println("<table>");
                resp.getWriter().println("<tr><th>File name</th><th>Word count</th></tr>");
                for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                    resp.getWriter().println("<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue() + "</td></tr>");
                }
                resp.getWriter().println("</table>");
            }
            resp.getWriter().println("</body>");
            resp.getWriter().println("</html>");
        }
    }
}
