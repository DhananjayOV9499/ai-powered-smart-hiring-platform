import fitz  # PyMuPDF
import docx

def extract_text_from_pdf(file) -> str:
    # Use file.read() directly (not file.file.read())
    doc = fitz.open(stream=file.read(), filetype="pdf")
    text = ""
    for page in doc:
        text += page.get_text()
    return text

def extract_text_from_docx(file) -> str:
    # Use file directly in docx.Document
    doc = docx.Document(file)
    return "\n".join([para.text for para in doc.paragraphs])
