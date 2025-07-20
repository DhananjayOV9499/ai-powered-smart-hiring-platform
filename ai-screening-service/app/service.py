import os
import json
import logging
from pathlib import Path
from typing import Optional, List
from dotenv import load_dotenv
from openai import OpenAI
from app.schemas import ScreeningResult

logger = logging.getLogger(__name__)

class ResumeAnalyzer:
    def __init__(self):
        # Load environment variables
        env_path = Path(__file__).resolve().parent.parent / ".env"
        load_dotenv(dotenv_path=env_path)

        self.api_key = os.getenv("OPENAI_API_KEY")
        if not self.api_key:
            raise ValueError("OpenAI API key not configured")
        
        # Initialize OpenAI client
        self.client = OpenAI(
            api_key=self.api_key,
            timeout=30  # Global timeout
        )

    def analyze_backend_developer(
        self,
        resume_content: str,
        required_skills: Optional[List[str]] = None
    ) -> ScreeningResult:
        """Analyze resume for backend developer role"""
        if not isinstance(resume_content, str):
            raise TypeError("Resume content must be a string")
        if not resume_content.strip():
            raise ValueError("Resume content cannot be empty")

        prompt = self._build_prompt(resume_content, required_skills)

        try:
            response = self.client.chat.completions.create(
                model="gpt-3.5-turbo",
                messages=[
                    {"role": "system", "content": "You are an expert technical recruiter."},
                    {"role": "user", "content": prompt}
                ],
                temperature=0.2
            )
            return self._parse_response(response)

        except Exception as e:
            logger.error(f"Resume analysis failed: {str(e)}")
            raise RuntimeError(f"Resume analysis failed: {str(e)}")

    def _build_prompt(self, resume_text: str, skills: Optional[List[str]]) -> str:
        """Constructs the analysis prompt"""
        skills = skills or ["Java", "Python", "SQL"]
        return (
            "Analyze the following resume and return ONLY a valid JSON object with the following keys:\n"
            "- summary: A short 1–2 line summary of the candidate\n"
            "- technical_skills: A list of technical skills from the resume\n"
            "- experience_level: Junior, Mid or Senior\n"
            "- match_score: Number from 0–100 based on how well this candidate matches a Backend Developer role\n\n"
            f"Required skills to match (for context): {', '.join(skills)}\n\n"
            f"Resume:\n{resume_text}\n"
            "Respond ONLY with the JSON object and no other text."
        )

    def _parse_response(self, response) -> ScreeningResult:
        """Parses the response into a ScreeningResult object"""
        try:
            content = response.choices[0].message.content.strip()
            result = json.loads(content)
            return ScreeningResult(
                summary=result.get("summary", "No summary"),
                skills=result.get("technical_skills", []),
                experience_level=result.get("experience_level", "Unknown"),
                match_score=result.get("match_score", 0)
            )
        except (json.JSONDecodeError, AttributeError, IndexError) as e:
            logger.warning(f"Failed to parse JSON response: {str(e)}")
            return self._fallback_parsing(response)

    def _fallback_parsing(self, response):
        """Fallback in case the model doesn't return valid JSON"""
        content = response.choices[0].message.content.strip()
        return ScreeningResult(
            summary=content,
            skills=[],
            experience_level="Unknown",
            match_score=0
        )


# ✅ Wrapper for FastAPI usage
analyzer = ResumeAnalyzer()

def analyze_resume_for_backend_role(resume_text: str, skills: Optional[List[str]] = None) -> ScreeningResult:
    return analyzer.analyze_backend_developer(resume_text, skills)
