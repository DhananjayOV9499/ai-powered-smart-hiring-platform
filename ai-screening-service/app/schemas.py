from pydantic import BaseModel
from typing import List

class ResumeInput(BaseModel):
    resume_text: str

class ScreeningResult(BaseModel):
    summary: str
    skills: List[str]
    experience_level: str
    match_score: int
