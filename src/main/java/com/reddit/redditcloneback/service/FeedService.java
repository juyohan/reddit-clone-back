package com.reddit.redditcloneback.service;

import com.reddit.redditcloneback.common.key.TempKey;
import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import com.reddit.redditcloneback.dto.FeedDto;
import com.reddit.redditcloneback.exception.BusinessException;
import com.reddit.redditcloneback.exception.Exceptions;
import com.reddit.redditcloneback.model.Feed;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    private final UserService userService;

    /**
     * 게시글을 DB 에 저장시키는 함수이다.
     * @param create 게시글의 내용이 담긴 객체이다. (title, contents, nickname)
     * @return 성공을 했다면, 게시글의 고유 번호를 반환한다.
     */
    public FeedDto.Response.Success create(FeedDto.Request.Create create) {
        String key = new TempKey().getKey(6, false);
        User user = userService.findOneByNickname(create.getNickname());

        Feed feed = feedRepository.save(
                Feed.builder()
                        .title(create.getTitle())
                        .contents(create.getContents())
                        .user(user)
                        .uid(key)
                        .likeCount(0L)
                        .commentCount(0L)
                        .build()
        );

        return FeedDto.Response.Success.entityTo(feed);
    }

    /**
     * 게시글 수정을 해주는 함수이다.
     * update 처리를 더티체킹으로 처리하고, modifiedDate 의 값도 자동으로 주입시켜준다.
     * @param modify 게시글 수정의 데이터가 담긴 객체이다. (title, contents)
     * @return
     */
    @Transactional
    public FeedDto.Response.Success modifyFeed(FeedDto.Request.Modify modify) {
        Feed feed = findOneByUid(modify.getUid());
        feed.setTitle(modify.getTitle());
        feed.setContents(modify.getContents());

        return FeedDto.Response.Success.entityTo(feed);
    }

    /**
     * 게시글의 고유 번호를 통해 게시글을 찾는 함수이다.
     * @param uid 게시글의 고유 번호가 담긴 데이터
     * @return 게시글 Entity 를 반환한다.
     */
    public Feed findOneByUid(String uid) {
        return feedRepository.findByUid(uid).orElseThrow(
                () -> new BusinessException(Exceptions.FEED_NOT_FOUND)
        );
    }

    /**
     * 사용자의 게시글을 페이징 처리를 도와주는 함수이다.
     * @param pageParamDto 페이징 처리에 대한 객체 (size, page, sort)
     * @param nickname 게시글을 올린 사용자의 닉네임 데이터
     * @return DTO 변환을 해서 반환한다.
     */
    public PageDto findAllByNickname(
            PageParamDto pageParamDto,
            String nickname
    ) {
        Page<PageDto> page = feedRepository.searchPageBy(pageParamDto, nickname);
        return PageDto.toDto(page);
    }

    /**
     * 닉네임과 게시글 고유번호를 통해 하나의 게시글을 반환해주는 함수이다.
     * @param nickname 게시글을 올린 사용자 닉네임이다.
     * @param uid 게시글의 고유번호이다.
     * @return 하나의 게시글에 대한 데이터
     */
    public FeedDto.QueryDsl.Detail findOneByUidAndNickname(String nickname, String uid) {
        return feedRepository.searchOneByNicknameAndUid(nickname, uid).orElseThrow(
                () -> new BusinessException(Exceptions.FEED_NOT_FOUND)
        );
    }

    /**
     * 각 경로에 따라 페이징 처리를 해주는 함수이다.
     * 경로 (hot, new, rising, top) 을 추출해내고, switchPath 함수에 전달한다.
     * @param pageParamDto 페이징 처리를 위한 데이터를 담은 객체 (size, page, sort)
     * @param request HttpServletRequest
     * @return 페이징 처리를 한 데이터
     */
    public PageDto findAllPageByRequestPath(
            PageParamDto pageParamDto,
            HttpServletRequest request
    ) {
        String path = Arrays.stream(request.getRequestURI().split("/"))
                .reduce((first, second) -> second)
                .get();

        return PageDto.toDto(switchPath(pageParamDto, path));
    }

    /**
     * 각 경로에 대한 분기를 태우는 함수
     * @param pageParamDto 페이징을 위한 데이터 객체이다.
     * @param path 어떤 경로인지 담긴 문자열 데이터
     * @return 페이징 처리가 완성된 데이터
     */
    private Page<PageDto> switchPath(
            PageParamDto pageParamDto,
            String path
    ) {
        switch (path) {
            case "hot":
                return feedRepository.searchPageByRequestPath(pageParamDto, 15, 3);
            case "rising":
                return feedRepository.searchPageByRequestPath(pageParamDto, 8, 1);
            case "top":
                return feedRepository.searchPageByRequestPath(pageParamDto, 30, null);
            case "new":
                return feedRepository.searchPageByRequestPath(pageParamDto, null, 7);
            default:
                throw new BusinessException(Exceptions.NOT_FOUND_PATH);
        }
    }

    /**
     * 게시글의 좋아요에 대한 값을 저장시키기 위한 함수이다.
     * @param count 좋아요의 수
     * @param uid 게시글의 고유 번호
     */
    public void updateLikeCount(Long count, String uid) {
        feedRepository.updateLikeCountByUid(count, uid);
    }

    /**
     * 게시글을 삭제하는 함수이다.
     * @param delete 어떤 게시글을 삭제하는지 정보가 담긴 객체이다. (uid, nickname)
     */
    public void deleteOneFeed(FeedDto.Request.Delete delete) {
        feedRepository.deleteFeedByUidAndNickname(delete.getUid(), delete.getNickname());
    }

    /**
     * 게시글의 댓글의 개수를 저장하는 함수이다.
     * @param count 댓글의 개수
     * @param uid 게시글의 고유 번호
     */
    public void updateCommentCount(Long count, String uid) {
        feedRepository.updateCommentCountByUid(count, uid);
    }


//    // 피드 생성
//    public Feed createFeed(RequestFeedDTO requestFeedDTO,
//                           List<MultipartFile> multipartFiles
//                           ) {
//        // 로그인 되어있는 사용자 정보 가져옴
//        User user = userService.loginAfterFindUserName();
//
//        // 지정 URL 설정
//        String key = new TempKey().getKey(6, false);
//
//        Feed feed = new Feed();
//        feed.setUser(user);
//        feed.setKey(key);
////        feed.setCreateDate(LocalDateTime.now());
//        feed.setTitle(requestFeedDTO.getTitle());
//        feed.setContent(requestFeedDTO.getContent());
//        feed.setLikeCount(0);
//
////        List<MultipartFile> multipartFiles = requestFeedDTO.getMultipartFiles();
//
//        if (multipartFiles != null) {
//            List<FeedFile> files = feedFilesService.loadFiles(multipartFiles);
//
//            if (!files.isEmpty()) {
//                for (FeedFile file : files) {
//                    file.addFeed(feed);
//                }
//            }
//        }
//
//        return feedRepository.save(feed);
//    }
}
