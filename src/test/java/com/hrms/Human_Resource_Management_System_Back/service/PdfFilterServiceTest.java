package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PdfFilterServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private PdfFilterService pdfFilterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    private byte[] createPdfWithText(String text) throws Exception {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            var page = new org.apache.pdfbox.pdmodel.PDPage();
            document.addPage(page);
            try (var contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(text);
                contentStream.endText();
            }
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Test
    void findPdfsContainingKeyword_shouldReturnEmptyListWhenNoMatch() throws Exception {
        // Arrange
        Document doc1 = new Document();
        doc1.setData(createPdfWithText("This is a test document."));
        when(documentRepository.findAll()).thenReturn(List.of(doc1));

        // Act
        List<Document> result = pdfFilterService.findPdfsContainingAnyKeywords(List.of("Nonexistent"));

        // Assert
        assertTrue(result.isEmpty());
    }



}
