package se.kth.sda8.devNews.articles;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleRestTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    ArticleService articleService;

    @Test
    public void testGetAllReturnEmptyArray() {
        //Arrange

        //Act
        Article[] responseArticles = testRestTemplate.getForObject("/articles", Article[].class);
        //Assert
        Assertions.assertEquals(0, responseArticles.length);
    }

    @Test
    public void testCreate() {
        // Arrange
        Article requestArticle = new Article(null, "Test title", "Test body", "Test author");

        // Act
        Article responseArticle = testRestTemplate.postForObject("/articles", requestArticle, Article.class);

        // Assert
        Assertions.assertEquals(requestArticle.getTitle(), responseArticle.getTitle());
        Assertions.assertEquals(requestArticle.getAuthorName(), responseArticle.getAuthorName());

        Article getByIdArticleResponse = testRestTemplate.getForObject("/articles/" + responseArticle.getId().toString(), Article.class);
        Assertions.assertEquals(requestArticle.getTitle(), getByIdArticleResponse.getTitle());
        Assertions.assertEquals(requestArticle.getAuthorName(), getByIdArticleResponse.getAuthorName());

        // Clean up
        testRestTemplate.delete("/Articles" + responseArticle.getId().toString());
    }

    @Test
    public void testUpdate() {
        // Arrange
        Article originalArticle = articleService.create(new Article(null, "Test title", "Test body", "Test author"));
        Article updatedArticle = new Article(originalArticle.getId(), "Updated title", "Updated body", "Updated author");

        // Act
        Article responseArticle = putForArticle(updatedArticle);


        // Assert
        Assertions.assertEquals(updatedArticle.getTitle(), responseArticle.getTitle());
        Assertions.assertEquals(updatedArticle.getAuthorName(), responseArticle.getAuthorName());

        Article getByIdArticleResponse = testRestTemplate.getForObject("/articles/" + updatedArticle.getId().toString(), Article.class);
        Assertions.assertEquals(updatedArticle.getTitle(), getByIdArticleResponse.getTitle());
        Assertions.assertEquals(updatedArticle.getAuthorName(), getByIdArticleResponse.getAuthorName());
    }

    private Article putForArticle(Article requestBody) {
        HttpEntity<Article> requestEntity = new HttpEntity<>(requestBody);
        HttpEntity<Article> response = testRestTemplate.exchange("/articles", HttpMethod.PUT, requestEntity, Article.class);
        return response.getBody();
    }

}
