package pri.demo.springboot.core.exception;

/**
 * 登录失败
 *
 * @author woieha320r
 */
public class LoginException extends RuntimeException {

    public LoginException(String msg) {
        super(msg);
    }

}
