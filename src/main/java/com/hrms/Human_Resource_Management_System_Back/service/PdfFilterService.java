package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfFilterService {

    private final DocumentRepository documentRepository;

    public PdfFilterService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> findPdfsContainingKeyword(String keyword) {
        List<Document> allDocs = documentRepository.findAll();

        return allDocs.stream()
                .filter(doc -> {
                    try {
                        return pdfContainsKeyword(doc.getData(), keyword);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private boolean pdfContainsKeyword(byte[] pdfBytes, String keyword) throws Exception {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDFTextStripper stripper = new PDFTextStripper();

            String text = stripper.getText(document);
            return text.toLowerCase().contains(keyword.toLowerCase());
        }
    }
}

