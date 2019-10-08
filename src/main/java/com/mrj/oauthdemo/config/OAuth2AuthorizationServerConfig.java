package com.mrj.oauthdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MRJ
 * @date 2019/9/29 14:14
 * @Description: OAuth2授权服务器配置文件
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    //引入 SpringSecurity 中配置的 AuthenticationManager
    @Resource
    private AuthenticationManager authenticationManager;

    //引入 UserServiceDetail 服务
    @Resource
    private UserDetailsService userServiceDetail;

    /*//授权服务器安全配置器|配置 Token 节点的安全策略
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许表单登录
        security.allowFormAuthenticationForClients();
    }

    //客户端详细信息服务配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //这里直接把配置信息保存在内存中
        clients.inMemory()
                // 客户端 id
                .withClient("service-hi")
                //这里必须使用加密
                .secret(new BCryptPasswordEncoder().encode("123456"))
                //配置 GrantTypes
                //支持 刷新token
                // 使用密码模式
                .authorizedGrantTypes("client_credentials","refresh_token","password")
                //这个随便配了一个，暂时没用到
                .scopes("server")
                // 跳转uri
                .redirectUris("http://localhost");
    }

    //授权服务器端点配置器
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
    }*/


    //配置客户端信息
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //这里直接把配置信息保存在内存中
        clients.inMemory()
                .withClient("service-hi")
                //这里必须使用加密
                .secret(new BCryptPasswordEncoder().encode("123456"))
                //配置 GrantTypes
                //支持 刷新token
                // 使用密码模式
                .authorizedGrantTypes("client_credentials","refresh_token","password")
                //这个随便配了一个，暂时没用到
                .scopes("server");
    }

    //配置 Token 的节点 和 Token 服务
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        //添加自定义声明配置
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), jwtTokenEnhancer()));

        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userServiceDetail);

        //普通配置
        /*endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userServiceDetail)
                .accessTokenConverter(jwtTokenEnhancer());*/
    }

    // 配置 Token 节点的安全策略
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    //使用 jwt
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    // 配置 jwt 生成 策略，对称密钥
    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123456");   //密钥
        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
}

/**
 * 添加自定义声明
 */
class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(
            OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("username", authentication.getName());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
