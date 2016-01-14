package ru.todoo.utils;

import com.google.gson.JsonElement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.todoo.utils.LambdaExceptionUtil.ThrowingCallable;

/**
 * Created by Dmitriy Dzhevaga on 28.11.2015.
 */
public class ServletUtil {
    private ServletUtil() {
    }

    public static String readContent(HttpServletRequest request) throws IOException {
        return request.getReader().lines().reduce("", (s1, s2) -> s1 + s2);
    }

    public static int getIdFromUri(HttpServletRequest request) {
        return Integer.valueOf(request.getPathInfo().replaceAll("/", ""));
    }

    public static void process(HttpServletResponse response, ThrowingCallable<JsonElement, Exception> callable)
            throws IOException {
        try {
            JsonElement result = callable.call();
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(result.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print(e.toString());
        }
    }
}
