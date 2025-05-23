package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Service for filtering PDF documents based on keyword content.
 * <p>
 * This service is responsible for searching through PDF files stored in the system and
 * returning documents that contain any of the specified keywords. It uses Apache PDFBox
 * to extract text content from PDF binaries.
 * </p>
 */
@Service
public class PdfFilterService {
    /**
     * Repository for accessing document entities from the database.
     */
    private final DocumentRepository documentRepository;
    /**
     * Constructs a new {@code PdfFilterService} with the provided {@link DocumentRepository}.
     *
     * @param documentRepository the repository used to access document data
     */
    public PdfFilterService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    /**
     * Finds all PDF documents that contain any of the specified keywords.
     * <p>
     * This method retrieves all documents from the repository and filters them based on
     * whether their content includes any of the given keywords.
     * </p>
     *
     * @param keywords the list of keywords to search for
     * @return a list of {@link Document} instances whose contents include any of the keywords
     */
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
    /**
     * Checks whether the given PDF binary data contains any of the specified keywords.
     *
     * @param pdfBytes the binary content of the PDF file
     * @param keywords the list of keywords to check against
     * @return {@code true} if any keyword is found in the PDF text, {@code false} otherwise
     * @throws Exception if an error occurs while parsing the PDF
     */
    private boolean pdfContainsAnyKeyword(byte[] pdfBytes, List<String> keywords) throws Exception {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).toLowerCase();

            return keywords.stream().anyMatch(k -> text.contains(k.toLowerCase()));
        }
    }

}

