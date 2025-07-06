package com.odde.atddv2;

import com.odde.atddv2.entity.User;
import com.odde.atddv2.repository.UserRepository;
import com.odde.atddv2.util.JsonUtils;
import io.cucumber.java.After;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class TestSteps {
    private WebDriver webDriver = null;

    private String token = null;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private UserRepository userRepository;

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    @After
    public void closeBrowser() {
        getWebDriver().quit();
    }

    @当("测试环境")
    public void 测试环境() {
        getWebDriver().get("http://host.docker.internal:10081/");
        assertThat(getWebDriver().findElements(xpath("//*[text()='登录']"))).isNotEmpty();
        getWebDriver().quit();
    }

    @那么("打印Token")
    public void 打印_token() {
        System.out.println(token);
        assertThat(token).isNotEmpty();
    }

    @那么("打印百度为您找到的相关结果数")
    public void 打印百度为您找到的相关结果数() {
    }

    @假如("存在用户名为{string}和密码为{string}的用户")
    public void 存在用户名为和密码为的用户(String arg0, String arg1) {

        User user = new User();
        user.setUserName(arg0);
        user.setPassword(arg1);

        Optional<User> userOpt = userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
        if (userOpt.isEmpty()) {
            userRepository.save(user);
            System.out.println("新增的user.id=%s".formatted(user.getId()));
        }

    }

    @当("通过API以用户名为{string}和密码为{string}登录时")
    public void 通过api以用户名为和密码为登录时(String arg0, String arg1) {

        Map<String, String> params = new HashMap<>();
        params.put("userName", arg0);
        params.put("password", arg1);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "*/*");
        headers.set("Connection", "keep-alive");

        // 构造请求
        HttpEntity<String> entity = new HttpEntity<>(JsonUtils.toJSONString(params), headers);

        // 发起 POST 请求
        ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:10081/users/login",
            HttpMethod.POST,
            entity,
            String.class
        );

        this.token = response.getHeaders().get("Token").stream().findFirst().orElse(null);
    }

    @当("在百度搜索关键字{string}")
    public void 在百度搜索关键字(String arg0) {
    }

    private WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }
}
