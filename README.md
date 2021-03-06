# BookRed   

### 💻  목차
* [🗣 프로젝트 소개](#-프로젝트-소개)
* [💾 DB Schema](#-DB-Schema)
* [🏫 사용한 기술](#-사용한-기술)
* [📀 프로젝트 배포](#-프로젝트-배포)
* [🤖 주요 기능](#-주요-기능)
* [📄 프로젝트 기술](#-프로젝트-기술)
  - [JWT (Json Web Token)](#jwt-json-web-token)
  - [OAuth2](#oauth2)
  - [File Upload & Read](#file-upload-&-read)

## 🗣 프로젝트 소개   
미국에서 운영중인 커뮤니티 사이트 [__레딧__](www.reddit.com, "Go Reddit"), `쓰레드 플로트형` & `피드형` 게시판을 모티브로 한 프로젝트입니다.
</br>
</br>

## 💾 DB Schema
<img width="805" alt="스크린샷 2022-01-03 오후 4 30 55" src="https://user-images.githubusercontent.com/20573091/147907626-e6113d4b-7312-4ea2-a20d-7f3e381a5772.png">

## 🏫 사용한 기술
- *Spring boot*
  - *JPA (Java Persistence API) & Query-Dsl*
  - *JWT (Json Web Token)*
  - *OAuth2*
  - *Mysql*
  - *Flyway*
  - *Redis*

</br>

## 📀 프로젝트 배포       

</br>
</br>

## 🤖 주요 기능

</br>
</br>

## 📄 프로젝트 기술

### JWT (Json Web Token)
 - 로그인 기능을 추가를 하려고 했을 때, 맨 처음엔 그저 비밀번호를 암호화를 시켜놓고 DB에 저장한 뒤, `passwordEncoder.matches()`의 메소드를 통해 일치 여부를 확인하고 일치하면 로그인 성공, 아니면 실패로 구현함.
 - 하지만 로그인에 성공을 한 뒤, 사용자한테 요청이 왔을 경우 해당 요청이 로그인한 사용자에게 요청이 왔는지, 아닌지를 판별하기 어렵다고 생각함.
 - 따라서 JWT의 기능을 도입시켜 로그인을 한 뒤, 로그인을 한 사용자가 요청을 보냈는지, 아닌지에 대한 판별을 할 수 있게 만들었음.
 - `Access-Token` 과 `Refresh-Token` 이렇게 두가지의 토큰을 통해, 보안성을 높이는 방법이 상용화가 되어있는 것을 보았음. 하지만, 가장 기본적인 Access-Token 만을 사용해서 기능이 제대로 구현이 되는지를 확인하고 추가하려고 함.
 - 만약, 사용자가 보낸 요청에 JWT의 유효기간이 10분보다 적게 남아있다면, 다시 새로운 토큰을 발급을 해줌.
 - 토큰이 탈취당할 가능성을 생각해 토큰 Claim에는 사용자의 권한만 추가를 해놓았고 Subject의 같은 경우 닉네임만 추가를 해놓았음.
 - 요청이 온 토큰에 검증 단계에서 실패를 하게 된다면, 알맞는 오류가 나온 에러부분을 클라이언트에게 전송함.

### OAuth2
- 카카오 로그인을 구현시키기 위해서 Spring Boot에서 지원을 해주는 OAuth2 프레임워크를 사용을 하게 되었음.
- 맨 처음엔 OAuth2를 사용하는게 아닌, 쌩으로 사용자가 카카오 서버에 `인가코드`를 요청하고, 받은 인가코드를 서버에서 받으면, 인가코드를 다시 카카오 서버로 요청을 보내서 `토큰`을 발급받고, 발급받은 토큰을 카카오 서버로 요청을 보내 `사용자의 정보`를 가져온 뒤, 연동이 되어있는지 확인하는 순서로 기능을 구현했음.
- 하지만 OAuth2라는 프레임워크를 사용을 해보니, 위의 모든 과정을 `application.yml`단에서 지정을 해주면 알아서 처리를 해줘서 자잘한 것들을 신경쓰지 않아도 되는 것을 알게 됨.
- `SuccessHandler`와 `FailureHandler`를 사용해 성공했을 때와 실패했을 때를 구분해서 사용하게 함.
- 연동이 되어있지 않거나 회원이 아닌 사용자가 로그인을 하려고 했을 때, 오류의 내용과 이동할 url 의 정보를 Json 형태로 클라이언트에게 전송함.
- 로그인에 성공을 했다면, 사용자의 정보와 토큰의 정보, 그리고 토큰의 유효 시간을 Json 형태로 클라이언트에게 전송을 해줌.

### File Upload & Read
- 파일관리에 대해 고민을 하고 있었을 때, 파일 자체를 DB에 저장을 해야하는가? 에 대해 궁금했음. 하지만, 자료를 찾아보니 DB엔 파일에 대한 정보만을 저장하고 원본은 S3나 서버에 저장하는 것을 알게됨.
- 그래서 파일을 저장할 때, 서버에 저장을 하고 정보를 DB에 저장을 시킴.
- 파일의 정보를 DB에 저장시킬 때, 파일의 이름을 같은 이름으로 저장시키는 사용자가 있을 수 있음. 파일을 저장시킬 때, UUID를 통해 중복값이 없게 만들어 저장을 시킴.
