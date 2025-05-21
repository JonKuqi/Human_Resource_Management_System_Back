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
    public List<Document> findPdfsContainingAnyKeywords(List<String> keywords) {
        List<Document> allDocs = documentRepository.findAll();

        return allDocs.stream()
                .filter(doc -> {
                    try {
                        return pdfContainsAnyKeyword(doc.getData(), keywords);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private boolean pdfContainsAnyKeyword(byte[] pdfBytes, List<String> keywords) throws Exception {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).toLowerCase();

            return keywords.stream().anyMatch(k -> text.contains(k.toLowerCase()));
        }
    }

}

