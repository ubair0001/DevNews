package se.kth.sda8.devNews.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repository;

    public List<Article> getAll() {
        return repository.findAll();
    }

    public List<Article> getAll_sort(String sort) {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(sort.equals("title") ? Article::getTitle : Article::getAuthorName))
                .collect(Collectors.toList());
    }

    public Optional<Article> getById(Long id) {
        return repository.findById(id);
    }

    public Article create(Article newArticle) {
        return repository.save(newArticle);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Article update(Article updateArticle) {
        return repository.save(updateArticle);
    }

    public List<Article> getAllByTopicId(Long topicId) {
        return repository.findAllByTopicsId(topicId);
    }
}
