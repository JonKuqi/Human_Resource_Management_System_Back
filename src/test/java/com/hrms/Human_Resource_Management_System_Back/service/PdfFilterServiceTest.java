package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfFilterServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private PdfFilterService pdfFilterService;

    private byte[] pdfWithKeyword;
    private byte[] pdfWithoutKeyword;

    @BeforeEach
    void setUp() throws Exception {
        pdfWithKeyword = Files.readAllBytes(Paths.get("src/test/resources/Sample_CV.pdf"));
    }



    @Test
    void shouldReturnTrueWhenKeywordExistsInPdf() throws Exception {
        byte[] pdf = Files.readAllBytes(Paths.get("src/test/resources/Sample_CV.pdf"));

        Document doc = new Document();
        doc.setDocumentId(1L);
        doc.setFileName("test_cv.pdf");
        doc.setData(pdf);

        when(documentRepository.findAll()).thenReturn(List.of(doc));

        List<Document> result = pdfFilterService.findPdfsContainingKeyword("Spring Boot");

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnFalseWhenKeywordDoesNotExistInPdf() throws Exception {
        byte[] pdf = Files.readAllBytes(Paths.get("src/test/resources/Sample_CV.pdf"));

        Document doc = new Document();
        doc.setDocumentId(2L);
        doc.setFileName("test_cv.pdf");
        doc.setData(pdf);

        when(documentRepository.findAll()).thenReturn(List.of(doc));

        // Use a word that definitely does NOT exist in the PDF
        List<Document> result = pdfFilterService.findPdfsContainingKeyword("Unicorns");

        assertEquals(0, result.size());
    }


}
