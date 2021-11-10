Steps to reproduce https://github.com/spring-projects/spring-boot/issues/28604

`./gradlew test`

and observe 
```
ServiceCachedSpiedTest > this fails because cached() FAILED
    java.lang.IllegalStateException at AopTestUtils.java:97
        Caused by: java.lang.IllegalStateException at AopTestUtils.java:97
            Caused by: io.mockk.MockKException at MockKStub.kt:93
```