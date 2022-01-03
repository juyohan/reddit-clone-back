package com.reddit.redditcloneback;

import com.reddit.redditcloneback.common.key.TempKey;
import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import com.reddit.redditcloneback.model.Feed;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.FeedRepository;
import com.reddit.redditcloneback.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class FeedTest {

    @Autowired
    FeedRepository feedRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void initialData() {
        User user = User.builder()
                .kakaoId(0L)
                .enable(true)
                .imagePath("")
                .password("asdf")
                .email("dygks8557@naver.com")
                .nickname("jupaka")
                .build();

        userRepository.save(user);

        for (int i = 1 ; i < 101 ; i++) {
//            String key = new TempKey().getKey(6, false);
            Feed feed = Feed.builder()
                    .title("FAKE TITLE " + i)
                    .contents("FAKE CONTENTS " + i)
                    .likeCount(Long.valueOf(i))
                    .uid("abcde"+i)
                    .user(user)

                    .build();

            feedRepository.save(feed);
        }
    }

    @Test
    public void deleteFeedTest() {
        feedRepository.deleteFeedByUidAndNickname("abcde2", "jupaka");
    }

    @Test
    public void paginationTest() {

        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPage(1);
        pageParamDto.setSize(15);
        Map<String, Sort.Direction> sort = new HashMap<>();
//        sort.put("uid", Sort.Direction.ASC);
        pageParamDto.setSort(sort);

//        Page<PageDto> pageDtos = feedRepository.searchPageOrderBy(pageParamDto);
//        PageDto pageDto = PageDto.toDto(pageDtos);
//
//        assertThat(pageDto.getTotalElements()).isEqualTo(100);
    }

    @Test
    public void paginationTopFeed() {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPage(1);
        pageParamDto.setSize(8);
        Map<String, Sort.Direction> sort = new HashMap<>();
        pageParamDto.setSort(sort);

        Page<PageDto> pageDtos = feedRepository.searchPageByRequestPath(pageParamDto, null, null);

        assertThat(pageDtos.getTotalElements()).isEqualTo(71);
    }

    @Test
    public void paginationNewFeed() {
        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setPage(2);
        pageParamDto.setSize(8);
        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("likeCount", Sort.Direction.DESC);
        pageParamDto.setSort(sort);

//        Page<PageDto> pageDtos = feedRepository.searchPageGroupBy(pageParamDto, 10, 1);

//        assertThat(pageDtos.getNumberOfElements()).isEqualTo(8);
    }
}
