package org.example.springmetricexternalapi.metric.controller;

import lombok.RequiredArgsConstructor;
import org.example.springmetricexternalapi.metric.LikeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/like")
    public void like() {
        likeService.like();
    }


    @GetMapping("/cancel")
    public void cancel() {
        likeService.cancel();
    }

    @GetMapping("/count")
    public int count() {
        return likeService.count();
    }
}
