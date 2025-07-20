import fitz  # PyMuPDF
import docx
from io import BytesIO

def extract_text_from_pdf(file) -> str:
    # Use file.file.read() for synchronous reading
    doc = fitz.open(stream=file.file.read(), filetype="pdf")
    text = ""
    for page in doc:
        text += page.get_text()
    return text

def extract_text_from_docx(file) -> str:
    file.file.seek(0)
    doc = docx.Document(BytesIO(file.file.read()))
    return "\n".join([para.text for para in doc.paragraphs])
