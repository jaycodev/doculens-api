package com.doculens.document.service;

import com.doculens.document.dto.request.CreateDocumentRequest;
import com.doculens.document.dto.request.UpdateDocumentRequest;
import com.doculens.document.dto.response.DocumentDetailResponse;
import com.doculens.document.dto.response.DocumentListResponse;
import com.doculens.document.model.Document;
import com.doculens.document.repository.DocumentRepository;
import com.doculens.folder.model.Folder;
import com.doculens.folder.repository.FolderRepository;
import com.doculens.shared.exception.ResourceNotFoundException;
import com.doculens.tag.model.Tag;
import com.doculens.tag.repository.TagRepository;
import com.doculens.user.model.User;
import com.doculens.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public List<DocumentListResponse> getList(Long userId, Long folderId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if (folderId != null) {
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + folderId));

            if (!folder.getUser().getId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Folder does not belong to this user");
            }
        }

        return documentRepository.findList(userId, folderId);
    }

    public DocumentDetailResponse getDetailById(Long id) {

        Document d = documentRepository.findFullById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + id));

        Long folderId = d.getFolder() != null ? d.getFolder().getId() : null;

        List<Long> tagIds = d.getTags() != null
                ? d.getTags().stream().map(Tag::getId).toList()
                : List.of();

        return new DocumentDetailResponse(
                d.getId(),
                d.getTitle(),
                d.getFileUrl(),
                d.getExtractedFields(),
                d.getOriginalFilename(),
                d.getMimeType(),
                folderId,
                d.getUser().getId(),
                tagIds,
                d.getCreatedAt());
    }

    @Transactional
    public DocumentDetailResponse create(CreateDocumentRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        Folder folder = null;

        if (request.folderId() != null) {
            folder = folderRepository.findById(request.folderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

            if (!folder.getUser().getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Folder does not belong to this user");
            }
        }

        Document d = new Document();
        d.setTitle(request.title());
        d.setUser(user);
        d.setFolder(folder);
        d.setFileUrl(request.fileUrl());
        d.setExtractedFields(request.extractedFields());
        d.setOriginalFilename(request.originalFilename());
        d.setMimeType(request.mimeType());

        Document saved = documentRepository.save(d);

        return getDetailById(saved.getId());
    }

    @Transactional
    public DocumentDetailResponse update(Long id, UpdateDocumentRequest request) {

        Document d = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (request.folderId() != null) {
            Folder folder = folderRepository.findById(request.folderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

            if (!folder.getUser().getId().equals(d.getUser().getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Folder does not belong to this user");
            }

            d.setFolder(folder);
        }

        d.setTitle(request.title());
        d.setExtractedFields(request.extractedFields());

        Document saved = documentRepository.save(d);

        return getDetailById(saved.getId());
    }

    @Transactional
    public DocumentDetailResponse addTags(Long documentId, Set<Long> tagIds) {

        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + documentId));

        Set<Long> existingTagIds = doc.getTags().stream()
                .map(Tag::getId)
                .collect(java.util.stream.Collectors.toSet());

        Set<Long> duplicates = tagIds.stream()
                .filter(existingTagIds::contains)
                .collect(java.util.stream.Collectors.toSet());

        if (!duplicates.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Document already has these tag(s): " + duplicates);
        }

        Set<Tag> tagsToAdd = tagRepository.findAllById(tagIds)
                .stream().collect(java.util.stream.Collectors.toSet());

        doc.getTags().addAll(tagsToAdd);

        documentRepository.save(doc);
        return getDetailById(documentId);
    }

    @Transactional
    public DocumentDetailResponse removeTag(Long documentId, Long tagId) {

        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + documentId));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagId));

        doc.getTags().remove(tag);

        documentRepository.save(doc);
        return getDetailById(documentId);
    }

    @Transactional
    public DocumentDetailResponse replaceTags(Long documentId, Set<Long> tagIds) {

        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + documentId));

        Set<Tag> newTags = tagRepository.findAllById(tagIds)
                .stream().collect(java.util.stream.Collectors.toSet());

        doc.setTags(newTags);

        documentRepository.save(doc);
        return getDetailById(documentId);
    }

    @Transactional
    public void delete(Long id) {
        if (!documentRepository.existsById(id))
            throw new ResourceNotFoundException("Document not found");
        documentRepository.deleteById(id);
    }
}