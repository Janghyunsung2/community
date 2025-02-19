package com.myproject.community.api.board.initailize;

import com.myproject.community.api.board.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardInitializer implements CommandLineRunner {

    private final BoardService boardService;

    @Override
    public void run(String... args) throws Exception {


    }
}
