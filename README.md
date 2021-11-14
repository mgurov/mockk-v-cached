Steps to reproduce https://github.com/spring-projects/spring-boot/issues/28604

`./gradlew test`

and observe 
```
MockkSpykingTest > this is the call that fails for spring mockito ResetMocksTestExecutionListener() FAILED
    java.lang.IllegalStateException at ServiceSpiedTest.kt:45
        Caused by: java.lang.IllegalStateException at ServiceSpiedTest.kt:45
            Caused by: io.mockk.MockKException at ServiceSpiedTest.kt:45

MockkSpykingTest > hardcoded unwrapping() FAILED
    io.mockk.MockKException at ServiceSpiedTest.kt:34
    
```