package Capstone.server.Controller;

import Capstone.server.DTO.Login.LoginData;
import Capstone.server.DTO.Login.LoginResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    JdbcTemplate jdbcTemplate;

    public LoginController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @ResponseBody
    @PostMapping("/user/login")
    public LoginResult userLogin(@RequestBody LoginData data) {
        LoginResult loginResult = new LoginResult();
        System.out.println("hello");
        String sql = "SELECT password, user_key, nickname FROM user WHERE email = ?";
        List<Result> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Result(
                    rs.getString("password"),
                    rs.getInt("user_key"),
                    rs.getString("nickname"));
        }, data.getEmail());

        if(results.isEmpty()) {
            loginResult.setMsg("id_error");
        } else {
            Result result = results.get(0);
            System.out.println(result.getPassword());
            if (data.getPassword().equals(result.getPassword())) {
                loginResult.setMsg("ok");
                loginResult.setUserKey(result.getUserKey());
                loginResult.setUserNickName(result.getNickName());
            } else {
                loginResult.setMsg("pw_error");
            }
        }
        return loginResult;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class Result {
        String password;
        int userKey;
        String nickName;
    };
}
