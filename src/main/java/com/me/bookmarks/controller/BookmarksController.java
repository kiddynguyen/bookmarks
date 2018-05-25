package com.me.bookmarks.controller;

import com.me.bookmarks.model.AccountRepository;
import com.me.bookmarks.model.Bookmark;
import com.me.bookmarks.model.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/{username}/bookmarks")
public class BookmarksController {

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    BookmarkRepository bookmarkRepo;

    @GetMapping("/{bookmarkId}")
    Bookmark getBookmark(@PathVariable String username, @PathVariable Long bookmarkId) {
        validateUser(username);
        return bookmarkRepo.findById(bookmarkId).orElseThrow(
                () -> new BookmarkNotFoundException(String.valueOf(bookmarkId))
        );
    }

    @GetMapping
    Collection<Bookmark> getBookmarks(@PathVariable String username) {
        validateUser(username);
        return bookmarkRepo.findByAccountUsername(username);
    }

    @PostMapping
    ResponseEntity<Bookmark> addBookmark(@PathVariable String username, @RequestBody Bookmark input) {
        validateUser(username);
//        Account account = accountRepo.findByUsername(username).get();
//        Bookmark bookmark = bookmarkRepo.save(
//                new Bookmark(input.getUri(), input.getDescription(), account));

        return accountRepo.findByUsername(username)
                .map(account -> {
                    Bookmark bookmark = bookmarkRepo.save(new Bookmark(input.getUri(), input.getDescription(), account));
                    return ResponseEntity.ok(bookmark);
                }).orElse(ResponseEntity.noContent().build());
    }

    void validateUser(String username) {
        accountRepo.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username));
    }
}
