package com.me.bookmarks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookmarkNotFoundException extends RuntimeException {

    public BookmarkNotFoundException(String s) {
        super(s);
    }
}
