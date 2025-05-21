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

    @BeforeEach
    void setUp() throws Exception {
        pdfWithKeyword = Files.readAllBytes(Paths.get("src/test/resources/Sample_CV.pdf"));
    }

    @Test
    void shouldReturnDocumentWhenAtLeastOneKeywordMatches() throws Exception {
        Document doc = Document.builder()
                .documentId(1L)
                .fileName("test_cv.pdf")
                .data(pdfWithKeyword)
                .build();

        when(documentRepository.findAll()).thenReturn(List.of(doc));

        List<String> keywords = List.of("Java","blla" );

        List<Document> result = pdfFilterService.findPdfsContainingAnyKeywords(keywords);

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoKeywordsMatch() throws Exception {
        Document doc = Document.builder()
                .documentId(2L)
                .fileName("test_cv.pdf")
                .data(pdfWithKeyword)
                .build();

        when(documentRepository.findAll()).thenReturn(List.of(doc));

        List<String> keywords = List.of("Unicorn", "BlockchainMagic");

        List<Document> result = pdfFilterService.findPdfsContainingAnyKeywords(keywords);

        assertEquals(0, result.size());
    }
}
