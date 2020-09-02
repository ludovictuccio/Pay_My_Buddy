//package com.paymybuddy.config;
//
//@Configuration
//@EnableWebSecurity
//@ComponentScan
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//@Autowired
//public BeanConfiguration(@Lazy UserDetailsService userDetailsService) {
//this.userDetailsService = userDetailsService;
//}
//
//    @Bean("authenticationManager")
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authProvider());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/utilisateur/**").hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.POST, "/utilisateur/**").hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.PUT, "/utilisateur/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/utilisateur/**").hasRole("ADMIN").and().requestCache()
//                .requestCache(new NullRequestCache()).and().cors().and().csrf().disable();
//    }
//
//}
