package com.me.bookmarks;

import com.me.bookmarks.model.Account;
import com.me.bookmarks.model.AccountRepository;
import com.me.bookmarks.model.Bookmark;
import com.me.bookmarks.model.BookmarkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class BookmarksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookmarksApplication.class, args);
    }

    @Bean
    CommandLineRunner init(AccountRepository accountRepo, BookmarkRepository bookmarkRepo) {
        return (evt) -> Arrays.asList("tunguyen","kiddy","skynet").forEach(
                username -> {
                    Account account = accountRepo.save(new Account(username, "password"));
                    bookmarkRepo.save(new Bookmark("http://bookmark.com/1/" + username, "Description 1", account));
                    bookmarkRepo.save(new Bookmark("http://bookmark.com/2/" + username, "Description 2", account));
                }
        );
    }
}
