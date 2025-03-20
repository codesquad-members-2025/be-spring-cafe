package codesquad.codestagram.dto;

import codesquad.codestagram.domain.Article;

public class ArticleDto {
    public static class ArticleRequestDto{
        private String title;
        private String content;

        public ArticleRequestDto(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public Article toArticle() {
            return new Article(title, content);
        }
    }
}
