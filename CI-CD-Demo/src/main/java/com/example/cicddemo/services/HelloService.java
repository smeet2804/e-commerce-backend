package com.example.cicddemo.services;


import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getHelloMessage() {
        return "Hello World, Codepipeline Working! Hooray! Done with CI/CD :)";
    }
}
