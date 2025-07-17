from fastapi import FastAPI, UploadFile, File, HTTPException, status
from fastapi.responses import JSONResponse
from typing import Optional
import logging
import sys
from app.utils.parser import extract_text_from_pdf, extract_text_from_docx
from app.schemas import ResumeInput, ScreeningResult
from app.service import analyze_resume_for_backend_role  # This is a sync function

# Initialize logger
logger = logging.getLogger(__name__)
app = FastAPI(title="AI Screening Service", version="1.0.0")

@app.post("/screen", response_model=ScreeningResult)
async def screen_resume(request: ResumeInput):
    """
    Analyze resume text for backend developer role
    """
    try:
        return analyze_resume_for_backend_role(request.resume_text)  # ❌ Removed `await`
    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e)
        )
    except Exception as e:
        logger.error(f"Analysis failed: {str(e)}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Failed to analyze resume"
        )

@app.post("/screen/upload", response_model=ScreeningResult)
async def screen_resume_upload(
    file: UploadFile = File(...),
    job_role: Optional[str] = "Backend Developer"
):
    print("Upload endpoint called")
    """
    Upload and analyze resume file (PDF/DOCX)
    """
    try:
        if not file.filename:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="No file provided"
            )

        if file.filename.endswith(".pdf"):
            text = extract_text_from_pdf(file)
        elif file.filename.endswith(".docx"):
            text = extract_text_from_docx(file)
        else:
            raise HTTPException(
                status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE,
                detail="Unsupported file type. Only PDF and DOCX are supported"
            )

        return analyze_resume_for_backend_role(text, job_role)  # ❌ Removed `await`

    except HTTPException:
        raise
    except Exception as e:
        raise  # This will print the full traceback in the terminal
