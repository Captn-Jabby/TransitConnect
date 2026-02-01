@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults()) 
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // 1. ALLOW THE ERROR PATH (CRITICAL FOR SPRING BOOT 3+)
            .requestMatchers("/error").permitAll() 
            
            // 2. ALLOW OPTIONS PREFLIGHTS
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            
            // 3. YOUR AUTH PATHS
            .requestMatchers("/auth/**").permitAll()
            
            .anyRequest().authenticated()
        )
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
