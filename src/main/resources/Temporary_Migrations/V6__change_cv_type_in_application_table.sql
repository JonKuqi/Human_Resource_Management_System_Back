ALTER TABLE application
DROP COLUMN cv,

ALTER TABLE application
ADD COLUMN cv_document_id BIGINT,

ALTER TABLE application
ADD CONSTRAINT fk_cv_document
FOREIGN KEY (cv_document_id)
REFERENCES document(document_id)
ON DELETE SET NULL;


