package ru.todoo.utils;

import com.google.gson.JsonObject;
import ru.todoo.dao.PersistException;
import ru.todoo.domain.User;

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

    public static void process(HttpServletResponse response, ThrowingCallable<JsonObject, PersistException> callable)
            throws IOException {
        JsonObject result;
        try {
            result = callable.call();
        } catch (PersistException e) {
            result = JsonUtil.getBuilder().addProperty("message", e.getMessage()).build();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        response.getWriter().print(result.toString());
    }

    public static User getUser() {
        return new User() {
            @Override
            public Integer getId() {
                return 1;
            }
        };
    }
}
