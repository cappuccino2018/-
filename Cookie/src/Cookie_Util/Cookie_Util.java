package Cookie_Util;

import javax.servlet.http.Cookie;

public class Cookie_Util {

    public static Cookie FindCookie(Cookie[] cookie , String name){
        if(cookie != null){
            for (Cookie c:cookie) {
                if(name.equals(c.getName())){
                    return c;
                }
            }
        }
        return null;
    }
}
