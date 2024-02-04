package com.muano.brainson.post;

public record Post(Integer id,
                   Integer userId,
                   String title,
                   String body,
                   Integer version) { }