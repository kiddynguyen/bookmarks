package com.me.bookmarks.controller;

import com.me.bookmarks.BookmarksApplication;
import com.me.bookmarks.model.Account;
import com.me.bookmarks.model.AccountRepository;
import com.me.bookmarks.model.Bookmark;
import com.me.bookmarks.model.BookmarkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookmarksApplication.class)
@WebAppConfiguration
public class BookmarkControllerTest {

    String username = "tunguyen";

    Account account;

    List<Bookmark> bookmarks = new ArrayList<>();

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    BookmarkRepository bookmarkRepo;

    @Autowired
    WebApplicationContext webAppContext;

    MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("UTF8"));

    MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

        bookmarkRepo.deleteAllInBatch();
        accountRepo.deleteAllInBatch();

        account = accountRepo.save(new Account(username, "password"));

        bookmarks.add(bookmarkRepo.save(new Bookmark("http://bookmark.com/1/"+username, "Desc1", account)));
        bookmarks.add(bookmarkRepo.save(new Bookmark("http://bookmark.com/2/"+username, "Desc2", account)));
    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(get("/kaka/bookmarks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getBookmark() throws Exception {
        String url = String.format("/%s/bookmarks/%d", username, bookmarks.get(0).getId());
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookmarks.get(0).getId().intValue())))
                .andExpect(jsonPath("$.uri", is(bookmarks.get(0).getUri())))
                .andExpect(jsonPath("$.description", is(bookmarks.get(0).getDescription())));
    }

    @Test
    public void getBookmarks() throws Exception {
        String url = String.format("/%s/bookmarks", username);
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookmarks.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].uri", is(bookmarks.get(0).getUri())))
                .andExpect(jsonPath("$[0].description", is(bookmarks.get(0).getDescription())))
                .andExpect(jsonPath("$[1].id", is(bookmarks.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].uri", is(bookmarks.get(1).getUri())))
                .andExpect(jsonPath("$[1].description", is(bookmarks.get(1).getDescription())));
    }
}
