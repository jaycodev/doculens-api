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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return documentRepository.findList(userId, folderId);
    }

    public DocumentDetailResponse getDetailById(Long id) {
        return documentRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + id));
    }

    @Transactional
    public DocumentDetailResponse create(CreateDocumentRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Folder folder = null;
        if (request.folderId() != null) {
            folder = folderRepository.findById(request.folderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
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

        d.setTitle(request.title());
        d.setExtractedFields(request.extractedFields());

        Document saved = documentRepository.save(d);

        return getDetailById(saved.getId());
    }

    @Transactional
    public DocumentDetailResponse addTags(Long documentId, Set<Long> tagIds) {

        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + documentId));

        Set<Tag> tags = tagRepository.findAllById(tagIds)
                .stream().collect(java.util.stream.Collectors.toSet());

        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("One or more tags do not exist");
        }

        doc.getTags().addAll(tags);

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