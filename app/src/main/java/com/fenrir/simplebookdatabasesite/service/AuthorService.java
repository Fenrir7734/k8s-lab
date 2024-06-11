package com.fenrir.simplebookdatabasesite.service;

import com.fenrir.simplebookdatabasesite.dto.AuthorDTO;
import com.fenrir.simplebookdatabasesite.dto.AuthorSlimDTO;
import com.fenrir.simplebookdatabasesite.dto.mapper.AuthorMapper;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceCreationException;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceNotFoundException;
import com.fenrir.simplebookdatabasesite.model.Author;
import com.fenrir.simplebookdatabasesite.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@AllArgsConstructor
@Service
public class AuthorService {
    private AuthorRepository authorRepository;
    private AuthorMapper authorMapper;

    public AuthorDTO get(Long id) {
        Author author = getById(id);
        return authorMapper.toAuthorDTO(author);
    }

    public Page<AuthorSlimDTO> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(authorMapper::toAuthorSlimDTO);
    }

    public AuthorDTO create(AuthorDTO authorDTO) {
        if (authorDTO.getId() != null) {
            throw new ResourceCreationException("ID value is prohibited during resource creation");
        }
        @Valid Author author = authorMapper.fromAuthorDTO(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toAuthorDTO(author);
    }

    public AuthorDTO update(Long id, AuthorDTO authorDTO) {
        @Valid Author updatedAuthor = authorMapper.fromAuthorDTO(authorDTO);
        Author author = getById(id);
        author.setFirstname(updatedAuthor.getFirstname());
        author.setLastname(updatedAuthor.getLastname());
        author.setBirthDate(updatedAuthor.getBirthDate());
        author.setDeathDate(updatedAuthor.getDeathDate());
        author.setDescription(updatedAuthor.getDescription());
        author = authorRepository.save(author);
        return authorMapper.toAuthorDTO(author);
    }

    public void delete(Long id) {
        Author author = getById(id);
        authorRepository.delete(author);
    }

    private Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Author was not found for id=%d", id)
                ));
    }
}
