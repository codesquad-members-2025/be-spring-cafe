package codesquad.codestagram.service;

import codesquad.codestagram.entity.Article;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private List<Article> articles = new ArrayList<>();

    public List<Article> getArticles() {
        return articles;
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    @PostConstruct
    public void init(){
        articles.add(new Article(1,"InitializingBean implements afterPropertiesSet() 호출되지 않는 문제."
        ,"A 에 의존성을 가지는 B라는 클래스가 있습니다.\n" +
                "\n" +
                "B라는 클래스는 InitializingBean 을 상속하고 afterPropertiesSet을 구현하고 있습니다. 서버가 가동되면서 bean들이 초기화되는 시점에 B라는 클래스의 afterPropertiesSet 메소드는\n" +
                "\n" +
                "A라는 클래스의 특정 메소드인 afunc()를 호출하고 있습니다."));
        articles.add(new Article(2, "두 번째 게시글", "내용 2"));
    }


}
