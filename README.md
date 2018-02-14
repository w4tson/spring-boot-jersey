Spring boot app with Jersey to replicate: Support WebJars directly in Jersey #11355
===================================================================================

Steps to reproduce 

1. `./gradlew bootrun`
2. `curl http://localhost:8080/webjars/swagger-ui/3.6.1/index.html`
3. `curl http://localhost:8080/webjars/swagger-ui/index.html`

Expected result: swagger ui html page served twice
Actual Result: HTTP 404 x 2

Now edit build.gradle and swap the `spring-boot-starter-jersey` for `spring-boot-starter-web`
repeat the web call and observe the correct behaviour.


